/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.model;

import android.app.ActivityManager;
import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Assessment implements Serializable {

    private long id;
    private long courseId;
    private String title;
    @Type
    private String type;
    private Date dueDate;

    public Assessment() {
        this.id = 0;
        this.courseId = 0;
        this.title = "New Assessment";
        this.type = OBJECTIVE;
        this.dueDate = new Date();
    }

    @StringDef({OBJECTIVE, PERFORMANCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public static final String OBJECTIVE = "Objective";
    public static final String PERFORMANCE = "Performance";

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        return "Assessment{" +
                "id='" + this.id + '\'' +
                ", courseId=" + this.courseId +
                ", type=" + this.type +
                ", dueDate=" + formatter.format(this.dueDate) +
                ", title='" + this.title + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
