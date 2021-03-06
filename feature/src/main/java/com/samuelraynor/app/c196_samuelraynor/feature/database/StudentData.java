/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.samuelraynor.app.c196_samuelraynor.feature.model.Assessment;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Mentor;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Note;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class StudentData extends SQLiteOpenHelper {
    public static final String ALARM_COURSE_START = CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START;
    public static final String ALARM_COURSE_END = CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END;
    public static final String ALARM_ASSESSMENT_DUE = AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE;

    private static final String TAG = ".feature.database";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "StudentData.db";

    private static final String SQL_CREATE_TERMS =
            "CREATE TABLE " + TermsContract.TermsEntry.TABLE_NAME + " (" +
                    TermsContract.TermsEntry._ID + " INTEGER PRIMARY KEY," +
                    TermsContract.TermsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TermsContract.TermsEntry.COLUMN_NAME_START + " DATE," +
                    TermsContract.TermsEntry.COLUMN_NAME_END + " DATE)";

    private static final String SQL_DELETE_TERMS =
            "DROP TABLE IF EXISTS " + TermsContract.TermsEntry.TABLE_NAME;

    private static final String SQL_CREATE_COURSES =
            "CREATE TABLE " + CoursesContract.CoursesEntry.TABLE_NAME + " (" +
                    CoursesContract.CoursesEntry._ID + " INTEGER PRIMARY KEY," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID + " INTEGER," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_TITLE + " TEXT," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_START + " DATE," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_END + " DATE," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_STATUS + " TEXT," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START + " INTEGER," +
                    CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END + " INTEGER)";

    private static final String SQL_DELETE_COURSES =
            "DROP TABLE IF EXISTS " + CoursesContract.CoursesEntry.TABLE_NAME;

    private static final String SQL_CREATE_MENTORS =
            "CREATE TABLE " + MentorsContract.MentorsEntry.TABLE_NAME + " (" +
                    MentorsContract.MentorsEntry._ID + " INTEGER PRIMARY KEY," +
                    MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
                    MentorsContract.MentorsEntry.COLUMN_NAME_NAME + " TEXT," +
                    MentorsContract.MentorsEntry.COLUMN_NAME_PHONE + " TEXT," +
                    MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL + " TEXT)";

    private static final String SQL_DELETE_MENTORS =
            "DROP TABLE IF EXISTS " + MentorsContract.MentorsEntry.TABLE_NAME;

    private static final String SQL_CREATE_NOTES =
            "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + " (" +
                    NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY," +
                    NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
                    NotesContract.NotesEntry.COLUMN_NAME_NOTE + " TEXT)";

    private static final String SQL_DELETE_NOTES =
            "DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME;

    private static final String SQL_CREATE_ASSESSMENTS =
            "CREATE TABLE " + AssessmentsContract.AssessmentsEntry.TABLE_NAME + " (" +
                    AssessmentsContract.AssessmentsEntry._ID + " INTEGER PRIMARY KEY," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE + " DATE," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE + " DATE," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE + " INTEGER)";

    private static final String SQL_DELETE_ASSESSMENTS =
            "DROP TABLE IF EXISTS " + AssessmentsContract.AssessmentsEntry.TABLE_NAME;

    public StudentData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "database: onCreate");
        db.execSQL(SQL_CREATE_TERMS);
        db.execSQL(SQL_CREATE_COURSES);
        db.execSQL(SQL_CREATE_MENTORS);
        db.execSQL(SQL_CREATE_NOTES);
        db.execSQL(SQL_CREATE_ASSESSMENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "database: onUpgrade");
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TERMS);
        db.execSQL(SQL_DELETE_COURSES);
        db.execSQL(SQL_DELETE_MENTORS);
        db.execSQL(SQL_DELETE_NOTES);
        db.execSQL(SQL_DELETE_ASSESSMENTS);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "database: onDowngrade");
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Term> getAllTerms() {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        String[] columns = {
                TermsContract.TermsEntry._ID,
                TermsContract.TermsEntry.COLUMN_NAME_TITLE,
                TermsContract.TermsEntry.COLUMN_NAME_START,
                TermsContract.TermsEntry.COLUMN_NAME_END,

        };

        Cursor cursor = db.query(
                TermsContract.TermsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Term> termsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Term newTerm = new Term();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TermsContract.TermsEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_TITLE));

            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newTerm.setId(itemId);
            newTerm.setTitle(itemTitle);
            newTerm.setStart(itemStart);
            newTerm.setEnd(itemEnd);

            termsList.add(newTerm);
        }
        cursor.close();
        return termsList;
    }

    public Term getTerm(long termId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        String[] columns = {
                TermsContract.TermsEntry._ID,
                TermsContract.TermsEntry.COLUMN_NAME_TITLE,
                TermsContract.TermsEntry.COLUMN_NAME_START,
                TermsContract.TermsEntry.COLUMN_NAME_END,

        };

        // Filter results WHERE "_id" = 1
        String selection = TermsContract.TermsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(termId)};


        Cursor cursor = db.query(
                TermsContract.TermsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Term> termsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Term newTerm = new Term();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TermsContract.TermsEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_TITLE));

            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(TermsContract.TermsEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newTerm.setId(itemId);
            newTerm.setTitle(itemTitle);
            newTerm.setStart(itemStart);
            newTerm.setEnd(itemEnd);

            termsList.add(newTerm);
        }
        cursor.close();
        return termsList.get(0);
    }

    public long addTerm(Term term) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TermsContract.TermsEntry.COLUMN_NAME_TITLE, term.getTitle());
        values.put(TermsContract.TermsEntry.COLUMN_NAME_START, formatter.format(term.getStart()));
        values.put(TermsContract.TermsEntry.COLUMN_NAME_END, formatter.format(term.getEnd()));

        // Insert the new row, returning the primary key value of the new row
        long returnValue = db.insert(TermsContract.TermsEntry.TABLE_NAME, null, values);
        return returnValue;
    }

    public int updateTerm(Term selectedTerm) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TermsContract.TermsEntry.COLUMN_NAME_TITLE, selectedTerm.getTitle());
        values.put(TermsContract.TermsEntry.COLUMN_NAME_START, formatter.format(selectedTerm.getStart()));
        values.put(TermsContract.TermsEntry.COLUMN_NAME_END, formatter.format(selectedTerm.getEnd()));

        // Which row to update, based on the title
        String selection = TermsContract.TermsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(selectedTerm.getId())};

        int returnValue = db.update(TermsContract.TermsEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteTerm(long termId) throws TermHasCoursesException {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = TermsContract.TermsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(termId)};

        // check to see if this term has courses
        if (this.termHasCourses(termId)) {
            throw new TermHasCoursesException("Term ID: " + String.valueOf(termId) + " has courses.");
        }
        int returnValue = db.delete(TermsContract.TermsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public boolean termHasCourses(long termId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // query
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + CoursesContract.CoursesEntry.TABLE_NAME +
                " WHERE " + CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID + "=" + String.valueOf(termId), null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        if (count != 0) {
            // term has courses.
            return true;
        }
        // term doesn't have courses.
        return false;
    }

    public ArrayList<Assessment> getExamsDue() {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        String[] columns = {
                AssessmentsContract.AssessmentsEntry._ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE,
        };

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeek = cal.getTime();


        // Filter results WHERE "due_date" >= "yyyy/MM/dd" AND "due_date" <= "yyyy/MM/dd+7"
        String selection = AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE + " = 1";
        //String[] selectionArgs = {formatter.format(today), formatter.format(nextWeek)};

        Cursor cursor = db.query(
                AssessmentsContract.AssessmentsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Assessment> examsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Assessment newItem = new Assessment();

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry._ID));
            long itemCourseId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE));
            int itemAlarmDue = cursor.getInt(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE));

            Date itemDueDate = null;
            try {
                itemDueDate = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newItem.setId(itemId);
            newItem.setCourseId(itemCourseId);
            newItem.setTitle(itemTitle);
            newItem.setType(itemType);
            newItem.setDueDate(itemDueDate);
            newItem.setAlarmDue(itemAlarmDue);

            examsList.add(newItem);
        }
        cursor.close();
        return examsList;
    }

    public ArrayList<Course> getCoursesStarting() {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        String[] columns = {
                CoursesContract.CoursesEntry._ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TITLE,
                CoursesContract.CoursesEntry.COLUMN_NAME_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_STATUS,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END,
        };

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeek = cal.getTime();


        // Filter results WHERE "due_date" >= "yyyy/MM/dd" AND "due_date" <= "yyyy/MM/dd+7"
        String selection = CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START + " = 1";
        //String[] selectionArgs = {formatter.format(today), formatter.format(nextWeek)};

        Cursor cursor = db.query(
                CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> coursesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course newItem = new Course();

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE));
            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String itemStatus = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS));
            int itemAlarmStart = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START));
            int itemAlarmEnd = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END));

            newItem.setId(itemId);
            newItem.setName(itemTitle);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);
            newItem.setStatus(itemStatus);
            newItem.setAlarmStart(itemAlarmStart);
            newItem.setAlarmEnd(itemAlarmEnd);

            coursesList.add(newItem);
        }
        cursor.close();
        return coursesList;
    }

    public ArrayList<Course> getCoursesEnding() {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        String[] columns = {
                CoursesContract.CoursesEntry._ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TITLE,
                CoursesContract.CoursesEntry.COLUMN_NAME_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_STATUS,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END,
        };

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeek = cal.getTime();


        // Filter results WHERE "due_date" >= "yyyy/MM/dd" AND "due_date" <= "yyyy/MM/dd+7"
        String selection = CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END + " = 1";
        //String[] selectionArgs = {formatter.format(today), formatter.format(nextWeek)};

        Cursor cursor = db.query(
                CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> coursesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course newItem = new Course();

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE));
            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String itemStatus = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS));
            int itemAlarmStart = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START));
            int itemAlarmEnd = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END));

            newItem.setId(itemId);
            newItem.setName(itemTitle);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);
            newItem.setStatus(itemStatus);
            newItem.setAlarmStart(itemAlarmStart);
            newItem.setAlarmEnd(itemAlarmEnd);

            coursesList.add(newItem);
        }
        cursor.close();
        return coursesList;
    }

    public void alarmToggle(Object item, String alarmType) {

        /*  TODO: This could be abstracted.
         *  set the table name and columns in the switch,
         *  do the database read and write using the variables
         */
        // Gets the data repository in read/write mode
        SQLiteDatabase db = this.getWritableDatabase();

        switch (alarmType) {
            case ALARM_ASSESSMENT_DUE:
                // is item an Assessment?
                if (item instanceof Assessment) {
                    // toggle assessment due

                    // setup columns
                    String[] columns = {
                            AssessmentsContract.AssessmentsEntry._ID,
                            alarmType,
                    };

                    // Filter results WHERE _ID = item.getId()
                    String selection = AssessmentsContract.AssessmentsEntry._ID + " = ?";
                    String[] selectionArgs = {String.valueOf(((Assessment) item).getId())};

                    Cursor cursor = db.query(
                            AssessmentsContract.AssessmentsEntry.TABLE_NAME,   // The table to query
                            columns,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            null               // The sort order
                    );

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int value = cursor.getInt(cursor.getColumnIndexOrThrow(alarmType));
                        value = (value == 0) ? 1 : 0;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(alarmType, value);
                        String whereClause = AssessmentsContract.AssessmentsEntry._ID + " = ?";
                        String[] whereArgs = {String.valueOf(((Assessment) item).getId())};

                        // save the entry back
                        int rows = db.update(AssessmentsContract.AssessmentsEntry.TABLE_NAME,
                                contentValues,
                                whereClause,
                                whereArgs);
                        if (rows == 0) {
                            throw new RuntimeException("Unable to toggle alarm: " + contentValues.toString());
                        }
                    }
                }
                break;
            case ALARM_COURSE_START:
            case ALARM_COURSE_END:
                // is item a Course?
                if (item instanceof Course) {
                    // toggle course start/end
                    String[] columns = {
                            CoursesContract.CoursesEntry._ID,
                            alarmType,
                    };

                    // Filter results WHERE _ID = item.getId()
                    String selection = CoursesContract.CoursesEntry._ID + " = ?";
                    String[] selectionArgs = {String.valueOf(((Course) item).getId())};

                    Cursor cursor = db.query(
                            CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                            columns,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            null               // The sort order
                    );

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int value = cursor.getInt(cursor.getColumnIndexOrThrow(alarmType));
                        if (value == 1) {
                            value = 0;
                        } else {
                            value = 1;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(alarmType, value);

                        String whereClause = CoursesContract.CoursesEntry._ID + " = ?";
                        String[] whereArgs = {String.valueOf(((Course) item).getId())};

                        // save the entry back
                        int rows = db.update(CoursesContract.CoursesEntry.TABLE_NAME,
                                contentValues,
                                whereClause,
                                whereArgs);
                        if (rows == 0) {
                            throw new RuntimeException("Unable to toggle alarm: " + contentValues.toString());
                        }
                    }

                }
                break;
            default:
                break;

        }
    }

    public class TermHasCoursesException extends Exception {
        public TermHasCoursesException() {
        }

        public TermHasCoursesException(String message) {
            super(message);
        }
    }

    public ArrayList<Course> getAllCourses() {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // setup columns
        // columns from 'courses'
        String[] columns_courses = {
                CoursesContract.CoursesEntry._ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TITLE,
                CoursesContract.CoursesEntry.COLUMN_NAME_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_STATUS,

        };

        // columns from 'mentors'
        String[] columns_mentors = {
                MentorsContract.MentorsEntry._ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_NAME,
                MentorsContract.MentorsEntry.COLUMN_NAME_PHONE,
                MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL,

        };

        // columns from 'notes'
        String[] columns_notes = {
                NotesContract.NotesEntry._ID,
                NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID,
                NotesContract.NotesEntry.COLUMN_NAME_NOTE,

        };

        // columns from 'assessments'
        String[] columns_assessments = {
                AssessmentsContract.AssessmentsEntry._ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE,

        };

        Cursor cursor_courses = db.query(
                CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                columns_courses,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> itemsList = new ArrayList<>();
        while (cursor_courses.moveToNext()) {
            Course newItem = new Course();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            long itemId = cursor_courses.getLong(
                    cursor_courses.getColumnIndexOrThrow(CoursesContract.CoursesEntry._ID));
            String itemTitle = cursor_courses.getString(cursor_courses.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE));

            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor_courses.getString(cursor_courses.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor_courses.getString(cursor_courses.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newItem.setId(itemId);
            newItem.setName(itemTitle);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);

            // mentors
            newItem.setMentorArrayList(this.getMentorsByCourseId(itemId));

            // notes
            newItem.setNotesArrayList(this.getNotesByCourseId(itemId));

            itemsList.add(newItem);
        }
        cursor_courses.close();
        return itemsList;
    }

    public Course getCourse(long courseId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'courses'
        String[] columns = {
                CoursesContract.CoursesEntry._ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TITLE,
                CoursesContract.CoursesEntry.COLUMN_NAME_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_STATUS,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END,
        };

        // Filter results WHERE "_id" = 1
        String selection = CoursesContract.CoursesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};


        Cursor cursor = db.query(
                CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course newItem = new Course();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE));

            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String itemStatus = new String(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS)));
            int itemAlarmStart = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START));
            int itemAlarmEnd = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END));

            newItem.setId(itemId);
            newItem.setName(itemTitle);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);
            newItem.setStatus(itemStatus);
            newItem.setAlarmStart(itemAlarmStart);
            newItem.setAlarmStart(itemAlarmEnd);

            // mentors
            newItem.setMentorArrayList(this.getMentorsByCourseId(itemId));

            // notes
            newItem.setNotesArrayList(this.getNotesByCourseId(itemId));

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList.get(0);
    }

    public ArrayList<Course> getCoursesByTermId(long termId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'courses'
        String[] columns = {
                CoursesContract.CoursesEntry._ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID,
                CoursesContract.CoursesEntry.COLUMN_NAME_TITLE,
                CoursesContract.CoursesEntry.COLUMN_NAME_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_END,
                CoursesContract.CoursesEntry.COLUMN_NAME_STATUS,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START,
                CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END,
        };

        // Filter results WHERE "_id" = 1
        String selection = CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(termId)};


        Cursor cursor = db.query(
                CoursesContract.CoursesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course newItem = new Course();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE));

            Date itemStart = null, itemEnd = null;
            try {
                itemStart = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_START)));
                itemEnd = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_END)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String itemStatus = new String(cursor.getString(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS)));
            int itemAlarmStart = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_START));
            int itemAlarmEnd = cursor.getInt(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_ALARM_END));

            newItem.setId(itemId);
            newItem.setName(itemTitle);
            newItem.setStatus(itemStatus);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);
            newItem.setAlarmStart(itemAlarmStart);
            newItem.setAlarmStart(itemAlarmEnd);

            // mentors
            newItem.setMentorArrayList(this.getMentorsByCourseId(itemId));

            // notes
            newItem.setNotesArrayList(this.getNotesByCourseId(itemId));

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList;
    }


    public long addCourse(Course course) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID, course.getId());
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE, course.getName());
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_START, formatter.format(course.getStart()));
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_END, formatter.format(course.getEnd()));
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS, course.getStatus());
        // Insert the new row, returning the primary key value of the new row
        long returnValue = db.insert(CoursesContract.CoursesEntry.TABLE_NAME, null, values);
        return returnValue;
    }

    public long updateCourse(Course course) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_TITLE, course.getName());
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_START, formatter.format(course.getStart()));
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_END, formatter.format(course.getEnd()));
        values.put(CoursesContract.CoursesEntry.COLUMN_NAME_STATUS, course.getStatus());

        // Which row to update, based on the title
        String selection = CoursesContract.CoursesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(course.getId())};

        int returnValue = db.update(CoursesContract.CoursesEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteCourse(long courseId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // x
        // Which row to update, based on the title
        String selection = CoursesContract.CoursesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        int returnValue = db.delete(CoursesContract.CoursesEntry.TABLE_NAME, selection, selectionArgs);
        // delete mentors, notes, and exams
        this.deleteMentorsByCourseId(courseId);
        this.deleteNotesByCourseId(courseId);
        this.deleteAssessmentsByCourseId(courseId);

        return returnValue;
    }


    public ArrayList<Mentor> getMentorsByCourseId(long courseId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'mentors'
        String[] columns = {
                MentorsContract.MentorsEntry._ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_NAME,
                MentorsContract.MentorsEntry.COLUMN_NAME_PHONE,
                MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL,
        };

        // Filter results WHERE "_id" = 1
        String selection = MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};


        Cursor cursor = db.query(
                MentorsContract.MentorsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Mentor> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Mentor newItem = new Mentor();

            newItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry._ID)));
            newItem.setCourseId(cursor.getLong(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID)));
            newItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_NAME)));
            newItem.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_PHONE)));
            newItem.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL)));

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList;
    }

    public long addMentor(Mentor newItem) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID, newItem.getCourseId());
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_NAME, newItem.getName());
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL, newItem.getEmail());
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_PHONE, newItem.getPhone());

        // Insert the new row, returning the primary key value of the new row
        long returnValue = db.insert(MentorsContract.MentorsEntry.TABLE_NAME, null, values);
        return returnValue;
    }

    public Mentor getMentor(long mentorId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'courses'
        String[] columns = {
                MentorsContract.MentorsEntry._ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID,
                MentorsContract.MentorsEntry.COLUMN_NAME_NAME,
                MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL,
                MentorsContract.MentorsEntry.COLUMN_NAME_PHONE,
        };

        // Filter results WHERE "_id" = 1
        String selection = MentorsContract.MentorsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(mentorId)};


        Cursor cursor = db.query(
                MentorsContract.MentorsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Mentor> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Mentor newItem = new Mentor();

            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry._ID));
            long itemCourseId = cursor.getLong(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_NAME));
            String itemEmail = cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(MentorsContract.MentorsEntry.COLUMN_NAME_PHONE));

            newItem.setId(itemId);
            newItem.setCourseId(itemCourseId);
            newItem.setName(itemName);
            newItem.setEmail(itemEmail);
            newItem.setPhone(itemPhone);

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList.get(0);
    }

    public long updateMentor(Mentor mentor) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_NAME, mentor.getName());
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_EMAIL, mentor.getEmail());
        values.put(MentorsContract.MentorsEntry.COLUMN_NAME_PHONE, mentor.getPhone());

        // Which row to update, based on the title
        String selection = MentorsContract.MentorsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(mentor.getId())};

        int returnValue = db.update(MentorsContract.MentorsEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteMentor(long mentorId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = MentorsContract.MentorsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(mentorId)};

        int returnValue = db.delete(MentorsContract.MentorsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public int deleteMentorsByCourseId(long courseId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = MentorsContract.MentorsEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        int returnValue = db.delete(MentorsContract.MentorsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public ArrayList<Note> getNotesByCourseId(long courseId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'mentors'
        String[] columns = {
                NotesContract.NotesEntry._ID,
                NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID,
                NotesContract.NotesEntry.COLUMN_NAME_NOTE,
        };

        // Filter results WHERE "_id" = 1
        String selection = NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};


        Cursor cursor = db.query(
                NotesContract.NotesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Note> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Note newItem = new Note();

            newItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry._ID)));
            newItem.setCourseId(cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID)));
            newItem.setNote(cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_NAME_NOTE)));


            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList;
    }

    public long addNote(Note newItem) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID, newItem.getCourseId());
        values.put(NotesContract.NotesEntry.COLUMN_NAME_NOTE, newItem.getNote());

        // Insert the new row, returning the primary key value of the new row
        long returnValue = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, values);
        return returnValue;
    }

    public Note getNote(long noteId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'courses'
        String[] columns = {
                NotesContract.NotesEntry._ID,
                NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID,
                NotesContract.NotesEntry.COLUMN_NAME_NOTE,
        };

        // Filter results WHERE "_id" = 1
        String selection = NotesContract.NotesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteId)};


        Cursor cursor = db.query(
                NotesContract.NotesEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Note> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Note newItem = new Note();

            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry._ID));
            long itemCourseId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID));
            String itemNote = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_NAME_NOTE));

            newItem.setId(itemId);
            newItem.setCourseId(itemCourseId);
            newItem.setNote(itemNote);

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList.get(0);
    }

    public long updateNote(Note note) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NotesContract.NotesEntry.COLUMN_NAME_NOTE, note.getNote());

        // Which row to update, based on the title
        String selection = NotesContract.NotesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(note.getId())};

        int returnValue = db.update(NotesContract.NotesEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteNote(long noteId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = NotesContract.NotesEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteId)};

        int returnValue = db.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public int deleteNotesByCourseId(long courseId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        int returnValue = db.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public ArrayList<Assessment> getAssessmentsByCourseId(long courseId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        // columns from 'assessments'
        String[] columns = {
                AssessmentsContract.AssessmentsEntry._ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE,
        };

        // Filter results WHERE "_id" = 1
        String selection = AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};


        Cursor cursor = db.query(
                AssessmentsContract.AssessmentsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Assessment> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Assessment newItem = new Assessment();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

            newItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry._ID)));
            newItem.setCourseId(cursor.getLong(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID)));
            newItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE)));

            newItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE)));
            try {
                newItem.setDueDate(formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newItem.setAlarmDue(cursor.getInt(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE)));

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList;
    }

    public long addAssessment(Assessment newItem) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID, newItem.getCourseId());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE, newItem.getTitle());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE, newItem.getType());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE, formatter.format(newItem.getDueDate()));

        // Insert the new row, returning the primary key value of the new row
        long returnValue = db.insert(AssessmentsContract.AssessmentsEntry.TABLE_NAME, null, values);
        return returnValue;
    }

    public Assessment getAssessment(long assessmentId) {
        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        // columns from 'assessments'
        String[] columns = {
                AssessmentsContract.AssessmentsEntry._ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE,
                AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE,
        };

        // Filter results WHERE "_id" = 1
        String selection = AssessmentsContract.AssessmentsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(assessmentId)};


        Cursor cursor = db.query(
                AssessmentsContract.AssessmentsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Assessment> itemsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Assessment newItem = new Assessment();

            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry._ID));
            long itemCourseId = cursor.getLong(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE));
            Date itemDue = new Date();
            try {
                itemDue = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int itemAlarmDue = cursor.getInt(cursor.getColumnIndexOrThrow(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_ALARM_DUE));

            newItem.setId(itemId);
            newItem.setCourseId(itemCourseId);
            newItem.setTitle(itemTitle);
            newItem.setType(itemType);
            newItem.setDueDate(itemDue);
            newItem.setAlarmDue(itemAlarmDue);

            itemsList.add(newItem);
        }
        cursor.close();
        return itemsList.get(0);
    }

    public long updateAssessment(Assessment assessment) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AssessmentsContract.AssessmentsEntry._ID, assessment.getId());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE, assessment.getTitle());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE, assessment.getType());
        values.put(AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE, formatter.format(assessment.getDueDate()));

        // Which row to update, based on the title
        String selection = AssessmentsContract.AssessmentsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(assessment.getId())};

        int returnValue = db.update(AssessmentsContract.AssessmentsEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteAssessment(long assessmentId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = AssessmentsContract.AssessmentsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(assessmentId)};

        int returnValue = db.delete(AssessmentsContract.AssessmentsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public int deleteAssessmentsByCourseId(long courseId) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = AssessmentsContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        int returnValue = db.delete(AssessmentsContract.AssessmentsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

}
