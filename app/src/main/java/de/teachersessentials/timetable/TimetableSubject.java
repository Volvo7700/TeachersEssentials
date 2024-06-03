package de.teachersessentials.timetable;

import android.graphics.Color;

public class TimetableSubject {
    public int id;
    public String shortage;
    public String name;
    public int color;

    public TimetableSubject(int id_,String shortage_,String name_,int color_) {
        id = id_;
        shortage = shortage_;
        name = name_;
        color = color_;
    }
}