/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CourseEditActivity extends AppCompatActivity {

    private String LOGTAG = "MYDEBUG";
    private StudentData studentData;
    private String action;
    private long courseId;
    private Course selectedCourse;
    private long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_course_edit_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // get a database
        this.studentData = new StudentData(this);

        // Get the Term object that was passed with putExtra()
        this.action = (String) getIntent().getStringExtra("ACTION");
        Intent callingIntent = getIntent();

        if (this.action.equals("INFO")) {
            // Get the selected course id
            courseId = callingIntent.getLongExtra("COURSE", 0);
            if (courseId == 0) throw new RuntimeException("No course passed in EXTRA!");

            // Get the term info from the database
            StudentData studentData = new StudentData(this);

            selectedCourse = studentData.getCourse(courseId);
        } else if (this.action.equals("NEW")) {
            Date today = new Date();
            selectedCourse = new Course("New Course", today, today, Course.PLAN_TO_TAKE);

            this.termId = callingIntent.getLongExtra("TERM", 0);
            if(this.termId == 0) throw new RuntimeException("No termId passed in EXTRA!");

            selectedCourse.setTermId(this.termId);
        }

        // Fill in the values on the form
        showTitle(selectedCourse.getName());
        showStartDate(selectedCourse.getStart());
        showEndDate(selectedCourse.getEnd());

        // Populate the spinner
        Spinner sStatus = findViewById(R.id.sStatus);
        ArrayList<String> statusStrings = new ArrayList<>();
        statusStrings.add(Course.PLAN_TO_TAKE);
        statusStrings.add(Course.IN_PROGRESS);
        statusStrings.add(Course.DROPPED);
        statusStrings.add(Course.COMPLETED);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusStrings);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sStatus.setAdapter(statusAdapter);

        if (selectedCourse.getStatus() != null) {
            int spinnerPosition = statusAdapter.getPosition(selectedCourse.getStatus());
            sStatus.setSelection(spinnerPosition);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int mItem = item.getItemId();
        if (mItem == R.id.menuCancel) {
            handleCancel();
        } else if (mItem == R.id.menuSave) {
            handleSave();
        } else if (mItem == R.id.menuDelete) {
            handleDelete();
        }
        return false;
    }

    public void setStartDate(View view) {
        showDialog(999);
    }

    public void setEndDate(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            SimpleDateFormat formatter = new SimpleDateFormat();
            formatter.applyPattern("yyyy");
            int startYear = Integer.valueOf(formatter.format(selectedCourse.getStart()));
            formatter.applyPattern("MM");
            int startMonth = Integer.valueOf(formatter.format(selectedCourse.getStart())) - 1;
            formatter.applyPattern("dd");
            int startDay = Integer.valueOf(formatter.format(selectedCourse.getStart()));

            return new DatePickerDialog(this,
                    myStartDateListener, startYear, startMonth, startDay);
        }
        if (id == 998) {
            SimpleDateFormat formatter = new SimpleDateFormat();
            formatter.applyPattern("yyyy");
            int endYear = Integer.valueOf(formatter.format(selectedCourse.getStart()));
            formatter.applyPattern("MM");
            int endMonth = Integer.valueOf(formatter.format(selectedCourse.getStart())) - 1;
            formatter.applyPattern("dd");
            int endDay = Integer.valueOf(formatter.format(selectedCourse.getStart()));

            return new DatePickerDialog(this,
                    myEndDateListener, endYear, endMonth, endDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = null;
                    try {
                        date = formatter.parse(String.format("%2d/%2d/%4d", arg2 + 1, arg3, arg1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    showStartDate(date);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = null;
                    try {
                        date = formatter.parse(String.format("%2d/%2d/%4d", arg2 + 1, arg3, arg1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    showEndDate(date);
                }
            };

    private void showStartDate(Date start) {
        TextView tv = (TextView) findViewById(R.id.tvStartDate);
        showDate(tv, start);

    }

    private void showEndDate(Date end) {
        TextView tv = (TextView) findViewById(R.id.tvEndDate);
        showDate(tv, end);
    }

    private void showDate(TextView tv, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        tv.setText(formatter.format(date));
    }

    private void showTitle(String title) {
        TextView tv;
        tv = findViewById(R.id.tvTitle);
        tv.setText(title);
    }

    protected void handleCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void handleDelete() {
        try {
            studentData.deleteCourse(selectedCourse.getId());
        } catch (Exception e) {
            e.printStackTrace();

            this.handleCancel();
        }
        setResult(RESULT_FIRST_USER);
        finish();
    }


    protected void handleSave() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Course selectedCourse = this.selectedCourse;
        if (this.action.equals("NEW")) {


            TextView _tvStartDate = findViewById(R.id.tvStartDate);
            TextView _tvEndDate = findViewById(R.id.tvEndDate);
            String _name = (((TextInputEditText) findViewById(R.id.tvTitle)).getText()).toString();
            String _status = ((Spinner) findViewById(R.id.sStatus)).getSelectedItem().toString();
            try {
                selectedCourse = new Course();
                selectedCourse.setId(termId);
                selectedCourse.setName(_name);
                selectedCourse.setStart(formatter.parse((String) _tvStartDate.getText()));
                selectedCourse.setEnd(formatter.parse((String) _tvEndDate.getText()));
                selectedCourse.setStatus(_status);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.studentData.addCourse(selectedCourse);

        } else if (this.action.equals("INFO")) {

            try {
                selectedCourse.setName((((TextInputEditText) findViewById(R.id.tvTitle)).getText()).toString());
                TextView tvStartDate = findViewById(R.id.tvStartDate);
                selectedCourse.setStart(formatter.parse((String) tvStartDate.getText()));
                TextView tvEndDate = findViewById(R.id.tvEndDate);
                selectedCourse.setEnd(formatter.parse((String) tvEndDate.getText()));
                selectedCourse.setStatus(((Spinner) findViewById(R.id.sStatus)).getSelectedItem().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.studentData.updateCourse(selectedCourse);
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("COURSE", selectedCourse);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
