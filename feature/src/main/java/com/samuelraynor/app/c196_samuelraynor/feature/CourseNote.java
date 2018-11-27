package com.samuelraynor.app.c196_samuelraynor.feature;

import android.os.Build;

import java.util.Objects;

public class CourseNote {
    private int noteId;
    private String text;

    public CourseNote(int noteId, String text) {
        this.noteId = noteId;
        this.text = text;
    }

    @Override
    public String toString() {
        return "CourseNote{" +
                "noteId=" + noteId +
                ", text='" + text + '\'' +
                '}';
    }

}
