package de.teachersessentials.ui.gallery;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.teachersessentials.databinding.FragmentGalleryBinding;
import de.teachersessentials.R;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;

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

    @SuppressLint("NewApi")
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

        //Buttons zum eintragen des Stundenplans
        for (int id : buttonIds) { //für jeden Listenplatz wird der entsprechende Button erzeugt
            Button button = root.findViewById(id);
            buttons.add(button);
        }

        int number = 0;
        for (Button button : buttons) {
            int finalNumber = number;
            button.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), PopUp.class));
                selectedLesson = finalNumber;
            }); //jeder Button erhält eigene OnClick
            number += 1;
        }

        updateButtons(selectedDayOfWeek);

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

        updateTextViews(selectedDayOfWeek);
        /*Button testbutton = (Button) root.findViewById(R.id.button_test);
        testbutton.setOnClickListener(v -> {
            TextView textView_test = (TextView) root.findViewById(R.id.textView_test);
            ArrayList<String[]> data = new ArrayList<>();
            String[] line = new String[4];
            line[0] = "hallo";
            line[1] = "test";
            line[2] = "hier";
            line[3] = "CSV";
            data.add(line);

            String[] headers = new String[1];
            headers[0] = "test";


            //CsvParser.save("test.csv", data, headers, "Testtabelle", getActivity().getApplicationContext());
            ArrayList<String[]> loadedData = CsvParser.read("test.csv", getActivity().getApplicationContext());


            textView_test.setText(loadedData.get(0).toString());
            String text = new String();
            for (String s : loadedData.get(0) )
            {
                text += s;
            }
            textView_test.setText(text);
        });*/

        return root;
    }

    private void updateButtons(int dayOfWeek) {
        int n = 0;

        Lesson[] dayLessons = Timetable.getDayLessons(dayOfWeek).toArray(new Lesson[11]); //hier Array benutzen, damit Fächer, die nicht gefüllt sind einen Platz in der Liste haben
        for (Button button : buttons) {
            if (dayLessons[n] == null) { //keine Stunde vorhanden
                //jeder Button wird zurückgestzt
                button.setText(R.string.stunde_einfuegen);
                button.setBackgroundColor(getResources().getColor(R.color.light_3));
            } else {
                button.setText("");
                //button.setBackgroundColor();
            }
            n += 1;
        }
    }

    private void updateTextViews(int dayOfWeek) {
        int n = 0;

        Lesson[] dayLessons = Timetable.getDayLessons(dayOfWeek).toArray(new Lesson[11]);
        String[] daySubjectNames = new String[11];
        String[] dayRoomNames = new String[11];

        for (Lesson lesson : dayLessons) { //namen der Fächer werden in einer Liste gespeichert
            if (lesson != null) {
                daySubjectNames[n] = String.valueOf(Timetable.getAllSubjects().get(lesson.subject).name);
            }
            if (lesson != null) {
                dayRoomNames[n] = String.valueOf(Timetable.getAllRooms().get(lesson.room).room);
            }
            n += 1;
        }

        n = 0;
        for (TextView subject : subjects) {
            if (dayLessons[n] == null) { //keine Stunde vorhanden
                //jedes Textview wird zurückgestzt
                subject.setText("");
            } else {
                subject.setText(daySubjectNames[n]);
            }
            n += 1;
        }

        n = 0;
        for (TextView room : rooms) {
            if (dayLessons[n] == null) { //keine Stunde vorhanden
                //jedes Textview wird zurückgestzt
                room.setText("");
            } else {
                room.setText(dayRoomNames[n]);
            }
            n += 1;
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