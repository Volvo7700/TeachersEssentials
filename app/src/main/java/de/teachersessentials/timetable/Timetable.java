package de.teachersessentials.timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

// Zugriffsklasse aus dem GUI auf die Datenbank
public class Timetable {
    private static final int[] lessonGrid = {28800000, 31500000, 35100000, 37800000, 41400000, 44100000, 46800000, 49500000, 52200000, 54900000, 57600000, 60300000};

    // FÄCHER FÜR PLAN
    // Eine bestimmte Lesson ausgeben
    public static Lesson getLesson(int day, int aua) {
        for (Lesson current : Database.lessons){
            if (current.day == day && current.hour == aua) {
                return current;
            }
        }
        return null;
    }

    public static TimetableSubject getSubjectById(int id) {
        for (TimetableSubject subject : getAllSubjects()) {
            if (id == subject.id) {
                return subject;
            }
        }
        throw new NullPointerException();
    }

    public static TimetableRoom getRoomById(int id) {
        for (TimetableRoom room : getAllRooms()) {
            if (id == room.id) {
                return room;
            }
        }
        throw new NullPointerException();
    }

    public static TimetableClass getClassById(int id) {
        for (TimetableClass class_ : getAllClasses()) {
            if (id == class_.id) {
                return class_;
            }
        }
        throw new NullPointerException();
    }

    // gibt uns alle Lessons
    public static ArrayList<Lesson> getAllLessons() {
        ArrayList<Lesson> Lesson = Database.lessons;
        return Lesson;
    }

    //suchen Lessons mit passendem Tag und packen die in ne Liste - dann Liste ausgeben
    public static ArrayList<Lesson> getDayLessons(int day) {
        ArrayList<Lesson> Lessons = new ArrayList<>();
        for (Lesson current : Database.lessons){
            if(current.day == day){
                Lessons.add(current);
            }
        }
        return Lessons;
    }

    // EINGABE
    // Lesson bearbeiten
    public static void setLesson(Lesson lesson) {
        int day = lesson.day;
        int aua = lesson.hour;

        ArrayList<Lesson> lessonToRemove = new ArrayList<>();
        for (Lesson current : Database.lessons) {
            if (current.day == day && current.hour == aua) {
                lessonToRemove.add(current);
            }
        }
        Database.lessons.removeAll(lessonToRemove);
        Database.lessons.add(lesson);
    }

    public static void removeLesson(int day, int hour) {
        Lesson lessonToRemove = null;
        for (Lesson current : Database.lessons) {
            if (current.day == day && current.hour == hour) {
                lessonToRemove = current;
                break;
            }
        }
        Database.lessons.remove(lessonToRemove);
    }

/*    public static void removeClass(int id) {
        TimetableClass classToRemove = null;
        for (TimetableClass current : Database.classes) {
            if (current.id == id) {
                classToRemove = current;
                break;
            }
        }
        Database.classes.remove(classToRemove);
    }

    public static void removeClass(String name) {
        TimetableClass classToRemove = null;
        for (TimetableClass current : Database.classes) {
            if (Objects.equals(current.name, name)) {
                classToRemove = current;
                break;
            }
        }
        Database.classes.remove(classToRemove);
    }


    public static void removeRoom(int id) {
        TimetableRoom roomToRemove = null;
        for (TimetableRoom current : Database.rooms) {
            if (current.id == id) {
                roomToRemove = current;
                break;
            }
        }
        Database.rooms.remove(roomToRemove);
    }

    public static void removeRoom(String room) {
        TimetableRoom roomToRemove = null;
        for (TimetableRoom current : Database.rooms) {
            if (Objects.equals(current.room, room)) {
                roomToRemove = current;
                break;
            }
        }
        Database.rooms.remove(roomToRemove);
    }

    public static void removeSubject(String shortage) {
        TimetableSubject subjectToRemove = null;
        for (TimetableSubject current : Database.subjects) {
            if (Objects.equals(current.shortage, shortage)) {
                subjectToRemove = current;
                break;
            }
        }
        Database.subjects.remove(subjectToRemove);
    }

    public static void removeSubject(int id) {
        TimetableSubject subjectToRemove = null;
        for (TimetableSubject current : Database.subjects) {
            if (current.id == id) {
                subjectToRemove = current;
                break;
            }
        }
        Database.subjects.remove(subjectToRemove);
    }*/
    //TO DO (überprüfen ob das da oben sinn macht)




