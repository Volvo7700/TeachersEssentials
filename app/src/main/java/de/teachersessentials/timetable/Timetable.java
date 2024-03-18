package de.teachersessentials.timetable;

public class Timetable {
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
}
