/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Autostart extends BroadcastReceiver {
    private static final String TAG = ".service.Autostart";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Restarting WGU Service");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Helper.scheduleJob(context);
        } else {
            try {
                Intent serviceIntent = new Intent(context, DateNotificationService.class);
                context.startService(serviceIntent);
            } catch (RuntimeException e) {
                Log.i(TAG, e.getMessage());
                Toast.makeText(context, "DateNotificationService Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