    // AUSGABE
    // Alle Fächer ausgeben
    public static ArrayList<TimetableSubject> getAllSubjects() {
        ArrayList<TimetableSubject> Subjects = Database.subjects;
        return Subjects;
    }

    // Alle Räume ausgeben
    public static ArrayList<TimetableRoom> getAllRooms() {
        ArrayList<TimetableRoom> Rooms = Database.rooms;
        return Rooms;
    }

    // Alle Klassen ausgeben
    public static ArrayList<TimetableClass> getAllClasses() {
        ArrayList<TimetableClass> classes = Database.classes;
        return classes;
    }

    // EINGABE
    // Fächer Bearbeiten
    public static void setSubject(TimetableSubject subject){
        for (TimetableSubject current : Database.subjects) {
            if (current.id == subject.id) {
                Database.subjects.remove(current);
            }
        }
        Database.subjects.add(subject);
    }

    public static void setSubject(String name, String shortage, int color){
        for (TimetableSubject current : Database.subjects) {
            if (current.name == name) {
                Database.subjects.remove(current);
                return;
            }
        }
        int id;
        if (Database.subjects.isEmpty()) { //erst schauen, ob classes leer ist
            id = -1;
        } else {
            id = Database.subjects.get(Database.subjects.size() - 1).id; //id der letzten Klasse wird ausgelesen
        }
        TimetableSubject subject_ = new TimetableSubject(id + 1, shortage, name, color); //id der neuen Klasse ist eins größer
        Database.subjects.add(subject_);
    }

    public static void setSubject(int id, String name, String shortage, int color) {
        for (TimetableSubject current : Database.subjects) {
            if (current.id == id) {
                Database.subjects.remove(current);
            }
        }
        TimetableSubject subject_ = new TimetableSubject(id, shortage, name, color);
        Database.subjects.add(subject_);
    }

    // Klasse Bearbeiten
    public static void setClass(TimetableClass class_){
        for (TimetableClass current : Database.classes) {
            if (current.id == class_.id) {
                Database.classes.remove(current);
            }
        }
        Database.classes.add(class_);
    }

    public static void setClass(String name){
        for (TimetableClass current : Database.classes) {
            if (current.name == name) {
                return;
            }
        }
        int id;
        if (Database.classes.isEmpty()) { //erst schauen, ob classes leer ist
            id = -1;
        } else {
            id = Database.classes.get(Database.classes.size() - 1).id; //id der letzten Klasse wird ausgelesen
        }
        TimetableClass class_ = new TimetableClass(id + 1, name); //id der neuen Klasse ist eins größer
        Database.classes.add(class_);
    }

    public static void setClass(int id, String name ){
        for (TimetableClass current : Database.classes) {
            if (current.id == id) {
                Database.classes.remove(current);
            }
        }
        TimetableClass class_ = new TimetableClass(id, name);
        Database.classes.add(class_);
    }

    // Raum Bearbeiten
    public static void setRoom(TimetableRoom room){
        for (TimetableRoom current : Database.rooms) {
            if (current.id == room.id) {
                Database.rooms.remove(current);
            }
        }
        Database.rooms.add(room);
    }
    //❤❤❤🍔🍕
    public static void setRoom(String name){
        for (TimetableRoom current : Database.rooms) {
            if (current.room == name) {
                return;
            }
        }
        int id;
        if (Database.rooms.isEmpty()) {
            id = -1;
        } else {
            id = Database.rooms.get(Database.rooms.size() - 1).id;
        }
        TimetableRoom room_ = new TimetableRoom(id + 1, name);
        Database.rooms.add(room_);
    }

    public static void setRoom(int id, String name ){
        for (TimetableRoom current : Database.rooms) {
            if (current.id == id) {
                Database.rooms.remove(current);
            }
        }
        TimetableRoom room = new TimetableRoom(id, name);
        Database.rooms.add(room);
    }


