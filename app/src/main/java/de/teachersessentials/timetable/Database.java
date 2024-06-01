package de.teachersessentials.timetable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;

import androidx.navigation.NamedNavArgument;

import java.util.ArrayList;

import javax.security.auth.Subject;

import de.teachersessentials.csv.CsvParser;

public class Database {
    public static ArrayList<Lesson> lessons = new ArrayList<>();
    public static ArrayList<TimetableSubject> subjects = new ArrayList<>();
    public static ArrayList<TimetableRoom> rooms = new ArrayList<>();
    public static ArrayList<TimetableClass> classes = new ArrayList<>();

    public static void load(Context context) {
        ArrayList<String[]> rawLessons = CsvParser.read("timetable.csv",context);
        for (String[] item : rawLessons) {

            int id = Integer.parseInt(item[0]);
            int day = Integer.parseInt(item[1]);
            int time = Integer.parseInt(item[2]);
            int subject = Integer.parseInt(item[3]);
            int class_ = Integer.parseInt(item[4]);
            int room = Integer.parseInt(item[5]);

            Lesson lesson = new Lesson(id, day, time, subject, class_, room);
            lessons.add(lesson);
        }

        ArrayList<String[]> rawSubjects = CsvParser.read("subjects.csv",context);
        for (String[] item : rawSubjects) {

            int id = Integer.parseInt(item[0]);
            String display_name = item[1];
            String name = item[2];
            int colorint = Color.parseColor(item[3]);

            TimetableSubject subject = new TimetableSubject(id, display_name, name, colorint);
            subjects.add(subject);
        }

        ArrayList<String[]> rawRooms = CsvParser.read("room.csv",context);
        for (String[] item : rawRooms) {

            int id = Integer.parseInt(item[0]);
            String name = item[1];

            TimetableRoom room = new TimetableRoom(id, name);
            rooms.add(room);
        }

        ArrayList<String[]> rawClasses = CsvParser.read("class.csv",context);
        for (String[] item : rawClasses) {

            int id = Integer.parseInt(item[0]);
            String name = item[1];

            TimetableClass class_ = new TimetableClass(id, name);
            classes.add(class_);
        }
        
    }
    public static void save() {

    }
}
