package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableClass;
import de.teachersessentials.timetable.TimetableRoom;
import de.teachersessentials.timetable.TimetableSubject;

public class PopUp extends Activity {

    private final int[] spinnerIds = {
            R.id.select_subject,
            R.id.select_room,
            R.id.select_class,
    };
    List<Spinner> spinners = new ArrayList<>(); //Alle Spinner werden in einer Liste gespeichert
    private final int[] addButtonIds = {
            R.id.add_subject,
            R.id.add_room,
            R.id.add_class
    };
    public static int addId = -1;
    private final String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    private final ArrayList<TimetableSubject> subjects = Timetable.getAllSubjects();
    private final ArrayList<TimetableRoom> rooms = Timetable.getAllRooms();
    private final ArrayList<TimetableClass> classes = Timetable.getAllClasses();
    public ArrayList<String> subjectsName = new ArrayList<>();
    public ArrayList<String> roomsName = new ArrayList<>();
    public ArrayList<String> classesName = new ArrayList<>();
    private int positionSelectedSubject;
    private int positionSelectedRoom;
    private int positionSelectedClass;
    private final int selectedDayOfWeek = GalleryFragment.getSelectedDayOfWeek();
    private final int selectedLesson = GalleryFragment.getSelectedLesson();
    private final Lesson currentLesson = Timetable.getLesson(selectedDayOfWeek, selectedLesson);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up);

        getWindow().setLayout(740, 655);

        TextView headline = findViewById(R.id.headline);
        String headlineText = days[selectedDayOfWeek] + ", " + (selectedLesson + 1) + ". Stunde"; //Anzeige Überschrift, welche Stunde genau
        headline.setText(headlineText);

        for (int id : spinnerIds) {
            Spinner spinner = findViewById(id);
            spinners.add(spinner);
        }

        for (Spinner spinner : spinners) {
            spinner.setMinimumWidth(800);
        }

        for (int id : addButtonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                startActivity(new Intent(PopUp.this, PopUpAdd.class));
                addId = id;
            }); //jeder Button erhält eigene OnClick
        }

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            for (Spinner spinner : spinners) {
                spinner.setSelection(0); //position 0 muss Platzhalter für keine Eingabe sein
            }
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            //ids: 100 bis 110 für Montag, 200 bis 210 für Dienstag, usw
            int id; // legt id für stunde fest
            if (selectedLesson <= 9) {
                id = Integer.parseInt(selectedDayOfWeek + "0" + selectedLesson);
            } else {
                id = Integer.parseInt(selectedDayOfWeek + "" + selectedLesson);
            }

            if ((positionSelectedRoom != 0 && positionSelectedClass != 0 && positionSelectedSubject != 0)) {
                //Eintragung in Datenbank

                int idSelectedSubject = 0;
                for (TimetableSubject subject : subjects) {
                    if (Objects.equals(subject.name, subjectsName.get(positionSelectedSubject))) {
                        idSelectedSubject = subject.id;
                    }
                }

                int idSelectedClass = 0;
                for (TimetableClass classs : classes) {
                    if (Objects.equals(classs.name, classesName.get(positionSelectedClass))) {
                        idSelectedClass = classs.id;
                    }
                }

                int idSelectedRoom = 0;
                for (TimetableRoom room : rooms) {
                    if (Objects.equals(room.room, roomsName.get(positionSelectedRoom))) {
                        idSelectedRoom = room.id;
                    }
                }

                Timetable.setLesson(new Lesson(id, selectedDayOfWeek, selectedLesson,  idSelectedSubject, idSelectedClass, idSelectedRoom));
                //TODO Fehler: man kann fächer nur anders speichern, wenn man subject mit ändert, die änderung von rom alleine reicht nicht
                //TODO auch überschreiben funktioniert nicht richtig
                finish();

            } else if ((positionSelectedRoom == 0 && positionSelectedClass == 0 && positionSelectedSubject == 0)) {
                Timetable.setLesson(new Lesson(id, selectedDayOfWeek, selectedLesson, 0, 0, 0)); //TODO remove Lesson
                finish();

            } else {
                Toast.makeText(this, "Keine gültige Stunde angegeben", Toast.LENGTH_SHORT).show();
            }
        });

        generateStringLists();

        loadSpinnerContent(0, subjectsName, this);
        loadSpinnerContent(1, roomsName, this);
        loadSpinnerContent(2, classesName, this);
    }

    public void generateStringLists() {
        subjectsName = new ArrayList<>();
        subjectsName.add("Auswahl");
        for (int i = 0; i < subjects.size(); i += 1) {
            subjectsName.add(subjects.get(i).name);
        }

        roomsName = new ArrayList<>();
        roomsName.add("Auswahl");
        for (int i = 0; i < rooms.size(); i += 1) {
            roomsName.add(rooms.get(i).room);
        }

        classesName = new ArrayList<>();
        classesName.add("Auswahl");
        for (int i = 0; i < classes.size(); i += 1) {
            classesName.add(classes.get(i).name);
        }
    }

    public void loadSpinnerContent(int n, ArrayList<String> content, Context context) {
        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinners.get(n).setAdapter(adapter);

        if (currentLesson != null) {
            spinners.get(0).setSelection(currentLesson.subject + 1);
            spinners.get(1).setSelection(currentLesson.room + 1);
            spinners.get(2).setSelection(currentLesson.class_ + 1);
        }

        spinners.get(0).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSelectedSubject = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinners.get(1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSelectedRoom = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinners.get(2).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSelectedClass = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static int getAddId() {
        return addId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        generateStringLists();
        loadSpinnerContent(0, subjectsName, this);
        loadSpinnerContent(1, roomsName, this);
        loadSpinnerContent(2, classesName, this);
    }
}
