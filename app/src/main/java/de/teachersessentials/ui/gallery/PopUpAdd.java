package de.teachersessentials.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.Button;
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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int) (width * 0.666), (int) (height * 0.3));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView head = findViewById(R.id.head);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText textAdd = findViewById(R.id.text_add);
        textAdd.setMinimumWidth((int) (width * 0.5));
        textAdd.setAutofillHints("Eingabe");

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> finish());

        //TODO: fertig machen, wenn vom Database team gefixt
        if (addId == addButtonIds[0]) { //Fach hinzufügen
            head.setText("Fach Hinzufügen");
            textAdd.setAutofillHints("Neues Fach");
        } else if (addId == addButtonIds[1]){ //Raum hinzufügen
            head.setText("Raum Hinzufügen");
            textAdd.setAutofillHints("Neuer Raum");
            String newRoom = String.valueOf(textAdd.getText());
            /*save.setOnClickListener(v -> {
                Timetable.setRoom(newRoom);
                System.out.println(Timetable.getAllRooms());
                    });*/
        } else { //Klasse hinzufügen
            String newClass = String.valueOf(textAdd.getText());
            textAdd.setAutofillHints("Neue Klasse");
            save.setOnClickListener(v -> {
                        Timetable.setClass(newClass);
                        finish();
                    });
            head.setText("Klasse hinzufügen");
        }
    }
}
