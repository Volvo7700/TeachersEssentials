package de.teachersessentials.timetable;

import java.util.Date;

public class Timetable {
    public Date start;
    public Date end;

    public static Lesson getLesson(int day, int lesson) {
        return new Lesson();
    }

    public static Lesson[] getAllLessons() {
        return new Lesson[0];
    }

    public static void setLesson(Lesson lesson) {

    }

    public static String[] getAllRooms() {
        return new String[0];
    }

    public static String[] getAllClasses() {
        return new String[0];
    }

    public Date getStart(int time) {
        start = new Date();
        if (time == 0) {
            start.setTime(28800000);//Erste Stunde von 08:00 bis 08:45
        } else if (time == 1) {
            start.setTime(31500000);//Zweite Stunde von 08:45 bis 09:30
        } else if (time == 2) {
            start.setTime(35100000);//Dritte Stunde von 09:45 bis 10:30
        } else if (time == 3) {
            start.setTime(37800000);//Vierte Stunde von 10:30 bis 11:15
        } else if (time == 4) {
            start.setTime(41400000);//Fünfte Stunde von 11:30 bis 12:15
        } else if (time == 5) {
            start.setTime(44100000);//Sechste Stunde von 12:15 bis 13:00
        } else if (time == 6) {
            start.setTime(46800000);//Siebte Stunde von 13:00 bis 13:45 (Normalerweise Mittagspause)
        } else if (time == 7) {
            start.setTime(49500000);//Achte Stunde von 13:45 bis 14:30
        } else if (time == 8) {
            start.setTime(52200000);//Neunte Stunde von 14:30 bis 15:15
        } else if (time == 9) {
            start.setTime(54900000);//Zehnte Stunde von 15:15 bis 16:00
        } else if (time == 10) {
            start.setTime(57600000);//Elfte Stunde von 16:00 bis 16:45
        } else {
            start = null; //Fehler
        }

        return start;
    }
    public Date getEnd(int time) {
        end = new Date();
        long startLesson = getStart(time).getTime();
        end.setTime(startLesson + 2700000);

        return end;
    }
}
