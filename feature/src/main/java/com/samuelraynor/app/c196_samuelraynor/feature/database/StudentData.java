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
import com.samuelraynor.app.c196_samuelraynor.feature.model.Term;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentData extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "StudentData.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TermsContract.TermsEntry.TABLE_NAME + " (" +
                    TermsContract.TermsEntry._ID + " INTEGER PRIMARY KEY," +
                    TermsContract.TermsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TermsContract.TermsEntry.COLUMN_NAME_START + " DATE," +
                    TermsContract.TermsEntry.COLUMN_NAME_END + " DATE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TermsContract.TermsEntry.TABLE_NAME;

    public StudentData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
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
        String[] selectionArgs = { String.valueOf(termId) };


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
        String[] selectionArgs = { String.valueOf(selectedTerm.getId()) };

        int returnValue = db.update(TermsContract.TermsEntry.TABLE_NAME, values, selection, selectionArgs);
        return returnValue;
    }

    public int deleteTerm(long termId) throws TermHasCoursesException {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Which row to update, based on the title
        String selection = TermsContract.TermsEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(termId) };

        int returnValue = db.delete(TermsContract.TermsEntry.TABLE_NAME, selection, selectionArgs);
        return returnValue;
    }

    public Course getCourse(long courseId) {
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
        String[] selectionArgs = { String.valueOf(courseId) };


        Cursor cursor = db.query(
                TermsContract.TermsEntry.TABLE_NAME,   // The table to query
                columns,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> coursesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course newCourse = null;
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

            newCourse.setId(itemId);
            newCourse.setName(itemTitle);
            newCourse.setStart(itemStart);
            newCourse.setEnd(itemEnd);

            coursesList.add(newCourse);
        }
        cursor.close();
        return coursesList.get(0);
    }

    public class TermHasCoursesException extends Exception {
    }
}
