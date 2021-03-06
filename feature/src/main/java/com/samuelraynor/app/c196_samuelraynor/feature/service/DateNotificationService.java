/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.samuelraynor.app.c196_samuelraynor.feature.R;
import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Assessment;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Check the database for exam due dates and course
 * end dates. Send a notification if there is an
 * exam due or course ending within 7 days from now.
 */
public class DateNotificationService extends Service {

    private static final String TAG = "DateNotificationService";

    private AlarmManager am;
    private PendingIntent pi;
    private Context context;

    public DateNotificationService() {
    }

    public DateNotificationService(Context applicationContext) {
        super();
        this.context = applicationContext;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create notification channel
            createNotificationChannel(); // for API 26+
        }

        // on creation, schedule ourselves to run periodically
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Helper.scheduleJob(getApplicationContext());
        } else {
            // use the pre-API 21 way
            Calendar cal = Calendar.getInstance();
            this.am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent serviceIntent = new Intent(this, DateNotificationService.class);
            this.pi = PendingIntent.getService(getApplicationContext(), 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            this.am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), (1000 * 30), this.pi);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called.");
        Toast.makeText(this, "WGU Service Destroyed", Toast.LENGTH_LONG).show();

        // we are being destroyed. reschedule or restart our service.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i(TAG, "Rescheduling job service.");
            Helper.scheduleJob(getApplicationContext());
        } else {
            Log.i(TAG, "Sending broadcastIntent.");
            Intent broadcastIntent = new Intent(this, Autostart.class);
            sendBroadcast(broadcastIntent);
            this.am.cancel(this.pi);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "WGU Service Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onStartCommand called.");

        // Get the service task
        Runnable r = getServiceRunnable();

        // Start the thread and do our work.
        Thread serviceThread = new Thread(r);
        serviceThread.start();

        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private Runnable getServiceRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                StudentData studentData = new StudentData(getBaseContext());
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                // Check the database for exam due dates and course
                // start end dates. Send a notification if there is an
                // exam due or course starting or ending within 7 days from now.
                ArrayList<Assessment> examsDue = studentData.getExamsDue();

                // check for courses starting in the next 7 days
                ArrayList<Course> coursesStarting = studentData.getCoursesStarting();

                // check for courses ending in the next 7 days
                ArrayList<Course> coursesEnding = studentData.getCoursesEnding();

                // Found items coming due?
                ArrayList<String[]> items = new ArrayList<>();
                for (Assessment assessment : examsDue) {
                    items.add(new String[]{"Assessment Due: " + assessment.getTitle(), "Due on: " + formatter.format(assessment.getDueDate())});
                }
                for (Course starting : coursesStarting) {
                    items.add(new String[]{"Course Starting: " + starting.getName(), "Starts on: " + formatter.format(starting.getStart())});
                }
                for (Course ending : coursesEnding) {
                    items.add(new String[]{"Course ending: " + ending.getName(), "Ends on: " + formatter.format(ending.getEnd())});
                }

                if (!items.isEmpty()) {
                    int id = 1;
                    for (String[] item : items) {
                        // Yes: Display a notification
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), "DateNotificationChannel")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(item[0])
                                .setContentText(item[1])
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(id++, mBuilder.build());
                    }
                } else {
                    /*// No: Display a notification
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), "DateNotificationChannel")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("No Items")
                            .setContentText("There are no exams due or courses starting/ending within the next 7 days!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(1, mBuilder.build());*/
                }
            }
        };
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyChannelName";
            String description = "MyChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("DateNotificationChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
