package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Timetable;

public class PopUpAdd extends Activity {
    private final int[] addButtonIds = {
            R.id.add_subject,
            R.id.add_room,
            R.id.add_class
    };
    private final int addId = PopUp.getAddId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up_add);

        getWindow().setLayout(740, 655);

        TextView head = findViewById(R.id.head);

        EditText textAdd = findViewById(R.id.text_add);
        textAdd.setMinimumWidth(600);

        TextView error = findViewById(R.id.error_input);

        Button save = findViewById(R.id.save);

        //TODO: fertig machen, wenn vom Database team gefixt
        if (addId == addButtonIds[0]) { //Fach hinzufügen
            head.setText("Fach Hinzufügen");
            Spinner spinner = findViewById(R.id.select_color);
            spinner.setVisibility(View.VISIBLE);
            spinner.setMinimumWidth(600);

            save.setOnClickListener(v -> {
                String newSubject = String.valueOf(textAdd.getText());
                if (newSubject.isEmpty()) {
                    error.setText("Bitte Name des Fachs eingeben");
                } else {
                    Timetable.setSubject(newSubject, "", 1); //Placeholder für shortage und color
                    finish();
                }
            });
        } else if (addId == addButtonIds[1]) { //Raum hinzufügen
            head.setText("Raum Hinzufügen");

            save.setOnClickListener(v -> {
                String newRoom = String.valueOf(textAdd.getText());
                if (newRoom.isEmpty()) {
                    error.setText("Bitte Name des Raums eingeben");
                } else {
                    Timetable.setRoom(newRoom);
                    finish();
                }
            });
        } else { //Klasse hinzufügen
            head.setText("Klasse hinzufügen");

            save.setOnClickListener(v -> {
                String newClass = String.valueOf(textAdd.getText());
                if (newClass.isEmpty()) {
                    error.setText("Bitte Name der Klasse eingeben");
                } else {
                    Timetable.setClass(newClass);
                    finish();
                }
            });
        }
    }
}
