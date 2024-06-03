package de.teachersessentials.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.R;

public class PopUp extends Activity {

    private final int[] spinnerIds = {
            R.id.select_subject,
            R.id.select_room,
            R.id.select_class,
            R.id.select_color
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int) (width * 0.666), (int) (height * 0.3));

        List<Spinner> spinners = new ArrayList<>(); //Alle Spinner werden in einer Liste gespeichert

        for(int id : spinnerIds) {
            Spinner spinner = findViewById(id);
            spinners.add(spinner);
        }

        for(Spinner spinner : spinners) {
            spinner.setMinimumWidth((int) (height * 0.15));
        }

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> finish());
    }
}
