package com.samuelraynor.app.c196_samuelraynor.feature;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;


public class Course {


    private String name;
    private Date start;
    private Date end;

    private ArrayList<Mentor> mentorArrayList;

    @Status
    private String status;

    private ArrayList<Assessment> assessmentArrayList;
    
    private ArrayList<CourseNote> notesArrayList;

    public Course(String name, Date start, Date end, String status) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.status = status;
        this.mentorArrayList = new ArrayList<>();
        this.assessmentArrayList = new ArrayList<>();
        this.notesArrayList = new ArrayList<>();
    }

    public Course(String name, Date start, Date end, ArrayList<Mentor> mentorArrayList, String status, ArrayList<Assessment> assessmentArrayList, ArrayList<CourseNote> notesArrayList) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.mentorArrayList = mentorArrayList;
        this.status = status;
        this.assessmentArrayList = assessmentArrayList;
        this.notesArrayList = notesArrayList;
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

    public ArrayList<Mentor> getMentorArrayList() {
        return mentorArrayList;
    }

    public void setMentorArrayList(ArrayList<Mentor> mentorArrayList) {
        this.mentorArrayList = mentorArrayList;
    }

    public void addMentor(Mentor mentor) {
        this.mentorArrayList.add(mentor);
    }
    
    public void removeMentor(Mentor mentor) {
        this.mentorArrayList.remove(mentor);
    }
    
    public void clearMentors() {
        this.mentorArrayList.clear();
    }
    
    public ArrayList<Assessment> getAssessmentArrayList() {
        return assessmentArrayList;
    }

    public void setAssessmentArrayList(ArrayList<Assessment> assessmentArrayList) {
        this.assessmentArrayList = assessmentArrayList;
    }

    public void addAssessment(Assessment assessment) {
        this.assessmentArrayList.add(assessment);
    }

    public void removeAssessment(Assessment assessment) {
        this.assessmentArrayList.remove(assessment);
    }

    public void clearAssessments() {
        this.assessmentArrayList.clear();
    }
    @Status
    public String getStatus() {
        return this.status;
    }

    public void setStatus(@Status String status) {
        this.status = status;
    }

    public ArrayList<CourseNote> getNotesArrayList() {
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<CourseNote> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }

    public void addNote(CourseNote note) {
        this.notesArrayList.add(note);
    }

    public void removeNote(CourseNote note) {
        this.notesArrayList.remove(note);
    }

    public void clearNotess() {
        this.notesArrayList.clear();
    }
    
    @StringDef ({PLAN_TO_TAKE, IN_PROGRESS, COMPLETED, DROPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {}

    public static final String PLAN_TO_TAKE = "Plan to take";
    public static final String IN_PROGRESS = "In progress";
    public static final String COMPLETED = "Completed";
    public static final String DROPPED = "Dropped";

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", mentorArrayList=" + mentorArrayList +
                ", status='" + status + '\'' +
                ", assessmentArrayList=" + assessmentArrayList +
                '}';
    }
}