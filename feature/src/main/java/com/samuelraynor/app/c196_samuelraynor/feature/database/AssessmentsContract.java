/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.database;

import android.provider.BaseColumns;

public class AssessmentsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AssessmentsContract() {
    }

    /* Inner class that defines the table contents */
    public static class AssessmentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "assessments";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DUE = "due_date";
        public static final String COLUMN_NAME_ALARM_DUE = "alarm_due";

    }

}
