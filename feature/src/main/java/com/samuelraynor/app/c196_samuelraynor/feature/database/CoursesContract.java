/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.database;

import android.provider.BaseColumns;

public final class CoursesContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CoursesContract() {
    }

    /* Inner class that defines the table contents */
    public static class CoursesEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_TERM_ID = "term_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START = "start_date";
        public static final String COLUMN_NAME_END = "end_date";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_ALARM_START = "alarm_start";
        public static final String COLUMN_NAME_ALARM_END = "alarm_end";

    }
}