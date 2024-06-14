package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        TextView error = findViewById(R.id.error_input);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());

        Button save = findViewById(R.id.save);

        //TODO: fertig machen, wenn vom Database team gefixt
        if (addId == addButtonIds[0]) { //Fach hinzufügen
            head.setText("Fach Hinzufügen");
            textAdd.setHint("Fach");
            EditText addShortage = findViewById(R.id.shortage_add);

            RelativeLayout relativeLayout2 = findViewById(R.id.relativ_layout_2);
            relativeLayout2.setVisibility(View.VISIBLE);

            error.setVisibility(View.GONE);

            TextView colorsText = findViewById(R.id.colors_text);
            colorsText.setVisibility(View.VISIBLE);

            ArrayList<Button> color_buttons = new ArrayList<>();
            for (int n = 1; n <= 16; n += 1) {
                int id = getResources().getIdentifier("color_" + (n), "id", getPackageName());
                Button button = findViewById(id);
                button.setVisibility(View.VISIBLE);
            }

            save.setOnClickListener(v -> {
                String newSubject = String.valueOf(textAdd.getText());
                String shortage = String.valueOf(addShortage.getText());
                if (newSubject.isEmpty()) {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Bitte Name des Fachs eingeben");
                } else {
                    Timetable.setSubject(newSubject, shortage, 1); //Placeholder für color
                    finish();
                }
            });
        } else if (addId == addButtonIds[1]) { //Raum hinzufügen
            head.setText("Raum Hinzufügen");
            textAdd.setHint("Raum");

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
            textAdd.setHint("Klasse");

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
