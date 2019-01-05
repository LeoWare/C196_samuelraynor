package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class TermDetailActivity extends AppCompatActivity {

    private final static int REQUESTCODE_TERM = 1;
    private final static int REQUESTCODE_COURSE = 2;

    private final String LOGTAG = "MYDEBUG";

    protected Term selectedTerm;
    protected long termId;
    protected ListView courseList;

    protected ArrayList<Course> courseInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Get the selected term id
        Intent callingIntent = getIntent();
        termId = callingIntent.getLongExtra("TERM", 0);
        if (termId == 0) throw new RuntimeException("No term passed in EXTRA!");

        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_activity_term_details);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTermAddCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the term info from the database
        StudentData studentData = new StudentData(this);
        selectedTerm = studentData.getTerm(termId);

        // show the term info
        showTitle(selectedTerm.getTitle());
        showStartDate(selectedTerm.getStart());
        showEndDate(selectedTerm.getEnd());


        // Set the ListView content
        courseList = findViewById(R.id.courseListView);

        // get the courseinfo from the database
        courseInfo = studentData.getCoursesByTermId(this.selectedTerm.getId());
        TermDetailActivity.CourseAdapter courseListAdapter = new TermDetailActivity.CourseAdapter(this, courseInfo);

        courseList.setAdapter(courseListAdapter);
        courseList.setOnItemClickListener(getOnItemClickListener());

    }


    public void updateCourseInfoList() {
        // get the courseinfo from the database
        StudentData studentData = new StudentData(this);
        ArrayList<Course> courseInfo = studentData.getCoursesByTermId(this.selectedTerm.getId());

        TermDetailActivity.CourseAdapter courseListAdapter = new TermDetailActivity.CourseAdapter(this, courseInfo);
        courseList.setAdapter(courseListAdapter);
        courseList.setOnItemClickListener(getOnItemClickListener());
    }

    public void addCourse(View view) {
        Intent addIntent = new Intent(this, CourseEditActivity.class);
        addIntent.putExtra("ACTION", "NEW");
        addIntent.putExtra("TERM", selectedTerm.getId());
        startActivityForResult(addIntent, REQUESTCODE_COURSE);
    }

    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selectedCourse = (Course) courseList.getItemAtPosition(position);
                Intent courseDetailIntent = new Intent(parent.getContext(), CourseDetailActivity.class);
                courseDetailIntent.putExtra("COURSE", selectedCourse);
                startActivityForResult(courseDetailIntent, REQUESTCODE_COURSE);
            }
        };
    }

    private void showStartDate(Date start) {
        TextView tv = (TextView) findViewById(R.id.textStartDate);
        showDate(tv, start);

    }

    private void showEndDate(Date end) {
        TextView tv = (TextView) findViewById(R.id.textEndDate);
        showDate(tv, end);
    }

    private void showDate(TextView tv, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        tv.setText(formatter.format(date));
    }

    private void showTitle(String title) {
        TextView tv;
        tv = findViewById(R.id.textTermTitle);
        tv.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUESTCODE_TERM) {
            if (resultCode == RESULT_OK) {
                // user clicked save
                Term savedTerm = (Term) data.getSerializableExtra("TERM");
                selectedTerm = savedTerm;
                showTitle(savedTerm.getTitle());
                showStartDate(savedTerm.getStart());
                showEndDate(savedTerm.getEnd());
            } else if (resultCode == RESULT_CANCELED) {
                // do nothing
            } else if (resultCode == RESULT_FIRST_USER) {
                // user deleted the record
                // finish the activity
                setResult(resultCode);
                finish();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
            updateCourseInfoList();
        } else if (requestCode == REQUESTCODE_COURSE) {
            if (resultCode == RESULT_OK) {
                updateCourseInfoList();
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term_details, menu);
        return true;
    }

    protected void edit_term() {
        Intent editIntent = new Intent(this, TermEditActivity.class);
        editIntent.putExtra("ACTION", "INFO");
        editIntent.putExtra("TERM", selectedTerm.getId());
        startActivityForResult(editIntent, 1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int mItem = item.getItemId();
        if (mItem == R.id.mEdit) {
            edit_term();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        Log.d(LOGTAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(LOGTAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LOGTAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOGTAG, "onStop");
        super.onStop();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.termId = savedInstanceState.getLong("termId");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("termId", selectedTerm.getId());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
