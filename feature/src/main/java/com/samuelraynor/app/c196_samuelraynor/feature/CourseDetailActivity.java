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
import com.samuelraynor.app.c196_samuelraynor.feature.model.Mentor;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CourseDetailActivity extends AppCompatActivity {

    //ListView courseList;
    ListView mentorsList;
    ListView notesList;
    ListView examsList;
    //ArrayList<Course> courseInfo = new ArrayList<>();
    protected Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitle(R.string.title_activity_course_detail);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the ListView content

        mentorsList = findViewById(R.id.mentorsListView);
        notesList = findViewById(R.id.notesListView);
        examsList = findViewById(R.id.examsListView);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


        //CourseDetailActivity.CourseAdapter courseListAdapter = new CourseDetailActivity.CourseAdapter(this, courseInfo);

        //courseList.setAdapter(courseListAdapter);
        /*courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                *//*Term selectedCourse = (Course) courseList.getItemAtPosition(position);
                Intent termDetailIntent = new Intent(parent.getContext(), CourseDetailActivity.class);
                termDetailIntent.putExtra("Course", selectedCourse);
                startActivityForResult(termDetailIntent, 1);*//*
            }
        });*/

        selectedCourse = (Course) getIntent().getSerializableExtra("COURSE");

        showTitle(selectedCourse.getName());
        showStartDate(selectedCourse.getStart());
        showEndDate(selectedCourse.getEnd());
        showStatus(selectedCourse.getStatus());
        showMentors();
        showNotes();
        showExams();
    }


    private void showTitle(String title) {
        TextView tv = findViewById(R.id.textTitle);
        tv.setText(title);
    }

    private void showStartDate(Date date) {
        TextView tv = findViewById(R.id.textStartDate);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        tv.setText(formatter.format(date));
    }

    private void showEndDate(Date date) {
        TextView tv = findViewById(R.id.textEndDate);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        tv.setText(formatter.format(date));
    }

    private void showStatus(String status) {
        TextView tv = findViewById(R.id.textStatus);
        tv.setText(status);
    }

    private void showMentors() {
        ListView lv = findViewById(R.id.mentorsListView);

        MentorsAdapter mentorsAdapter = new MentorsAdapter(this, selectedCourse.getMentorArrayList());
        lv.setAdapter(mentorsAdapter);
    }

    private void showNotes() {
        ListView lv = findViewById(R.id.notesListView);
        NotesAdapter notesAdapter = new NotesAdapter(this, selectedCourse.getNotesArrayList());
        lv.setAdapter(notesAdapter);
    }

    private void showExams() {
        //ListView lv = findViewById(R.id.examsListView);
        //ExamsAdapter mentorsAdapter = new ExamsAdapter(this, selectedCourse.getAssessmentArrayList());
        //lv.setAdapter(mentorsAdapter);
    }


    protected void cmdAddMentor(View view) {

    }

    protected void cmdAddNote(View view) {

    }

    protected void cmdAddExam(View view) {

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

    protected class MentorsAdapter extends ArrayAdapter<Mentor> {
        public MentorsAdapter(Context context, ArrayList<Mentor> mentors) {
            super(context, 0, mentors);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Mentor mentor = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mentor, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);

            //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            tvName.setText(mentor.getName());
            tvPhone.setText(getString(R.string.phoneLabel).concat(mentor.getPhone()));
            tvEmail.setText(getString(R.string.emailLabel).concat(mentor.getEmail()));

            return convertView;
        }
    }

    protected class NotesAdapter extends ArrayAdapter<Note> {
        public NotesAdapter(Context context, ArrayList<Note> mentors) {
            super(context, 0, mentors);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Note note = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView tvNote = (TextView) convertView.findViewById(android.R.id.text1);


            //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            tvNote.setText(note.getNote());


            return convertView;
        }
    }

    protected class ExamsAdapter extends ArrayAdapter<Assessment> {
        public ExamsAdapter(Context context, ArrayList<Assessment> mentors) {
            super(context, 0, mentors);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Assessment assessment = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mentor, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvStart = (TextView) convertView.findViewById(R.id.tvStart);
            TextView tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            tvName.setText(assessment.getTitle());
            tvStart.setText("Type: ".concat(assessment.getType()));
            tvEnd.setText("Due Date: ".concat(formatter.format(assessment.getDueDate())));

            return convertView;
        }
    }
}
