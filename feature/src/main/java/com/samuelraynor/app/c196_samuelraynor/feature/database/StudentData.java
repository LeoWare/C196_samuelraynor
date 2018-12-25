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

import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Mentor;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Note;
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentData extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
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
                    CoursesContract.CoursesEntry.COLUMN_NAME_STATUS + " TEXT)";

    private static final String SQL_DELETE_COURSES =
            "DROP TABLE IF EXISTS " + CoursesContract.CoursesEntry.TABLE_NAME;

    private static final String SQL_CREATE_MENTORS =
            "CREATE TABLE " + MentorsContract.MentorsEntry.TABLE_NAME + " (" +
                    MentorsContract.MentorsEntry._ID + " INTEGER PRIMARY KEY," +
                    NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
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
                    NotesContract.NotesEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_TYPE + " DATE," +
                    AssessmentsContract.AssessmentsEntry.COLUMN_NAME_DUE + " DATE)";
    ;

    private static final String SQL_DELETE_ASSESSMENTS =
            "DROP TABLE IF EXISTS " + AssessmentsContract.AssessmentsEntry.TABLE_NAME;

    public StudentData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TERMS);
        db.execSQL(SQL_CREATE_COURSES);
        db.execSQL(SQL_CREATE_MENTORS);
        db.execSQL(SQL_CREATE_NOTES);
        db.execSQL(SQL_CREATE_ASSESSMENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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

        int returnValue = db.delete(TermsContract.TermsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public class TermHasCoursesException extends Exception {
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
            Course newItem = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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


            newItem.setId(itemId);
            newItem.setTermId(cursor.getLong(cursor.getColumnIndexOrThrow(CoursesContract.CoursesEntry.COLUMN_NAME_TERM_ID)));
            newItem.setName(itemTitle);
            newItem.setStart(itemStart);
            newItem.setEnd(itemEnd);
            newItem.setStatus(itemStatus);

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
        return returnValue;
    }


    private ArrayList<Mentor> getMentorsByCourseId(long courseId) {
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

    private ArrayList<Note> getNotesByCourseId(long courseId) {
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

}
