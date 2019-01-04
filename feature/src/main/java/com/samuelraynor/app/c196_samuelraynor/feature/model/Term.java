/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.model;

import com.samuelraynor.app.c196_samuelraynor.feature.model.Course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Term implements Serializable {
    private long id;
    private String name;
    private Date start;
    private Date end;

    //private ArrayList<Course> courseArrayList;

    /**
     *
     */
    public Term() {
        this.id = 0;
        this.name = "New Term";
        this.start = new Date();
        this.end = new Date();
        //this.courseArrayList = new ArrayList<>();
    }

    /**
     * @param name
     * @param start
     * @param end
     */
    public Term(String name, Date start, Date end) {
        this.id = 0;
        this.name = name;
        this.start = start;
        this.end = end;
        //this.courseArrayList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getTitle() {
        return name;
    }

    /**
     * @param name
     */
    public void setTitle(String name) {
        this.name = name;
    }

    /**
     * @return Date
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return Date
     */
    public Date getEnd() {
        return this.end;
    }

    /**
     * @param end
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @param course
     */
    //public void addCourse(Course course) {
    //    this.courseArrayList.add(course);
    //}

    /**
     * @param course
     */
    //public void removeCourse(Course course) {
    //    this.courseArrayList.remove(course);
    //}

    /**
     *
     */
    //public void clearCourses() {
    //    this.courseArrayList.clear();
    //}

}
