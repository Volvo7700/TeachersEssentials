package de.teachersessentials.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.teachersessentials.ConfigFile;
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
    private int lesson;
    private ProgressBar showProgress;
    private long timeInDay;
    private final ColorStateList colorRed = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.RED});
    private final ColorStateList colorBlue = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.parseColor("#1414B8")});

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int fontsize = Integer.parseInt(ConfigFile.getConfigData(requireActivity(), 1));

        //Anzeige der aktuellen Uhrzeit
        clockTextView = root.findViewById(R.id.clock_text_view);
        clockTextView.setTextSize((float) fontsize); //Schriftgröße

        //Anzeige der verbleibenden Zeit
        timeLeftView = root.findViewById(R.id.time_left_view);
        timeLeftView.setTextSize((float) fontsize * 2); //Schriftgröße

        //Aktuelles Fach
        currentSubject = root.findViewById(R.id.current_subject);
        currentSubject.setTextSize((float) ((float) fontsize * 0.75)); //Schriftgröße
        //Anzeige des aktuellen Fachs einbauen (Timetable Database)

        //ProgressBar
        showProgress = root.findViewById(R.id.progress_bar);

        //Nächstes Fach
        nextSubject = root.findViewById((R.id.next_subject));
        nextSubject.setTextSize((float) ((float) fontsize * 0.75)); //Schriftgröße
        //Anzeige des nächsten Fachs einbauen (Timetable Database) und evt. Raum

        Button TestButton = root.findViewById(R.id.test); //Test Buttop links unten
        TestButton.setOnClickListener((v -> System.out.println("Dieser Button kann zum Testen benutzt werden")));

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


    private long getTimeUntilNextLesson() {  //Zeit bis zur nächsten vollen Stunde in Millisekunden
        timeInDay = (System.currentTimeMillis() + 7200000) % 86400000 - 1000; //Zeit des Tages (nur Uhr ohne Datum oder andere Tage); 2 Stunden extra wegen Zeitzonen
        //timeInDay = 34506000;
        lesson = Timetable.getLessonNumber(timeInDay);

        if (lesson == 12) { //Erst am nächsten Tag wieder Schule
            return -1;
        } else if (lesson == 11) { //Vor 8 oder Pause
            long timeInDayPlus = timeInDay;
            while (lesson == 11) { //solange wiederholt bis nächste Stunde feststeht
                timeInDayPlus = timeInDayPlus + 900000;
                lesson = Timetable.getLessonNumber(timeInDayPlus);
            }
            return Timetable.getStart(lesson).getTime() - timeInDay; //Zeit bis zum Start der nächsten Stunde
        } else {
            return Timetable.getEnd(lesson).getTime() - timeInDay; //Zeit bis zum Ende der aktuellen Stunde
        }
    }

    private void updateTimeLeft() {
        if (getTimeUntilNextLesson() == -1) {
            timeLeftView.setText(R.string.feierabend);
        } else {
            SimpleDateFormat timeLeftFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
            Date date = new Date(getTimeUntilNextLesson());

            String TimeLeftText = timeLeftFormat.format(date);
            timeLeftView.setText(TimeLeftText); //Zeit wird ins Widget gefüllt
        }

        handler.postDelayed(this::updateTimeLeft, 1000); //Uhr updated jede Sekunde
    }

    private void updateProgress() { //Progress bar wird aufgefüllt bzw. leert sich
        long timeUntilNextLesson = getTimeUntilNextLesson();
        if (Timetable.getLessonNumber(timeInDay) == 11) {
            if (timeInDay < 30000000) { //vor 8
                showProgress.setMax(28800000); //Zeit von 0 bis 8 Uhr bestimmt Größe der ProgressBar
            } else {
                showProgress.setMax(900000); //Pausenlänge als Größe der Progressbar
            }
        } else {
            showProgress.setMax(2700000); //Länge der Stunde bestimmt Größe der ProgressBar
        }
        showProgress.setProgress((int) timeUntilNextLesson);

        if (timeUntilNextLesson < 300000) { //wahrscheinlich nicht effizient jedes mal neu abzufragen, müssen wir sowieso ändern, wenn eine Nachricht gesendet werden soll
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
