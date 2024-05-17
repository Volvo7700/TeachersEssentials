package de.teachersessentials.timetable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;

import androidx.navigation.NamedNavArgument;

import java.util.ArrayList;

import javax.security.auth.Subject;

import de.teachersessentials.csv.CsvParser;

public class Database {
    public static void load(Context context) {
        //private void loadSubjects() {
        //
        //}
        //private void loadClasses() {
        //
        //}
        //private void loadRooms() {
        //
        //}

        ArrayList<String[]> rawLessons = CsvParser.read("timetable",context);
        ArrayList<Lesson> lessons = new ArrayList<>();
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

        ArrayList<String[]> rawSubjects = CsvParser.read("subjects",context);
        ArrayList<TimetableSubject> subjects = new ArrayList<>();
        for (String[] item : rawSubjects) {

            int id = Integer.parseInt(item[0]);
            String display_name = item[1];
            String name = item[2];
            String stringcolor = item[3];

            int colorint = Color.parseColor(stringcolor);
            Color color = Color.valueOf(colorint);

            //stringcolor.replace("Color(","");
            //stringcolor.replace(")","");
            //String[] colorvalues = stringcolor.split(", ");
            //int color_r = Integer.parseInt(colorvalues[0]);
            //int color_g = Integer.parseInt(colorvalues[1]);
            //int color_b = Integer.parseInt(colorvalues[2]);
            //int color_a = Integer.parseInt(colorvalues[3]);
            //ColorSpace.Named color_spacename = ColorSpace.Named.valueOf( colorvalues[4]);
            //Color color = Color.valueOf(color_r,color_g,color_b,color_a);

            TimetableSubject subject = new TimetableSubject(id, display_name, name, color);
            subjects.add(subject);
        }

        ArrayList<String[]> rawRooms = CsvParser.read("room",context);
        ArrayList<TimetableRoom> rooms = new ArrayList<>();
        for (String[] item : rawRooms) {

            int id = Integer.parseInt(item[0]);
            String name = item[1];

            TimetableRoom room = new TimetableRoom(id, name);
            rooms.add(room);
        }

        ArrayList<String[]> rawClasses = CsvParser.read("class",context);
        ArrayList<TimetableClass> classes = new ArrayList<>();
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
