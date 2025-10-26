package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableSubject;

public class PopUpAdd extends Activity {
    public static int[] addButtonIds = {
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
    private final int[] selectColors = {
            0x000000, 0x525252, 0xA8A8A8, 0xEDEDED, 0x0000FF, 0x0080FF,
            0x009999, 0x008000, 0x00CC2C, 0x89F022, 0xE7C905, 0xFF8000,
            0xD70000, 0x990000, 0xE415E4, 0x9933FF
    };
    int selectedColor = -1;
    AtomicInteger colorSelection = new AtomicInteger();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_pop_up_add);

        getWindow().setLayout(800, 715);

        TextView head = findViewById(R.id.head);

        EditText textAdd = findViewById(R.id.text_add);

        //TextView error = findViewById(R.id.error_input);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());

        Button save = findViewById(R.id.save);

        //evt. werden inputs ausgelesen
        int addId = getIntent().getIntExtra("addId", -1);
        String headerInput = getIntent().getStringExtra("header");
        textAdd.setText(headerInput);
        String shortageInput = getIntent().getStringExtra("shortage");
        int colorInput = getIntent().getIntExtra("color", -1);
        int inputId = getIntent().getIntExtra("id", -1);

        if (addId == addButtonIds[0]) { //Fach hinzufügen
            head.setText("Fach Hinzufügen");
            textAdd.setHint("Fach");

            getWindow().setLayout(800, 900);

            //Kürzel
            RelativeLayout relativeLayout2 = findViewById(R.id.relativ_layout_2);
            relativeLayout2.setVisibility(View.VISIBLE);

            EditText addShortage = findViewById(R.id.shortage_add);
            addShortage.setText(shortageInput);

            ConstraintLayout buttonConstraints = findViewById(R.id.button_constraints);
            buttonConstraints.setVisibility(View.VISIBLE);

            View showColor = findViewById(R.id.show_color);

            //Buttons zum auswählen der Farbe
            ArrayList<Button> color_buttons = new ArrayList<>();
            for (int id : colorButtonIds) {
                Button button = findViewById(id);
                color_buttons.add(button);
            }

            //Hexcode eingabe einer Farbe
            EditText hexColorAdd = findViewById(R.id.color_hex_add);
            hexColorAdd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 7) {
                        //Eingegebene farbe soll angezeigt werden
                        try {
                            showColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(String.valueOf(s))));
                        } catch (NumberFormatException e) {
                            showColor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_3)));
                        }
                    } else {
                        showColor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_3)));
                    }
                }
            });

            //wert muss angegeben sein
            if (colorInput != -1) {
                hexColorAdd.setText(String.format("#%06X", colorInput));
            } else {
                //sonst Standartwert dunkelgrau
                showColor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_3)));
            }

            for (Button button : color_buttons) {
                button.setOnClickListener(v -> {
                    colorSelection.set(color_buttons.indexOf(button));
                    selectedColor = colorSelection.get();

                    //Farbe von Button wird in Textfeld eingtragen
                    hexColorAdd.setText(String.format("#%06X", selectColors[selectedColor]));
                });
            }

            save.setOnClickListener(v -> {
                //Asulesen der Texteingaben
                String newSubject = String.valueOf(textAdd.getText());
                String shortage = String.valueOf(addShortage.getText());
                String hexcode = String.valueOf(hexColorAdd.getText());

                if (shortage.length() < 5) { //Kürzel darf nicht zu lang sein
                    if (newSubject.isEmpty() || shortage.isEmpty()) { //Alle Parameter müssen angegeben sein
                        error("Bitte alles eingeben");
                    } else {
                        if (!hexcode.isEmpty()) {
                            try {
                                //Farbeigabe über Hexcode und textfeld
                                int color = Color.parseColor(hexcode);
                                if (inputId == -1) {
                                    Timetable.setSubject(newSubject, shortage, color);
                                } else {
                                    ArrayList<TimetableSubject> subjects = Timetable.getAllSubjects();
                                    //An der gleiche Stelle wird das Fach ersetzt
                                    int index = Database.subjects.indexOf(Timetable.getSubjectById(inputId));
                                    subjects.set(index, new TimetableSubject(inputId, shortage, newSubject, color));
                                    Database.subjects = subjects;
                                }
                                finish();
                            } catch (IllegalArgumentException e) {
                                error("Kein gültiger Hexcode");
                            }
                        } else {
                            //bei keiner Farbe wird grau als Farbe genommen
                            if (inputId == -1) {
                                Timetable.setSubject(newSubject, shortage, getResources().getColor(R.color.light_3));
                            } else {
                                ArrayList<TimetableSubject> subjects = Timetable.getAllSubjects();
                                //An der gleiche Stelle wird das Fach ersetzt
                                int index = Database.subjects.indexOf(Timetable.getSubjectById(inputId));
                                subjects.set(index, new TimetableSubject(inputId, shortage, newSubject, getResources().getColor(R.color.light_3)));
                                Database.subjects = subjects;
                            }
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
                if (newClass.length() <= 5) {
                    if (!newClass.isEmpty()) {
                        //Neue Klasse wird gespeichert
                        Timetable.setClass(newClass.toUpperCase());
                        finish();
                    } else { //nichts eingegeben
                        error("Bitte Name der Klasse eingeben");
                    }
                } else { //Name der Klasse zu lang
                    error("Klasse zu lang");
                }
            });
        }
    }

    private void error(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //error.setVisibility(View.VISIBLE);
        //error.setText(message);
    }
}