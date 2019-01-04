/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.model;

import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;


public class Course implements Serializable {


    private String name;
    private Date start;
    private Date end;

    private ArrayList<Mentor> mentorArrayList;

    @Status
    private String status;

    private ArrayList<Assessment> assessmentArrayList;

    private ArrayList<Note> notesArrayList;
    private long id;
    private long termId;

    public Course() {
        Date today = new Date();
        this.name = "New Course";
        this.start = today;
        this.end = today;
        this.status = PLAN_TO_TAKE;
        this.mentorArrayList = new ArrayList<>();
        this.assessmentArrayList = new ArrayList<>();
        this.notesArrayList = new ArrayList<>();
    }

    public Course(String name, Date start, Date end, String status) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.status = status;
        this.mentorArrayList = new ArrayList<>();
        this.assessmentArrayList = new ArrayList<>();
        this.notesArrayList = new ArrayList<>();
    }

    public Course(String name, Date start, Date end, ArrayList<Mentor> mentorArrayList, String status, ArrayList<Assessment> assessmentArrayList, ArrayList<Note> notesArrayList) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.mentorArrayList = mentorArrayList;
        this.status = status;
        this.assessmentArrayList = assessmentArrayList;
        this.notesArrayList = notesArrayList;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public ArrayList<Note> getNotesArrayList() {
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<Note> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }

    public void addNote(Note note) {
        this.notesArrayList.add(note);
    }

    public void removeNote(Note note) {
        this.notesArrayList.remove(note);
    }

    public void clearNotes() {
        this.notesArrayList.clear();
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public long getTermId() {
        return termId;
    }

    @StringDef({PLAN_TO_TAKE, IN_PROGRESS, COMPLETED, DROPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

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