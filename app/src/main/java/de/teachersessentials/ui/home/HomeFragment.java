package de.teachersessentials.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.teachersessentials.Shared;
import de.teachersessentials.databinding.FragmentHomeBinding;
import de.teachersessentials.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView clockTextView;
    private TextView timeLeftView;
    private Handler handler;
    private ProgressBar showProgress;
    private final ColorStateList colorRed = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.RED});
    private final ColorStateList colorBlue = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.parseColor("#1414B8")});

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //anzeige der Uhrzeit
        clockTextView = root.findViewById(R.id.clock_text_view);
        clockTextView.setTextSize((float) (Shared.fontsize)); //Schriftgröße Uhr wird angepasst

        //Anzeige der verbleibenden Zeit
        timeLeftView = root.findViewById(R.id.time_left_view);
        timeLeftView.setTextSize((float) (Shared.fontsize*2)); //Schriftgröße des Timers wird angepasst

        //ProgressBar
        showProgress = root.findViewById(R.id.progress_bar);

        handler = new Handler();
        updateClock();
        updateTimeLeft();

        return root;
    }

    private void updateClock() {
        //Zeit wird abgerufen
        SimpleDateFormat dateFormatClock = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String clockText = dateFormatClock.format(new Date());
        //Zeit wird ins Widget gefüllt
        clockTextView.setText(clockText);

        //progress bar wird aufgefüllt
        updateProgress();
        //updateClock wird wieder aufgerufen, damit die Uhr jede Sekunde weitertickt
        handler.postDelayed(this::updateClock, 1000); //Uhr updated jede Sekunde
    }

    private void updateTimeLeft() { //noch nicht fertig
        SimpleDateFormat timeLeftFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String TimeLeftText = timeLeftFormat.format(new Date());

        timeLeftView.setText(TimeLeftText);
        //progress bar wird aufgefüllt
        updateProgress();
        //updateClock wird wieder aufgerufen, damit die Uhr jede Sekunde weitertickt
        handler.postDelayed(this::updateTimeLeft, 1000); //Uhr updated jede Sekunde
    }

    private void updateProgress() {
        //Zeit zur nächsten vollen Stunde in millis
        long progressTime = System.currentTimeMillis() % 3600000;
        //Progress bar wird aufgefüllt bzw. leert sich
        showProgress.setProgress(3600000 - (int) progressTime);

        if (progressTime > 3300000) { //wahrscheinlich nicht effizient jedes mal neu abzufragen, müssen wir sowieso ändern, wenn eine Nachricht gesendet werden soll
            //Progress bar wird rot
            showProgress.setProgressTintList(colorRed);
            //Hier möglicherweise Benachrictigung schicken
        }
        else {
            //progress bar ist sonst blau
            showProgress.setProgressTintList(colorBlue);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
