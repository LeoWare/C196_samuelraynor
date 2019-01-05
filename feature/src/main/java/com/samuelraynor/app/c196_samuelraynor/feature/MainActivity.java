package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.samuelraynor.app.c196_samuelraynor.feature.service.DateNotificationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_main);
    }

    public void showTermsActivity(View v) {
        Intent myIntent = new Intent(this, TermListActivity.class);
        startActivity(myIntent);
    }

    public void startService(View v) {
        // start the DateNotificationService
        startService(new Intent(this, DateNotificationService.class));
    }

}
