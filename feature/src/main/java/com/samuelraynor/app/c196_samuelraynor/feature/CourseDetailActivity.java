package com.samuelraynor.app.c196_samuelraynor.feature;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.samuelraynor.app.c196_samuelraynor.feature.database.StudentData;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Assessment;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Mentor;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CourseDetailActivity extends AppCompatActivity {

    private static final int COMMAND_NEW = 0;
    private static final int COMMAND_EDIT = 1;

    //ListView courseList;
    ListView mentorsList;
    ListView notesList;
    ListView examsList;
    //ArrayList<Course> courseInfo = new ArrayList<>();
    protected Course selectedCourse;

    protected StudentData studentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_activity_course_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // student data
        this.studentData = new StudentData(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int mItem = item.getItemId();
        if (mItem == R.id.mEdit) {
            edit_course();
            return true;
        } else if (mItem == android.R.id.home) {
            Intent upIntent = getSupportParentActivityIntent();

            upIntent.putExtra("ACTION", "INFO");
            upIntent.putExtra("TERM", selectedCourse.getTermId());
            setResult(RESULT_OK, upIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void edit_course() {
        Intent editIntent = new Intent(this, CourseEditActivity.class);
        editIntent.putExtra("ACTION", "INFO");
        editIntent.putExtra("COURSE", selectedCourse.getId());
        startActivityForResult(editIntent, 1);

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

    protected void showMentors() {
        ListView lv = findViewById(R.id.mentorsListView);

        MentorsAdapter mentorsAdapter = new MentorsAdapter(this, studentData.getMentorsByCourseId(selectedCourse.getId()));
        lv.setAdapter(mentorsAdapter);
    }

    private void showNotes() {
        ListView lv = findViewById(R.id.notesListView);
        NotesAdapter notesAdapter = new NotesAdapter(this, studentData.getNotesByCourseId(selectedCourse.getId()));
        lv.setAdapter(notesAdapter);
    }

    private void showExams() {
        ListView lv = findViewById(R.id.examsListView);
        ExamsAdapter examsAdapter = new ExamsAdapter(this, studentData.getAssessmentsByCourseId(selectedCourse.getId()));
        lv.setAdapter(examsAdapter);
    }


    private void refresh() {
        showTitle(selectedCourse.getName());
        showStartDate(selectedCourse.getStart());
        showEndDate(selectedCourse.getEnd());
        showStatus(selectedCourse.getStatus());
        showMentors();
        showNotes();
        showExams();
    }

    /**
     * Show a dialog box to add a mentor to the current course.
     *
     * @param view
     */
    protected void cmdAddMentor(View view) {
        Mentor mentor = new Mentor();
        mentor.setCourseId(selectedCourse.getId());

        MentorDialog dialog = new MentorDialog(this, mentor, COMMAND_NEW);
        dialog.show();
    }

    /**
     * Show a dialog box to add a note to the current course
     *
     * @param view
     */
    protected void cmdAddNote(View view) {
        Note note = new Note();
        note.setCourseId(selectedCourse.getId());

        NoteDialog dialog = new NoteDialog(this, note, COMMAND_NEW);
        dialog.show();
    }

    /**
     * Show a dialog box to add an assessment to the current course
     *
     * @param view
     */
    protected void cmdAddExam(View view) {
        Assessment assessment = new Assessment();
        assessment.setCourseId(selectedCourse.getId());

        ExamDialog dialog = new ExamDialog(this, assessment, COMMAND_NEW);
        dialog.show();
    }

    /**
     * This is called when the Edit activity finishes.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            this.selectedCourse = (Course) data.getSerializableExtra("COURSE");
            refresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected class CourseAdapter extends ArrayAdapter<Course> {
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
        Context context;

        public MentorsAdapter(Context context, ArrayList<Mentor> mentors) {
            super(context, 0, mentors);
            this.context = context;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Mentor mentor = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mentor, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);

            tvName.setText(mentor.getName());
            tvPhone.setText(getString(R.string.phoneLabel).concat(" ").concat(mentor.getPhone()));
            tvEmail.setText(getString(R.string.emailLabel).concat(" ").concat(mentor.getEmail()));

            ImageButton edit = convertView.findViewById(R.id.btnEdit);
            ImageButton delete = convertView.findViewById(R.id.btnDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "EDIT Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the mentor id from the parent view
                    long mentorId = Long.valueOf((Long) v.getTag());
                    StudentData sd = new StudentData(v.getContext());

                    MentorDialog dialog = new MentorDialog((Activity) v.getContext(), studentData.getMentor(mentorId), COMMAND_EDIT);
                    dialog.show();

                    // refresh listview
                    showMentors();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "DELETE Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the mentor id from the parent view
                    long mentorId = Long.valueOf((Long) v.getTag());
                    StudentData sd = new StudentData(v.getContext());

                    sd.deleteMentor(mentorId);

                    // refresh listview
                    showMentors();
                }
            });

            edit.setTag(new Long(mentor.getId()));
            delete.setTag(new Long(mentor.getId()));

            return convertView;
        }
    }

    protected class NotesAdapter extends ArrayAdapter<Note> {
        public NotesAdapter(Context context, ArrayList<Note> notes) {
            super(context, 0, notes);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Note note = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
            }

            TextView tvNote = (TextView) convertView.findViewById(R.id.tvNote);
            tvNote.setText(note.getNote());

            ImageButton edit = convertView.findViewById(R.id.btnEdit);
            ImageButton delete = convertView.findViewById(R.id.btnDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "EDIT Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the note id from the parent view
                    long noteId = Long.valueOf((Long) v.getTag());

                    NoteDialog dialog = new NoteDialog((Activity) v.getContext(), studentData.getNote(noteId), COMMAND_EDIT);
                    dialog.show();

                    // refresh listview
                    showNotes();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "DELETE Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the mentor id from the parent view
                    long noteId = Long.valueOf((Long) v.getTag());
                    StudentData sd = new StudentData(v.getContext());

                    sd.deleteNote(noteId);

                    // refresh listview
                    showNotes();
                }
            });

            edit.setTag(new Long(note.getId()));
            delete.setTag(new Long(note.getId()));

            return convertView;
        }
    }

    protected class ExamsAdapter extends ArrayAdapter<Assessment> {
        public ExamsAdapter(Context context, ArrayList<Assessment> assessments) {
            super(context, 0, assessments);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Assessment assessment = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_assessment, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.textTitle);
            TextView tvType = (TextView) convertView.findViewById(R.id.textType);
            TextView tvDueDate = (TextView) convertView.findViewById(R.id.textDueDate);

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            tvTitle.setText(assessment.getTitle());
            tvType.setText("Type: ".concat(assessment.getType()));
            tvDueDate.setText("Due Date: ".concat(formatter.format(assessment.getDueDate())));

            ImageButton edit = convertView.findViewById(R.id.btnEdit);
            ImageButton delete = convertView.findViewById(R.id.btnDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "EDIT Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the note id from the parent view
                    long examId = Long.valueOf((Long) v.getTag());

                    ExamDialog dialog = new ExamDialog((Activity) v.getContext(), studentData.getAssessment(examId), COMMAND_EDIT);
                    dialog.show();

                    // refresh listview
                    showExams();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, "DELETE Mentor::id(" + String.valueOf(v.getTag()) + ")", Snackbar.LENGTH_SHORT).show();

                    // get the mentor id from the parent view
                    long examId = Long.valueOf((Long) v.getTag());
                    StudentData sd = new StudentData(v.getContext());

                    sd.deleteAssessment(examId);

                    // refresh listview
                    showExams();
                }
            });

            edit.setTag(new Long(assessment.getId()));
            delete.setTag(new Long(assessment.getId()));

            return convertView;
        }
    }

    protected class MentorDialog extends Dialog implements View.OnClickListener {
        public Activity parentActivity;
        private long courseId;
        private int command;
        private Mentor mentor;

        public MentorDialog(Activity activity, Mentor mentor, int command) {
            // call the parent classes constructor
            super(activity);

            // save the activity
            this.parentActivity = activity;
            this.mentor = mentor;
            this.courseId = mentor.getCourseId();
            this.command = command;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_mentor);
            Button save = (Button) findViewById(R.id.btnSave);
            Button cancel = (Button) findViewById(R.id.btnCancel);
            save.setOnClickListener(this);
            cancel.setOnClickListener(this);

            if (this.command == COMMAND_EDIT) {
                // populate the fields
                TextInputEditText name = findViewById(R.id.etMentorName);
                TextInputEditText email = findViewById(R.id.etMentorEmail);
                TextInputEditText phone = findViewById(R.id.etMentorPhone);

                name.setText(this.mentor.getName());
                email.setText(this.mentor.getEmail());
                phone.setText(this.mentor.getPhone());
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btnCancel) {
                handleCancel(v);
            } else if (id == R.id.btnSave) {
                handleSave(v);

            }
        }

        protected void handleCancel(View view) {
            showMentors();
            dismiss();
        }

        protected void handleSave(View view) {
            StudentData studentData = new StudentData(this.parentActivity);
            // save the record and dismiss
            //Mentor newItem = new Mentor();

            //newItem.setCourseId(this.courseId);
            mentor.setName(((TextInputEditText) findViewById(R.id.etMentorName)).getText().toString());
            mentor.setEmail(((TextInputEditText) findViewById(R.id.etMentorEmail)).getText().toString());
            mentor.setPhone(((TextInputEditText) findViewById(R.id.etMentorPhone)).getText().toString());

            if (this.command == COMMAND_NEW) {
                studentData.addMentor(mentor);
            }
            if (this.command == COMMAND_EDIT) {
                studentData.updateMentor(mentor);
            }
            showMentors();
            dismiss();
        }
    }

    protected class NoteDialog extends Dialog implements View.OnClickListener {
        public Activity parentActivity;
        private long courseId;
        private int command;
        private Note note;

        public NoteDialog(Activity activity, Note note, int command) {
            // call the parent classes constructor
            super(activity);

            // save the activity
            this.parentActivity = activity;
            this.note = note;
            this.courseId = note.getCourseId();
            this.command = command;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_note);
            Button save = (Button) findViewById(R.id.btnSave);
            Button cancel = (Button) findViewById(R.id.btnCancel);
            save.setOnClickListener(this);
            cancel.setOnClickListener(this);

            if (this.command == COMMAND_EDIT) {
                // populate the fields
                EditText note = findViewById(R.id.etNote);

                note.setText(this.note.getNote());
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btnCancel) {
                handleCancel(v);
            } else if (id == R.id.btnSave) {
                handleSave(v);

            }
        }

        protected void handleCancel(View view) {
            showNotes();
            dismiss();
        }

        protected void handleSave(View view) {
            StudentData studentData = new StudentData(this.parentActivity);
            // save the record and dismiss
            //Mentor newItem = new Mentor();

            note.setCourseId(this.courseId);
            note.setNote(((EditText) findViewById(R.id.etNote)).getText().toString());

            if (this.command == COMMAND_NEW) {
                studentData.addNote(note);
            }
            if (this.command == COMMAND_EDIT) {
                studentData.updateNote(note);
            }
            showNotes();
            dismiss();
        }
    }

    protected class ExamDialog extends Dialog implements View.OnClickListener {
        public Activity parentActivity;
        private long courseId;
        private int command;
        private Assessment assessment;
        private Date pickerDate;

        public ExamDialog(Activity activity, Assessment assessment, int command) {
            // call the parent classes constructor
            super(activity);

            // save the activity
            this.parentActivity = activity;
            this.assessment = assessment;
            this.courseId = assessment.getCourseId();
            this.command = command;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_assessment);
            Button save = (Button) findViewById(R.id.btnSave);
            Button cancel = (Button) findViewById(R.id.btnCancel);
            save.setOnClickListener(this);
            cancel.setOnClickListener(this);

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            // Populate the spinner
            Spinner type = findViewById(R.id.sType);
            ArrayList<String> typeStrings = new ArrayList<>();
            typeStrings.add(Assessment.OBJECTIVE);
            typeStrings.add(Assessment.PERFORMANCE);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this.parentActivity, android.R.layout.simple_list_item_1, typeStrings);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(typeAdapter);

            int spinnerPosition = typeAdapter.getPosition(assessment.getType());
            type.setSelection(spinnerPosition);


            // populate the fields
            TextView title = findViewById(R.id.etTitle);
            TextView due = findViewById(R.id.etDueDate);
            title.setText(this.assessment.getTitle());
            due.setText(formatter.format(assessment.getDueDate()));
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btnCancel) {
                handleCancel(v);
            } else if (id == R.id.btnSave) {
                handleSave(v);
            }
        }

        protected void handleCancel(View view) {
            showNotes();
            dismiss();
        }

        protected void handleSave(View view) {
            StudentData studentData = new StudentData(this.parentActivity);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            // save the record and dismiss

            assessment.setCourseId(this.courseId);
            assessment.setTitle(((TextInputEditText) findViewById(R.id.etTitle)).getText().toString());
            assessment.setType(((Spinner) findViewById(R.id.sType)).getSelectedItem().toString());
            try {
                TextView tv = (TextView) findViewById(R.id.etDueDate);
                assessment.setDueDate(formatter.parse(tv.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (this.command == COMMAND_NEW) {
                studentData.addAssessment(assessment);
            }
            if (this.command == COMMAND_EDIT) {
                studentData.updateAssessment(assessment);
            }
            showExams();
            dismiss();
        }
    }
}
