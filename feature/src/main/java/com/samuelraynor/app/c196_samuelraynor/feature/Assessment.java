package com.samuelraynor.app.c196_samuelraynor.feature;

import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Assessment implements Serializable {

    private String title;

    @Types
    private String type;

    private Date dueDate;

    public Assessment(String title, String type, Date dueDate) {
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Types
    public String getType() {
        return type;
    }

    public void setType(@Types String type) {
        this.type = type;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @StringDef({OBJECTIVE, PERFORMANCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Types {
    }

    public static final String OBJECTIVE = "Objective";
    public static final String PERFORMANCE = "Performance";

    @Override
    public String toString() {
        return "Assessment{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }

}