package com.samuelraynor.app.c196_samuelraynor.feature;

import java.util.ArrayList;
import java.util.Date;

public class Term {
    private String name;
    private Date start;
    private Date end;

    private ArrayList<Course> courseArrayList;

    public Term(String name, Date start, Date end) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.courseArrayList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void addCourse(Course course) {
        this.courseArrayList.add(course);
    }

    public void removeCourse(Course course) {
        this.courseArrayList.remove(course);
    }

    public void clearCourses() {
        this.courseArrayList.clear();
    }

}
