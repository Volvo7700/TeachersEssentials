package de.teachersessentials.timetable;

public class Lesson {
    public int id;
    public int day;
    public int hour;
    public int subject;
    public int class_;
    public int room;

    public Lesson(int id_, int day_, int hour_, int subject_, int class__,int room_) {
        id = id_;
        day = day_;
        hour = hour_;
        subject = subject_;
        class_ = class__;
        room = room_;
    }
}