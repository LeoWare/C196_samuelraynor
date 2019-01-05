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
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.samuelraynor.app.c196_samuelraynor.feature.R;


/**
 * Check the database for exam due dates and course
 * end dates. Send a notification if there is an
 * exam due or course ending within 7 days from now.
 */
public class DateNotificationService extends Service {

    private static final String TAG = "DateNotificationService";

    public DateNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        createNotificationChannel(); // for API 26+
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy called.");
        Toast.makeText(this, "WGU Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "WGU Service Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onStartCommand called.");


        Runnable r = new Runnable() {
            @Override
            public void run() {
                // Check the database for exam due dates and course
                // end dates. Send a notification if there is an
                // exam due or course ending within 7 days from now.


                // Found items coming due?


                // Yes: Display a notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), "DateNotificationChannel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification Title")
                        .setContentText("Test Notification")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, mBuilder.build());
            }
        };

        Thread serviceThread = new Thread(r);
        serviceThread.start();

        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
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
