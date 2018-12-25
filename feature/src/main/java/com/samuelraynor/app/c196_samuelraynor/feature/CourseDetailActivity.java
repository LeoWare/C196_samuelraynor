package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CourseDetailActivity extends AppCompatActivity {

    ListView courseList;

    ArrayList<Course> courseInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitle(R.string.title_activity_course_detail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the ListView content
        courseList = findViewById(R.id.examsListView);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);


        CourseDetailActivity.CourseAdapter courseListAdapter = new CourseDetailActivity.CourseAdapter(this, courseInfo);

        courseList.setAdapter(courseListAdapter);
        /*courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                *//*Term selectedCourse = (Course) courseList.getItemAtPosition(position);
                Intent termDetailIntent = new Intent(parent.getContext(), CourseDetailActivity.class);
                termDetailIntent.putExtra("Course", selectedCourse);
                startActivityForResult(termDetailIntent, 1);*//*
            }
        });*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class CourseAdapter extends ArrayAdapter<Course> {
        public CourseAdapter(Context context, ArrayList<Course> courses) {
            super(context, 0, courses);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Course course = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvStart = (TextView) convertView.findViewById(R.id.tvStart);
            TextView tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            tvName.setText(course.getName());
            tvStart.setText("Start: ".concat(formatter.format(course.getStart())));
            tvEnd.setText("End: ".concat(formatter.format(course.getEnd())));

            return convertView;
        }
    }
}
