package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Timetable;

public class PopUpAdd extends Activity {
    private final int[] addButtonIds = {
            R.id.add_subject,
            R.id.add_room,
            R.id.add_class
    };
    private final int[] colorButtonIds = {
            R.id.color_1,
            R.id.color_2,
            R.id.color_3,
            R.id.color_4,
            R.id.color_5,
            R.id.color_6,
            R.id.color_7,
            R.id.color_8,
            R.id.color_9,
            R.id.color_10,
            R.id.color_11,
            R.id.color_12,
            R.id.color_13,
            R.id.color_14,
            R.id.color_15,
            R.id.color_16,
    };
    int selectedColor = -1;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_pop_up_add);

        getWindow().setLayout(740, 655);

        TextView head = findViewById(R.id.head);

        EditText textAdd = findViewById(R.id.text_add);

        error = findViewById(R.id.error_input);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());

        Button save = findViewById(R.id.save);

        int addId = getIntent().getIntExtra("addId", -1);
        String headerInput = getIntent().getStringExtra("header");
        textAdd.setText(headerInput);
        String shortageInput = getIntent().getStringExtra("shortage");


        //TODO: fertig machen, wenn vom Database team gefixt
        if (addId == addButtonIds[0]) { //Fach hinzufügen
            head.setText("Fach Hinzufügen");
            textAdd.setHint("Fach");

            //Kürzel
            RelativeLayout relativeLayout2 = findViewById(R.id.relativ_layout_2);
            relativeLayout2.setVisibility(View.VISIBLE);

            EditText addShortage = findViewById(R.id.shortage_add);
            addShortage.setText(shortageInput);

            error.setVisibility(View.GONE);

            TextView colorsText = findViewById(R.id.colors_text);
            colorsText.setVisibility(View.VISIBLE);

            //Hex Code der Farbe
            RelativeLayout relativeLayout3 = findViewById(R.id.relativ_layout_3);
            relativeLayout3.setVisibility(View.VISIBLE);

            ArrayList<Button> color_buttons = new ArrayList<>();
            for (int id : colorButtonIds) {
                Button button = findViewById(id);
                button.setVisibility(View.VISIBLE);
                color_buttons.add(button);
            }

            EditText hexColorAdd = findViewById(R.id.color_hex_add);
            hexColorAdd.setOnFocusChangeListener((v, hasFocus) -> selectedColor = -1);
            hexColorAdd.setOnClickListener(v -> selectedColor = -1);

            AtomicInteger colorSelection = new AtomicInteger();
            for (Button button : color_buttons) {
                button.setOnClickListener(v -> {
                    color_buttons.get(colorSelection.get()).setVisibility(View.VISIBLE);

                    colorSelection.set(color_buttons.indexOf(button));

                    color_buttons.get(colorSelection.get()).setVisibility(View.INVISIBLE);

                    selectedColor = colorSelection.get();
                    hexColorAdd.setText("");
                });
            }

            save.setOnClickListener(v -> {
                //Asulesen der Texteingaben
                String newSubject = String.valueOf(textAdd.getText());
                String shortage = String.valueOf(addShortage.getText());
                String hexcode = String.valueOf(hexColorAdd.getText());

                if (shortage.length() < 5) { //Kürzel darf nicht zu lang sein
                    if (newSubject.isEmpty() || shortage.isEmpty() || (selectedColor == -1 && hexcode.isEmpty())) { //Alle Parameter müssen angegeben sein
                        error("Bitte alles eingeben");
                    } else {
                        if (selectedColor == -1) {
                            try {
                                //Farbeigabe über Hexcode und textfeld
                                int  color = Color.parseColor(hexcode);
                                //TODO hier neue Farbe erstellen
                                Timetable.setSubject(newSubject, shortage, color); //TODO Farbe
                                finish();
                            } catch (IllegalArgumentException e) {
                                error("Kein gültiger Hexcode");
                            }
                        } else {
                            //Farbeingabe über buttons
                            Timetable.setSubject(newSubject, shortage, selectedColor); //TODO Farbe
                            finish();
                        }
                    }
                } else {
                    error("Kürzel zu lang");
                }

            });

        } else if (addId == addButtonIds[1]) { //Raum hinzufügen
            head.setText("Raum Hinzufügen");
            textAdd.setHint("Raum");

            save.setOnClickListener(v -> {
                String newRoom = String.valueOf(textAdd.getText());
                if (newRoom.length() < 5) {
                    if (!newRoom.isEmpty()) {
                        //Neuer Raum wird gespeichert
                        Timetable.setRoom(newRoom.toUpperCase());
                        finish();
                    } else { //nichts eingegeben
                        error("Bitte Nummer des Raums eingeben");
                    }
                } else { //Name des Raumes zu lang
                    error("Raumnummer zu lang");
                }
            });

        } else { //Klasse hinzufügen
            head.setText("Klasse hinzufügen");
            textAdd.setHint("Klasse");

            save.setOnClickListener(v -> {
                String newClass = String.valueOf(textAdd.getText());
                if (newClass.length() < 5) {
                    if (!newClass.isEmpty()) {
                        //Neue Klasse wird gespeichert
                        Timetable.setClass(newClass.toUpperCase());
                        finish();
                    } else { //nichts eingegeben
                        error("Bitte Name des Klasse eingeben");
                    }
                } else { //Name der Klasse zu lang
                    error("Klasse zu lang");
                }
            });
        }
    }
    private void error(String message) {
        error.setVisibility(View.VISIBLE);
        error.setText(message);
    }
}
