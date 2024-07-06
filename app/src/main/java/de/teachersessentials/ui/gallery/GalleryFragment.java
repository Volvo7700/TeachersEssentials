package de.teachersessentials.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.teachersessentials.databinding.FragmentGalleryBinding;
import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableRoom;
import de.teachersessentials.timetable.TimetableSubject;

public class GalleryFragment extends Fragment {
    private static int selectedLesson;
    private FragmentGalleryBinding binding;
    private final String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    private static int selectedDayOfWeek;
    List<Button> buttons = new ArrayList<>(); //Alle Buttons werden in einer Liste gespeichert
    List<TextView> subjects = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert
    List<TextView> rooms = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert

    private final int[] buttonIds = {
            R.id.lesson_button_0,
            R.id.lesson_button_1,
            R.id.lesson_button_2,
            R.id.lesson_button_3,
            R.id.lesson_button_4,
            R.id.lesson_button_5,
            R.id.lesson_button_6,
            R.id.lesson_button_7,
            R.id.lesson_button_8,
            R.id.lesson_button_9,
            R.id.lesson_button_10
    };

    private final int[] subjectIds = {
            R.id.subject_0,
            R.id.subject_1,
            R.id.subject_2,
            R.id.subject_3,
            R.id.subject_4,
            R.id.subject_5,
            R.id.subject_6,
            R.id.subject_7,
            R.id.subject_8,
            R.id.subject_9,
            R.id.subject_10
    };

    private final int[] roomIds = {
            R.id.room_0,
            R.id.room_1,
            R.id.room_2,
            R.id.room_3,
            R.id.room_4,
            R.id.room_5,
            R.id.room_6,
            R.id.room_7,
            R.id.room_8,
            R.id.room_9,
            R.id.room_10,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Calendar cal = Calendar.getInstance();
        selectedDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2; //Aktueller Wochentag, startet bei 0 (Montag)

        //Spinner zur Auswahl der Tage
        Spinner selectDay = root.findViewById(R.id.select_day);
        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectDay.setAdapter(adapter);
        if (selectedDayOfWeek <= 4) {
            selectDay.setSelection(selectedDayOfWeek); //aktueller Tag wird automatisch eingestellt
        } else {
            selectDay.setSelection(0);
        }

        selectDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDayOfWeek = position;
                updateButtons(selectedDayOfWeek);
                updateTextViews(selectedDayOfWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Button zum löschen eines ganzen Tages
        Button deleteDay = root.findViewById(R.id.delete_day);
        deleteDay.setOnClickListener(v -> {
            //Nachfrage ob wirklich gelöscht werden soll
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(days[selectedDayOfWeek] + " löschen");
            builder.setMessage("Soll der komplette am " + days[selectedDayOfWeek] + " eingetragene Stundenplan gelöscht werden?");
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                ArrayList<Lesson> dayLessons = Timetable.getDayLessons(selectedDayOfWeek);
                if (dayLessons.isEmpty()) {
                    //Keine Stunden am entsprechende Tag eingetragen
                    Toast.makeText(requireActivity(), "Keine Stunden vorhanden", Toast.LENGTH_SHORT).show();
                } else {
                    //Stunden des entsprechenden Tages werden gelöscht
                    for (Lesson lesson : dayLessons) {
                        Database.lessons.remove(lesson);
                    }
                    updateButtons(selectedDayOfWeek);
                    updateTextViews(selectedDayOfWeek);
                    Toast.makeText(requireActivity(), "Alle Stunden am " + days[selectedDayOfWeek] + " gelöscht", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
            }));
            builder.create().show(); //AlertDialog wird gezeigt
        });

        //Buttons zum eintragen des Stundenplans
        for (int id : buttonIds) { //für jeden Listenplatz wird der entsprechende Button erzeugt
            Button button = root.findViewById(id);
            buttons.add(button);
        }

        int number = 0;
        for (Button button : buttons) {
            int finalNumber = number;
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PopUp.class);
                startActivity(intent);
                selectedLesson = finalNumber;
            }); //jeder Button erhält eigene OnClick
            number += 1;
        }

        //TextViews zur Anzeige der Fächer
        for (int id : subjectIds) { //für jeden Listenplatz wird das entsprechende TextView erzeugt
            TextView subject = root.findViewById(id);
            subjects.add(subject);
        }

        //TextViews zur Anzeige der Räume
        for (int id : roomIds) { //für jeden Listenplatz wird das entsprechende TextView erzeugt
            TextView room = root.findViewById(id);
            rooms.add(room);
        }

        return root;
    }

    private void updateButtons(int dayOfWeek) {
        ArrayList<Lesson> dayLessons = Timetable.getDayLessons(dayOfWeek);

        //Alle Button zurücksetzen
        for (Button button : buttons) {
            button.setText(R.string.stunde_einfuegen);
            button.setBackgroundColor(getResources().getColor(R.color.light_3));
        }

        for (Lesson lesson : dayLessons) {
            buttons.get(lesson.hour).setText("");
            //button.setText(String.valueOf(Timetable.getAllSubjects().get(dayLessons[n].subject).color)); TODO farbig machen, aber wie?
            //buttons.get(lesson.hour).setBackgroundColor(colors[Timetable.getAllSubjects().get(lesson.subject).color]);
        }
    }

    private void updateTextViews(int dayOfWeek) {
        ArrayList<Lesson> dayLessons = Timetable.getDayLessons(dayOfWeek);

        //Alle TextViews Fächer zurücksetzen
        for (TextView textView : subjects) {
            textView.setText("");
        }

        //Alle TextViews Räume zurücksetzen
        for (TextView textView : rooms) {
            textView.setText("");
        }

        for (Lesson lesson : dayLessons) {
            //TextViews der Stunden werden upgedated
            //TextViews der Fächer
            try {
                TimetableSubject subject = Timetable.getSubjectById(lesson.subject);
                if (subject.name.length() > 15) {
                    //Name zu lang
                    subjects.get(lesson.hour).setText(subject.shortage);
                } else {
                    subjects.get(lesson.hour).setText(subject.name);
                }
            } catch (NullPointerException e) { //Falls Fach gelöscht wurde
                System.out.println(e.getMessage());
            }

            //TextViews der Räume
            try {
                TimetableRoom room = Timetable.getRoomById(lesson.room);
                rooms.get(lesson.hour).setText(String.valueOf(room.room));
            } catch (NullPointerException e) { //Falls Raum gelöscht wurde
                System.out.println(e.getMessage());
            }
        }
    }

    public static int getSelectedLesson() {
        return selectedLesson;
    }

    public static int getSelectedDayOfWeek() {
        return selectedDayOfWeek;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTextViews(selectedDayOfWeek);
        updateButtons(selectedDayOfWeek);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}