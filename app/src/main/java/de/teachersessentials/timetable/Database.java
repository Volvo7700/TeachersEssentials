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

        ///// liest die CSV Dateien ("timetable.csv","subjects.csv","room.csv","class.csv") von dem mobilen Endgerät aus,
        ///// konvertiert sie von String zu ihren gewünschten Datentypen
        ///// und speichert sie von ArrayList<String[]> in Variablen der Objekte Lesson, TimetableSubject, TimetableRoom, TimetableClass

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
    public static void save(Context context) {

        /////// liest die Variablen der Objekte Lesson, TimetableSubject, TimetableRoom und TimetableClass
        /////// und speichert sie in einer ArrayList<String[]>   ( 1 Liste pro Objekt )
        /////// diese ArrayLists werden in ihren Dateien auf dem digitalen Endgerät mithilfe der Metode CsvParser.save() gespeichert

        String[] headers = {"id","day","hour","subject_id","class_id","room_id"};
        ArrayList<String[]> rawLessons = new ArrayList<>();
        for (Lesson lesson : lessons){
            String[] values = new String[6];
            values[0] = (String.valueOf(lesson.id));
            values[1] = (String.valueOf(lesson.day));
            values[2] = (String.valueOf(lesson.hour));
            values[3] = (String.valueOf(lesson.subject));
            values[4] = (String.valueOf(lesson.class_));
            values[5] = (String.valueOf(lesson.room));
            rawLessons.add(values);
        }
        CsvParser.write("timetable.csv",rawLessons,headers,"timetable",context);


        String[] headers2 = {"id","shortage","name","color"};
        ArrayList<String[]> rawSubjects = new ArrayList<>();
        for (TimetableSubject subject : subjects){
            String[] values = new String[4];
            values[0] = (String.valueOf(subject.id));
            values[1] = subject.shortage;
            values[2] = subject.name;
            values[3] = (String.valueOf(subject.color));
            rawLessons.add(values);
        }
        CsvParser.write("subjects.csv",rawSubjects,headers2,"subjects",context);


        String[] headers3 = {"id","room_name"};
        ArrayList<String[]> rawRooms = new ArrayList<>();
        for (TimetableRoom room : rooms){
            String[] values = new String[2];
            values[0] = (String.valueOf(room.id));
            values[1] = room.room;
            rawLessons.add(values);
        }
        CsvParser.write("room.csv",rawRooms,headers3,"rooms",context);


        String[] headers4 = {"id","class_name"};
        ArrayList<String[]> rawClasses = new ArrayList<>();
        for (TimetableClass class_ : classes){
            String[] values = new String[2];
            values[0] = (String.valueOf(class_.id));
            values[1] = class_.name;
            rawLessons.add(values);
        }
        CsvParser.write("class.csv",rawClasses,headers4,"timetable",context);


    }
}
