/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.database;

import android.provider.BaseColumns;


public final class TermsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TermsContract() {
    }

    /* Inner class that defines the table contents */
    public static class TermsEntry implements BaseColumns {
        public static final String TABLE_NAME = "terms";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START = "start_date";
        public static final String COLUMN_NAME_END = "end_date";
    }


}
