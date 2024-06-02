package de.teachersessentials.timetable;

import java.util.ArrayList;
import java.util.Date;

// Zugriffsklasse aus dem GUI auf die Datenbank
public class Timetable {
    private static final int[] lessonGrid = {28800000, 31500000, 35100000, 37800000, 41400000, 44100000, 46800000, 49500000, 52200000, 54900000, 57600000, 60300000};

    public static Lesson getLesson(int day, int lesson) {

        return new Lesson();
    }

    public static ArrayList<Lesson> getAllLessons() {
    //gibt uns alle Lessons
        ArrayList<Lesson> Lesson = Database.lessons;
        return Lesson;
    }

    public static ArrayList<Lesson> getDayLessons(int day) {
        //suchen Lessons mit passendem Tag und packen die in ne Liste - dann Liste ausgeben
        ArrayList<Lesson> Lessons = new ArrayList<>();
        for (Lesson current : Database.lessons){
            if(current.day == day){
                Lessons.add(current);
            }
        }

        return Lessons;
    }


    public static void setLesson(Lesson lesson) {

    }

    public static String[] getAllRooms() {

        return new String[0];
    }

    public static String[] getAllClasses() {

        return new String[0];
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
