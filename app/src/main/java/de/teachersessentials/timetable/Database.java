package de.teachersessentials.timetable;

import java.util.ArrayList;

import de.teachersessentials.csv.CsvParser;

public class Database {
    public static void load() {
        //private void loadSubjects() {
        //
        //}
        //private void loadClasses() {
        //
        //}
        //private void loadRooms() {
        //
        //}
        ArrayList<String[]> rawLessons = CsvParser.read("file");
        ArrayList<Lesson> lessons = new ArrayList<>();
        for (String[] item : rawLessons) {

            int id = Integer.parseInt(item[0]);
            int day = Integer.parseInt(item[1]);
            int time = Integer.parseInt(item[2]);
            int subject = Integer.parseInt(item[3]);
            int class_ = Integer.parseInt(item[4]);
            int room = Integer.parseInt(item[5]);
            Lesson lesson = new Lesson(id, day, lesson, subject, class_, room);
        }
        
    }
    public static void save() {

    }
}
