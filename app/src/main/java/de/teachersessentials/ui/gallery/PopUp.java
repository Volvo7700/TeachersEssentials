package de.teachersessentials.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String[] subjectsName;
    private String[] roomsName;
    private String[] classesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up);

        getWindow().setLayout(740, 655);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView headline = findViewById(R.id.headline);
        String headlineText = days[GalleryFragment.getSelectedDayOfWeek()] + ", " + (GalleryFragment.getSelectedLesson() + 1) + ". Stunde"; //Anzeige Überschrift, welche Stunde genau
        headline.setText(headlineText);

        List<Spinner> spinners = new ArrayList<>(); //Alle Spinner werden in einer Liste gespeichert

        for(int id : spinnerIds) {
            Spinner spinner = findViewById(id);
            spinners.add(spinner);
        }

        for(Spinner spinner : spinners) {
            spinner.setMinimumWidth(350);
        }

        for(int i = 0; i < subjects.size(); i += 1) {
            subjectsName[i] = subjects.get(i).name;
        }

        //Liste wird in Spinner geladen
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectsName);
        //adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //spinners.get(0).setAdapter(adapter);

        /*spinners.get(0).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDayOfWeek = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });*/

        for(int id : addButtonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                startActivity(new Intent(PopUp.this, PopUpAdd.class));
                addId = id;
            }); //jeder Button erhält eigene OnClick
        }

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            System.out.println(Arrays.toString(subjectsName));
            finish();
        });
    }

    public static int getAddId() {
        return addId;
    }
}
