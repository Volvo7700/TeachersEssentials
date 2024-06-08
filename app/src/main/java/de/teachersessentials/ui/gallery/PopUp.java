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

import de.teachersessentials.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up);

        getWindow().setLayout(740, 655);

        TextView headline = findViewById(R.id.headline);
        String headlineText = days[GalleryFragment.getSelectedDayOfWeek()] + ", " + (GalleryFragment.getSelectedLesson() + 1) + ". Stunde"; //Anzeige Überschrift, welche Stunde genau
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
            if ((positionSelectedRoom != 0 && positionSelectedClass != 0 && positionSelectedSubject != 0)
                    || (positionSelectedRoom == 0 && positionSelectedClass == 0 && positionSelectedSubject == 0)) {
                finish();
            } else {
                Toast.makeText(this, "Keine gültige Stunde angegeben", Toast.LENGTH_LONG).show();
            }
        });

        generateStringLists();

        loadSpinnerContent(0, subjectsName, this);
        loadSpinnerContent(1, roomsName, this);
        loadSpinnerContent(2, classesName, this);
    }

    public void generateStringLists() {
        subjectsName = new ArrayList<>();
        subjectsName.add("");
        for (int i = 0; i < subjects.size(); i += 1) {
            subjectsName.add(subjects.get(i).name);
        }

        roomsName = new ArrayList<>();
        roomsName.add("");
        for (int i = 0; i < rooms.size(); i += 1) {
            roomsName.add(rooms.get(i).room);
        }

        classesName = new ArrayList<>();
        classesName.add("");
        for (int i = 0; i < classes.size(); i += 1) {
            classesName.add(classes.get(i).name);
        }
    }

    public void loadSpinnerContent(int n, ArrayList<String> content, Context context) {
        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinners.get(n).setAdapter(adapter);

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
