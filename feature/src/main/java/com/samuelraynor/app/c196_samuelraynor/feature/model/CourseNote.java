/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.samuelraynor.app.c196_samuelraynor.feature.model;

import java.io.Serializable;

public class CourseNote implements Serializable {
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
