package de.teachersessentials.timetable;

import android.content.Context;

import java.util.ArrayList;

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
            int colorint = Integer.parseInt(item[3]);

            TimetableSubject subject = new TimetableSubject(id, display_name, name, colorint);
            subjects.add(subject);
        }

        ArrayList<String[]> rawRooms = CsvParser.read("rooms.csv",context);
        for (String[] item : rawRooms) {

            int id = Integer.parseInt(item[0]);
            String name = item[1];

            TimetableRoom room = new TimetableRoom(id, name);
            rooms.add(room);
        }

        ArrayList<String[]> rawClasses = CsvParser.read("classes.csv",context);
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
        CsvParser.write("timetable.csv",rawLessons,headers,"Stundenplan",context);


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
        CsvParser.write("subjects.csv",rawSubjects,headers2,"Fächer",context);

        String[] headers3 = {"id","room_name"};
        ArrayList<String[]> rawRooms = new ArrayList<>();
        for (TimetableRoom room : rooms){
            String[] values = new String[2];
            values[0] = (String.valueOf(room.id));
            values[1] = room.room;
            rawLessons.add(values);
        }
        CsvParser.write("rooms.csv",rawRooms,headers3,"Räume",context);


        String[] headers4 = {"id","class_name"};
        ArrayList<String[]> rawClasses = new ArrayList<>();
        for (TimetableClass class_ : classes){
            String[] values = new String[2];
            values[0] = (String.valueOf(class_.id));
            values[1] = class_.name;
            rawLessons.add(values);
        }
        CsvParser.write("classes.csv",rawClasses,headers4,"Klassen",context);


    }

    // Die Datenbank mit den aktuellen Standarddaten füttern
    public static void generateDefaults(Context context) {
        String[] classes = { "5A", "5B", "5C", "5D", "5E", "5F", "5G", "5H", "5I",
                "6A", "6B", "6C", "6D", "6E", "6F", "6H", "6I", "6K",
                "7A", "7B", "7C", "7D", "7E", "7F", "7H",
                "8A", "8B", "8C", "8D", "8E", "8F",
                "9A", "9B", "9C", "9D", "9E", "9F",
                "10A", "10B", "10C", "10D", "10E", "10F", "10G",
                "11A", "11B", "11C", "11D", "11E", "11F",
                "12Q1", "12Q2", "12Q3", "12Q4", "12Q5", "12Q6" };

        String[] rooms = { "010", "014", "016", "018", "019", "021", "022", "025", "027", "028", "029", "040","042","044", "045", "046", "047", "049", "053", "060", "069", "084",
                "111", "112", "113", "117", "118", "121", "122", "123", "124", "125", "126", "127", "131", "132", "133", "134", "135", "136", "137", "141", "142", "143", "144", "145", "146", "147",
                "211", "212", "213", "214", "215", "216", "217", "221", "222", "223", "224", "225", "226", "231", "232", "233", "234", "235", "241", "242", "243", "242", "245", "246",
                "S004", "S005", "S007", "S008", "S009", "S010", "S011", "S013", "S014", "SU13", "SU14", "SU15",
                "S104", "S105", "S107", "S108", "S109", "S110", "S111",
                "S204", "S205", "S206", "S207", "S208", "S209", "S210", "S211", "S212", "S213", "S215", "S218",
                "S304", "S305", "S307", "S308", "S313", "S314" };

        String[] subjects = { "Deutsch", "Mathematik",
                "Englisch", "Französisch", "Latein", "Spanisch",
                "Physik", "Chemie", "Biologie", "Informatik",
                "Katholisch", "Evangelisch", "Ethik",
                "Geographie", "Geschichte", "Politik und Gesellschaft",
                "Sport", "Kunst", "Musik",
                "Physik Übung", "Chemie Übung",
                "Deutsch Int.", "Mathematik Int.", "Französisch Int.", "Latein Int.",
                "P-Seminar", "Modul zur berufl. Orientierung" };

        String[] shortages = {"D", "M",
                "E", "F", "L", "Sp",
                "Ph", "C", "B", "Inf",
                "K", "Ev", "Eth",
                "Geo", "G", "PuG",
                "S", "Ku", "Mu",
                "PhÜ", "ChÜ",
                "DInt", "MInt", "FInt", "LInt",
                "P", "MbO"};

        Integer[] colors = {0xD70000, 0x0080FF,
                0x00CC00, 0xFFFF00, 0x990000, 0x00CC66,
                0x0000FF, 0xFF00FF, 0x99FF33, 0x00FFFF,
                0x9933FF, 0x9933FF, 0x9933FF,
                0x994C00, 0x808080, 0xFF8000,
                0xE0E0E0, 0x009999, 0xCC0066,
                0x0000FF, 0xFFFF00,
                0xD70000, 0x0080FF, 0xFFFF00, 0x990000,
                0x000000, 0x404040
        };

        ArrayList<String[]> classes_arr = new ArrayList<>();
        ArrayList<String[]> rooms_arr = new ArrayList<>();
        ArrayList<String[]> subjects_arr = new ArrayList<>();

        // Arrays jeweils durchgehen und mit ins passende Format (Arraylist<String> mit "Primärschlüssel") konvertieren
        for (int i = 0; i < classes.length; i++) {
            String[] row = { Integer.toString(i), classes[i] };
            classes_arr.add(row);
        }
        for (int i = 0; i < rooms.length; i++) {
            String[] row = { Integer.toString(i), rooms[i] };
            rooms_arr.add(row);
        }
        for (int i = 0; i < subjects.length; i++) {
            String[] row = { Integer.toString(i), shortages[i], subjects[i], colors[i].toString() };
            subjects_arr.add(row);
        }

        CsvParser.write("classes.csv", classes_arr, new String[] {"ID", "Klasse"}, "Klassen", context);
        CsvParser.write("rooms.csv", rooms_arr, new String[] {"ID", "Raum"}, "Räume", context);
        CsvParser.write("subjects.csv", subjects_arr, new String[] {"ID", "Fach"}, "Fächer", context);
    }
}
