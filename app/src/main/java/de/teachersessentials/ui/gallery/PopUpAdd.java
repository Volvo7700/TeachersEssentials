package de.teachersessentials.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;

import de.teachersessentials.R;

public class PopUpAdd extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop_up_add);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int) (width * 0.666), (int) (height * 0.3));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText textAdd = findViewById(R.id.text_add);
        textAdd.setMinimumWidth(width - 16);

    }
}
