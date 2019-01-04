/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.util.Locale;

public class DateWatcherTextInputEditText extends TextInputEditText implements TextWatcher {

    public static final int MAX_FORMAT_LENGTH = 8;
    public static final int MIN_FORMAT_LENGTH = 3;

    private String updatedText;
    private boolean editing;

    public DateWatcherTextInputEditText(Context context) {
        super(context);
        this.addTextChangedListener(this);
    }

    public DateWatcherTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(this);
    }

    public DateWatcherTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (text.toString().equals(updatedText) || editing) return;

        String digitsOnly = text.toString().replaceAll("\\D", "");
        int digitLen = digitsOnly.length();

        if (digitLen < MIN_FORMAT_LENGTH || digitLen > MAX_FORMAT_LENGTH) {
            updatedText = digitsOnly;
            return;
        }

        if (digitLen <= 4) {
            String month = digitsOnly.substring(0, 2);
            String day = digitsOnly.substring(2);

            updatedText = String.format(Locale.US, "%s/%s", month, day);
        } else {
            String month = digitsOnly.substring(0, 2);
            String day = digitsOnly.substring(2, 4);
            String year = digitsOnly.substring(4);

            updatedText = String.format(Locale.US, "%s/%s/%s", month, day, year);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editing) return;

        editing = true;

        editable.clear();
        editable.insert(0, updatedText);

        editing = false;
    }


}
