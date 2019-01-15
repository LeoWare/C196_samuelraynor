package com.samuelraynor.app.c196_samuelraynor.feature;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.samuelraynor.app.c196_samuelraynor.feature.service.DateNotificationService;
import com.samuelraynor.app.c196_samuelraynor.feature.service.Helper;

public class MainActivity extends AppCompatActivity {

    // last 23 chars of package
    private static String TAG = "96_samuelraynor.feature";

    Intent serviceIntent;
    private DateNotificationService dateService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Helper.scheduleJob(this);
        } else {
            try {
                this.dateService = new DateNotificationService(this);
                this.serviceIntent = new Intent(this, dateService.getClass());
                if (!isMyServiceRunning(dateService.getClass())) {
                    startService(this.serviceIntent);
                }
            } catch (RuntimeException e) {
                Log.i(TAG, e.getMessage());
                Toast.makeText(this, "DateNotificationService Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(this.serviceIntent);
        super.onDestroy();
    }

    public void showTermsActivity(View v) {
        Intent myIntent = new Intent(this, TermListActivity.class);
        startActivity(myIntent);
    }

    public void startService(View v) {
        // start the DateNotificationService
        startService(new Intent(this, DateNotificationService.class));
        /*Calendar cal = Calendar.getInstance();
        AlarmManager am=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(this, DateNotificationService.class);
        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), (1000 * 15 ), pi);*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

}
