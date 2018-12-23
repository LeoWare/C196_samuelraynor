package com.samuelraynor.app.c196_samuelraynor.feature;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TermEditActivity extends AppCompatActivity {

    protected String action;
    protected long termId;

    private StudentData studentData;
    protected Term selectedTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_activity_term_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // get a database
        this.studentData = new StudentData(this);

        // Get the Term object that was passed with putExtra()
        this.action = (String) getIntent().getStringExtra("ACTION");

        if (this.action.equals("INFO")) {
            // Get the selected term id
            Intent callingIntent = getIntent();
            termId = callingIntent.getLongExtra("TERM", 0);
            if (termId == 0) throw new RuntimeException("No term passed in EXTRA!");

            // Get the term info from the database
            StudentData studentData = new StudentData(this);
            selectedTerm = studentData.getTerm(termId);


        } else if (this.action.equals("NEW")) {
            Date today = new Date();
            selectedTerm = new Term("New Term", today, today);
        }

        showTitle(selectedTerm.getTitle());
        showStartDate(selectedTerm.getStart());
        showEndDate(selectedTerm.getEnd());
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
        // TODO Auto-generated method stub
        if (id == 999) {
            SimpleDateFormat formatter = new SimpleDateFormat();
            formatter.applyPattern("yyyy");
            int startYear = Integer.valueOf(formatter.format(selectedTerm.getStart()));
            formatter.applyPattern("MM");
            int startMonth = Integer.valueOf(formatter.format(selectedTerm.getStart())) - 1;
            formatter.applyPattern("dd");
            int startDay = Integer.valueOf(formatter.format(selectedTerm.getStart()));

            return new DatePickerDialog(this,
                    myStartDateListener, startYear, startMonth, startDay);
        }
        if (id == 998) {
            SimpleDateFormat formatter = new SimpleDateFormat();
            formatter.applyPattern("yyyy");
            int endYear = Integer.valueOf(formatter.format(selectedTerm.getStart()));
            formatter.applyPattern("MM");
            int endMonth = Integer.valueOf(formatter.format(selectedTerm.getStart())) - 1;
            formatter.applyPattern("dd");
            int endDay = Integer.valueOf(formatter.format(selectedTerm.getStart()));

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
        tv = findViewById(R.id.tvTermTitle);
        tv.setText(title);
    }

    protected void handleCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void handleDelete() {
        try {
            studentData.deleteTerm(selectedTerm.getId());
        } catch (StudentData.TermHasCoursesException e) {
            e.printStackTrace();
        }
        setResult(RESULT_FIRST_USER);
        finish();
    }


    protected void handleSave() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Term selectedTerm = this.selectedTerm;
        if (this.action.equals("NEW")) {


            TextView _tvStartDate = findViewById(R.id.tvStartDate);
            TextView _tvEndDate = findViewById(R.id.tvEndDate);
            String _name = (((TextInputEditText) findViewById(R.id.tvTermTitle)).getText()).toString();
            try {
                selectedTerm = new Term(_name, formatter.parse((String) _tvStartDate.getText()), formatter.parse((String) _tvEndDate.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.studentData.addTerm(selectedTerm);

        } else if (this.action.equals("INFO")) {

            try {
                selectedTerm.setTitle((((TextInputEditText) findViewById(R.id.tvTermTitle)).getText()).toString());
                TextView tvStartDate = findViewById(R.id.tvStartDate);
                selectedTerm.setStart(formatter.parse((String) tvStartDate.getText()));
                TextView tvEndDate = findViewById(R.id.tvEndDate);
                selectedTerm.setEnd(formatter.parse((String) tvEndDate.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.studentData.updateTerm(selectedTerm);
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("TERM", selectedTerm);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