    public static Date getStart(int time) { //gibt Startzeit der jeweiligen Stunde zurück
        Date start = new Date();
        if (time == 0) {
            start.setTime(lessonGrid[0]); //Erste Stunde von 08:00 bis 08:45
        } else if (time == 1) {
            start.setTime(lessonGrid[1]); //Zweite Stunde von 08:45 bis 09:30
        } else if (time == 2) {
            start.setTime(lessonGrid[2]); //Dritte Stunde von 09:45 bis 10:30
        } else if (time == 3) {
            start.setTime(lessonGrid[3]); //Vierte Stunde von 10:30 bis 11:15
        } else if (time == 4) {
            start.setTime(lessonGrid[4]); //Fünfte Stunde von 11:30 bis 12:15
        } else if (time == 5) {
            start.setTime(lessonGrid[5]); //Sechste Stunde von 12:15 bis 13:00
        } else if (time == 6) {
            start.setTime(lessonGrid[6]); //Siebte Stunde von 13:00 bis 13:45 (Normalerweise Mittagspause)
        } else if (time == 7) {
            start.setTime(lessonGrid[7]); //Achte Stunde von 13:45 bis 14:30
        } else if (time == 8) {
            start.setTime(lessonGrid[8]); //Neunte Stunde von 14:30 bis 15:15
        } else if (time == 9) {
            start.setTime(lessonGrid[9]); //Zehnte Stunde von 15:15 bis 16:00
        } else if (time == 10) {
            start.setTime(lessonGrid[10]); //Elfte Stunde von 16:00 bis 16:45
        } else {
            start = null; //Fehler
        }

        return start;
    }

    public static int getLessonNumber(long timeInMillis){ //für eine Zeit in Millisekunden wird die aktuelle Stunde ausgegeben
        int lessonNumber;
        if (lessonGrid[0] <= timeInMillis && timeInMillis < lessonGrid[1]) {
            lessonNumber = 0; //Erste Stunde von 08:00 bis 08:45
        } else if (lessonGrid[1] <= timeInMillis && timeInMillis < lessonGrid[2]) {
            lessonNumber = 1; //Zweite Stunde von 08:45 bis 09:30
        } else if (lessonGrid[2] <= timeInMillis && timeInMillis < lessonGrid[3]) {
            lessonNumber = 2; //Dritte Stunde von 09:45 bis 10:30
        } else if (lessonGrid[3] <= timeInMillis && timeInMillis < lessonGrid[4]) {
            lessonNumber = 3; //Vierte Stunde von 10:30 bis 11:15
        } else if (lessonGrid[4] <= timeInMillis && timeInMillis < lessonGrid[5]) {
            lessonNumber = 4; //Fünfte Stunde von 11:30 bis 12:15
        } else if (lessonGrid[5] <= timeInMillis && timeInMillis < lessonGrid[6]) {
            lessonNumber = 5; //Sechste Stunde von 12:15 bis 13:00
        } else if (lessonGrid[6] <= timeInMillis && timeInMillis < lessonGrid[7]) {
            lessonNumber = 6; //Siebte Stunde von 13:00 bis 13:45 (Normalerweise Mittagspause)
        } else if (lessonGrid[7] <= timeInMillis && timeInMillis < lessonGrid[8]) {
            lessonNumber = 7; //Achte Stunde von 13:45 bis 14:30
        } else if (lessonGrid[8] <= timeInMillis && timeInMillis < lessonGrid[9]) {
            lessonNumber = 8; //Achte Stunde von 14:30 bis 15:15
        } else if (lessonGrid[9] <= timeInMillis && timeInMillis < lessonGrid[10]) {
            lessonNumber = 9; //Neunte Stunde von 15:15 bis 16:00
        } else if (lessonGrid[10] <= timeInMillis && timeInMillis < lessonGrid[11]) {
            lessonNumber = 10; //Zehnte Stunde von 16:00 bis 16:45
        } else if (timeInMillis < lessonGrid[11]){
            lessonNumber = 11; //vor der Schule oder Pause
        } else {
            lessonNumber = 12; //nach der Schule
        }

        return lessonNumber;
    }

    public static Date getEnd(int time) { //gibt Endzeit der jeweiligen Stunde zurück
        Date end = new Date();
        long startLesson = getStart(time).getTime();
        end.setTime(startLesson + 2700000); //Stunde ist immer 45 Minuten lang -> Endzeit ist Startzeit + 45 Minuten

        return end;
    }
}
