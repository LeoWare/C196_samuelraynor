/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.samuelraynor.app.c196_samuelraynor.feature.R;
import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Assessment;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DateJobService extends JobService {
    private static final String TAG = "DateNotificationService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "DateJobService::onStartJob()");

        //Intent service = new Intent(getApplicationContext(), DateNotificationService.class);
        //getApplicationContext().startService(service);

        // Create notification channel
        createNotificationChannel();

        // Get the service task
        Runnable r = getServiceRunnable();
        // Start the thread and do our work.
        Thread serviceThread = new Thread(r);
        serviceThread.start();

        // reschedule ourselves
        Helper.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "DateJobService::onStopJob()");
        return true;
    }

    private Runnable getServiceRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "WGU JobService Started", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Running JobService Runnable");
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
