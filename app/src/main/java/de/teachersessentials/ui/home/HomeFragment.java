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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.ui.settings.Export;
import de.teachersessentials.util.ConfigFile;
import de.teachersessentials.databinding.FragmentHomeBinding;
import de.teachersessentials.R;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.util.notifications.Notifications;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView clockTextView;
    private TextView timeLeftView;
    private TextView nextSubject;
    private TextView currentSubject;
    private TextView showRoom;
    private TextView showClass;
    private ProgressBar showProgress;
    public static long timeInDay;
    public static int currentDay;
    private final ColorStateList colorRed = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
    //private final ColorStateList colorBlue = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#1414B8")});

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int fontsize = ConfigFile.getConfigData(requireActivity(), 1);

        //Aktueller Tag, Uhrzeit
        updateTimeAndDay();

        //Anzeige der aktuellen Uhrzeit
        clockTextView = root.findViewById(R.id.clock_text_view);
        clockTextView.setTextSize(fontsize); //Schriftgröße

        //Anzeige der verbleibenden Zeit
        timeLeftView = root.findViewById(R.id.time_left_view);
        timeLeftView.setTextSize(fontsize * 2); //Schriftgröße

        //Aktuelles Fach
        currentSubject = root.findViewById(R.id.current_subject);
        currentSubject.setTextSize((float) (fontsize * 1.5)); //Schriftgröße

        //Aktuelles Fach
        showRoom = root.findViewById(R.id.show_room);
        showRoom.setTextSize((float) (fontsize * 0.75)); //Schriftgröße

        //Aktuelles Fach
        showClass = root.findViewById(R.id.show_class);
        showClass.setTextSize((float) (fontsize * 0.75)); //Schriftgröße

        //ProgressBar
        showProgress = root.findViewById(R.id.progress_bar);

        //Nächstes Fach
        nextSubject = root.findViewById((R.id.next_subject));
        nextSubject.setTextSize((float) (fontsize * 0.75)); //Schriftgröße

        Button TestButton = root.findViewById(R.id.test); //Test Buttop links unten
        TestButton.setOnClickListener(v -> Export.downloadLastExport(requireContext()));

        updateClock();
        updateTimeLeftAndLessons();

        return root;
    }

    public static void updateTimeAndDay() {
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 2; //Aktueller Wochentag, startet bei 0 (Montag)
        timeInDay = (System.currentTimeMillis() + 7200000) % 86400000; //Zeit des Tages (nur Uhr ohne Datum oder andere Tage); 2 Stunden extra wegen Zeitzonen
    }

    private Lesson getCurrentLesson() {
        int currentLesson = Timetable.getLessonNumber(timeInDay); //Aktuelle Stunde

        return Timetable.getLesson(currentDay, currentLesson);
    }

    private Lesson getNextLesson() {
        int currentLesson = Timetable.getLessonNumber(timeInDay); //Nächste Stunde

        return Timetable.getLesson(currentDay, currentLesson + 1);
    }

    private void updateClock() {
        //Zeit wird abgerufen
        SimpleDateFormat dateFormatClock = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String clockText = dateFormatClock.format(new Date());
        //Zeit wird ins Widget gefüllt
        clockTextView.setText(clockText);

        //progress bar wird aufgefüllt
        updateProgress();
        updateTimeAndDay();

        Handler handler = new Handler();
        handler.postDelayed(this::updateClock, 1000); //Uhr updated jede Sekunde
    }


    public static long getTimeUntilNextLesson() {  //Zeit bis zur nächsten vollen Stunde in Millisekunden
        int lesson = Timetable.getLessonNumber(timeInDay - 1000);

        if (lesson == 12) { //Erst am nächsten Tag wieder Schule
            return -1;
        } else if (lesson == 11) { //Vor 8 oder Pause
            long timeInDayPlus = (timeInDay - 1000);
            while (lesson == 11) { //solange wiederholt bis nächste Stunde feststeht
                timeInDayPlus = timeInDayPlus + 900000;
                lesson = Timetable.getLessonNumber(timeInDayPlus);
            }
            return Timetable.getStart(lesson).getTime() - (timeInDay - 1000); //Zeit bis zum Start der nächsten Stunde
        } else {
            return Timetable.getEnd(lesson).getTime() - (timeInDay - 1000); //Zeit bis zum Ende der aktuellen Stunde
        }
    }

    private void updateTimeLeftAndLessons() {
        if (getTimeUntilNextLesson() == -1) {
            timeLeftView.setText(R.string.feierabend);
        } else {
            SimpleDateFormat timeLeftFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
            Date date = new Date(getTimeUntilNextLesson());
            timeLeftView.setText(timeLeftFormat.format(date)); //Zeit wird ins Widget gefüllt
        }

        //Anzeige aktuelle Stunde
        int currentLessonNumber = Timetable.getLessonNumber(timeInDay); //aktuelle Stunde

        //Atkuelle stunde
        Lesson currentLesson = getCurrentLesson();

        if (Timetable.getDayLessons(currentDay).isEmpty()) { //am entsprechende Tag gar kein Unterricht
            currentSubject.setText("Heute frei");
        } else {
            if (currentLessonNumber == 12) { //Schule verbei
                currentSubject.setText("Nach der Schule");
            } else if (currentLessonNumber == 11) { //Pause oder vor der Schule
                currentSubject.setText("Pause");
            } else { //während einer Stunde
                try {
                    if (Timetable.getSubjectById(currentLesson.subject).name.length() >= 15) {
                        currentSubject.setText(Timetable.getSubjectById(currentLesson.subject).shortage);
                    } else {
                        currentSubject.setText(Timetable.getSubjectById(currentLesson.subject).name);
                    }
                    showRoom.setText(Timetable.getRoomById(currentLesson.room).room);
                    showClass.setText(Timetable.getClassById(currentLesson.class_).name);
                } catch (NullPointerException ignore) {
                }
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(this::updateTimeLeftAndLessons, 1000); //Uhr updated jede Sekunde
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

        //Benachrichtigung
        if (timeUntilNextLesson >= 450000 && timeUntilNextLesson <= 451000) {
            Notifications.sendNotification(requireActivity(), getNextLesson());
        }

        if (timeUntilNextLesson < 300000) { //wahrscheinlich nicht effizient jedes mal neu abzufragen, müssen wir sowieso ändern, wenn eine Nachricht gesendet werden soll
            //Progress bar wird rot
            showProgress.setProgressTintList(colorRed);
        } else {
            //progress bar ist sonst blau
            try {
                showProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(String.format("#%06X", Timetable.getSubjectById(getCurrentLesson().subject).color))));
            } catch (RuntimeException ignore) {
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
