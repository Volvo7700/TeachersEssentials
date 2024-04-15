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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.teachersessentials.Shared;
import de.teachersessentials.databinding.FragmentHomeBinding;
import de.teachersessentials.R;
import de.teachersessentials.timetable.Timetable;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView clockTextView;
    private TextView timeLeftView;
    private TextView nextSubject;
    private TextView currentSubject;
    private Handler handler;
    private ProgressBar showProgress;
    private final ColorStateList colorRed = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.RED});
    private final ColorStateList colorBlue = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.parseColor("#1414B8")});

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Anzeige der aktuellen Uhrzeit
        clockTextView = root.findViewById(R.id.clock_text_view);
        clockTextView.setTextSize((float) (Shared.fontsize)); //Schriftgröße Uhr wird angepasst

        //Anzeige der verbleibenden Zeit
        timeLeftView = root.findViewById(R.id.time_left_view);
        timeLeftView.setTextSize((float) (Shared.fontsize*2)); //Schriftgröße des Timers wird angepasst

        //Aktuelles Fach
        currentSubject = root.findViewById(R.id.current_subject);
        currentSubject.setTextSize((float) (Shared.fontsize*0.75));
        //Anzeige des aktuellen Fachs einbauen (Timetable Database)

        //ProgressBar
        showProgress = root.findViewById(R.id.progress_bar);

        //Nächstes Fach
        nextSubject = root.findViewById((R.id.next_subject));
        nextSubject.setTextSize((float) (Shared.fontsize*0.75)); //Schriftgröße
        //Anzeige des nächsten Fachs einbauen (Timetable Database) und evt. Raum

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

        handler.postDelayed(this::updateClock, 1000); //Uhr updated jede Sekunde
    }


    private int getTimeUntilNextHour() {  //Zeit bis zur nächsten vollen Stunde in Millisekunden
        Calendar calendar = Calendar.getInstance();

        return ((60 - calendar.get(Calendar.MINUTE)) * 60 - calendar.get(Calendar.SECOND)) * 1000;
    }

    private void updateTimeLeft() {
        SimpleDateFormat timeLeftFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        Date date = new Date(getTimeUntilNextHour());

        String TimeLeftText = timeLeftFormat.format(date);
        timeLeftView.setText(TimeLeftText); //Zeit wird ins Widget gefüllt

        handler.postDelayed(this::updateTimeLeft, 1000); //Uhr updated jede Sekunde
    }

    private void updateProgress() {
        //Progress bar wird aufgefüllt bzw. leert sich
        showProgress.setProgress(getTimeUntilNextHour());

        if (getTimeUntilNextHour() < 300000) { //wahrscheinlich nicht effizient jedes mal neu abzufragen, müssen wir sowieso ändern, wenn eine Nachricht gesendet werden soll
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
