package de.teachersessentials.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableClass;
import de.teachersessentials.timetable.TimetableRoom;
import de.teachersessentials.timetable.TimetableSubject;

public class GalleryFragment extends Fragment {
    private static boolean weekSelected;
    private static int selectedLesson;
    private ViewBinding binding;
    private static final String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    private static int selectedDayOfWeek;
    static List<TextView> lessonViews = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert
    static List<Button> buttons = new ArrayList<>(); //Alle Buttons werden in einer Liste gespeichert
    static List<TextView> buttonsWeek = new ArrayList<>(); //Alle Buttons für Wochenanzeige werden in einer Liste gespeichert
    static List<TextView> subjects = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert
    static List<TextView> rooms = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert
    static List<TextView> classes = new ArrayList<>(); //Alle TextViews werden in einer Liste gespeichert
    static ImageButton addAfternoon;

    private static final int[] buttonIds = {
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

    private static final int[] subjectIds = {
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

    private static final int[] classIds = {
            R.id.class_0,
            R.id.class_1,
            R.id.class_2,
            R.id.class_3,
            R.id.class_4,
            R.id.class_5,
            R.id.class_6,
            R.id.class_7,
            R.id.class_8,
            R.id.class_9,
            R.id.class_10,
    };

    private static final int[] LessonViewIds = {
            R.id.lesson_view_0,
            R.id.lesson_view_1,
            R.id.lesson_view_2,
            R.id.lesson_view_3,
            R.id.lesson_view_4,
            R.id.lesson_view_5,
            R.id.lesson_view_6,
            R.id.lesson_view_7,
            R.id.lesson_view_8,
            R.id.lesson_view_9,
            R.id.lesson_view_10
    };

    private static final int[] buttonWeekIds = {
            R.id.button_0_0,
            R.id.button_0_1,
            R.id.button_0_2,
            R.id.button_0_3,
            R.id.button_0_4,
            R.id.button_0_5,
            R.id.button_0_6,
            R.id.button_0_7,
            R.id.button_0_8,
            R.id.button_0_9,
            R.id.button_0_10,
            R.id.button_1_0,
            R.id.button_1_1,
            R.id.button_1_2,
            R.id.button_1_3,
            R.id.button_1_4,
            R.id.button_1_5,
            R.id.button_1_6,
            R.id.button_1_7,
            R.id.button_1_8,
            R.id.button_1_9,
            R.id.button_1_10,
            R.id.button_2_0,
            R.id.button_2_1,
            R.id.button_2_2,
            R.id.button_2_3,
            R.id.button_2_4,
            R.id.button_2_5,
            R.id.button_2_6,
            R.id.button_2_7,
            R.id.button_2_8,
            R.id.button_2_9,
            R.id.button_2_10,
            R.id.button_3_0,
            R.id.button_3_1,
            R.id.button_3_2,
            R.id.button_3_3,
            R.id.button_3_4,
            R.id.button_3_5,
            R.id.button_3_6,
            R.id.button_3_7,
            R.id.button_3_8,
            R.id.button_3_9,
            R.id.button_3_10,
            R.id.button_4_0,
            R.id.button_4_1,
            R.id.button_4_2,
            R.id.button_4_3,
            R.id.button_4_4,
            R.id.button_4_5,
            R.id.button_4_6,
            R.id.button_4_7,
            R.id.button_4_8,
            R.id.button_4_9,
            R.id.button_4_10,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        //Tag wird geladen
        weekSelected = false;
        loadDay(requireActivity(), getContext(), root);

        return root;
    }

    public void loadWeek(FragmentActivity activity, Context context, View root) {
        //Richtiges Layout wird geladen
        ViewGroup container = root.findViewById(R.id.gallery_container);
        container.removeAllViews(); //altes Layout wir entfernt
        View weekView = LayoutInflater.from(context).inflate(R.layout.fragment_gallery_week, container, false);
        container.addView(weekView);

        //Button zum ändern der Ansicht
        TextView changeToDay = root.findViewById(R.id.change_day);
        changeToDay.setOnClickListener(v -> {
            weekSelected = false;
            loadDay(activity, context, root);
        });

        buttonsWeek = new ArrayList<>();
        for (int i = 0; i <= 54; i += 1) {
            TextView button = root.findViewById(buttonWeekIds[i]);

            updateButtons(0, context);

            //Buttons zum eintragen des Stundenplans
            int finalI = i;
            button.setOnClickListener(v -> {
                Intent intent = new Intent(activity, PopUp.class);
                startActivity(intent);
                selectedLesson = finalI%11; //jetzt
                selectedDayOfWeek = Math.round(((float) finalI - 5 )/11);
            });
            buttonsWeek.add(button);

        }
    }

    public void loadDay(FragmentActivity activity, Context context, View root) {
        ViewGroup container = root.findViewById(R.id.gallery_container);
        container.removeAllViews(); // clear old layout
        View dayView = LayoutInflater.from(context).inflate(R.layout.fragment_gallery_day, container, false);
        container.addView(dayView);

        TextView changeToWeek = root.findViewById(R.id.change_week);
        changeToWeek.setOnClickListener(v -> {
            weekSelected = true;
            loadWeek(activity, context, root);
        });

        Calendar calendar = Calendar.getInstance();
        selectedDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2; //Aktueller Wochentag, startet bei 0 (Montag)

        //Spinner zur Auswahl der Tage
        Spinner selectDay = root.findViewById(R.id.select_day);
        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, days);
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
                updateButtons(selectedDayOfWeek, context);
                updateTextViews(selectedDayOfWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Button zum löschen eines ganzen Tages
        ImageButton deleteDay = root.findViewById(R.id.delete_day);
        deleteDay.setOnClickListener(v -> {
            //Nachfrage ob wirklich gelöscht werden soll
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(days[selectedDayOfWeek] + " löschen");
            builder.setMessage("Soll der komplette am " + days[selectedDayOfWeek] + " eingetragene Stundenplan gelöscht werden?");
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                ArrayList<Lesson> dayLessons = Timetable.getDayLessons(selectedDayOfWeek);
                if (dayLessons.isEmpty()) {
                    //Keine Stunden am entsprechende Tag eingetragen
                    Toast.makeText(activity, "Keine Stunden vorhanden", Toast.LENGTH_SHORT).show();
                } else {
                    //Stunden des entsprechenden Tages werden gelöscht
                    for (Lesson lesson : dayLessons) {
                        Database.lessons.remove(lesson);
                    }
                    Database.save(activity);
                    updateButtons(selectedDayOfWeek, context);
                    updateTextViews(selectedDayOfWeek);
                    Toast.makeText(activity, "Alle Stunden am " + days[selectedDayOfWeek] + " gelöscht", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
            }));
            builder.create().show(); //AlertDialog wird gezeigt
        });

        //Listen werden resettet
        buttons = new ArrayList<>();
        rooms = new ArrayList<>();
        classes = new ArrayList<>();
        subjects = new ArrayList<>();
        lessonViews = new ArrayList<>();

        for (int i = 0; i <= 10; i += 1) {
            TextView lessonView = root.findViewById(LessonViewIds[i]);
            lessonViews.add(lessonView);

            //TextViews zur Anzeige der Fächer
            TextView subject = root.findViewById(subjectIds[i]);
            subjects.add(subject);

            //TextViews zur Anzeige der Räume
            TextView room = root.findViewById(roomIds[i]);
            rooms.add(room);

            TextView class_ = root.findViewById(classIds[i]);
            classes.add(class_);

            //Buttons zum eintragen des Stundenplans
            Button button = root.findViewById(buttonIds[i]);
            int finalI = i;
            button.setOnClickListener(v -> {
                Intent intent = new Intent(activity, PopUp.class);
                startActivity(intent);
                selectedLesson = finalI;
            });
            buttons.add(button);
        }

        addAfternoon = root.findViewById(R.id.add_afternoon);
        addAfternoon.setOnClickListener(v -> {
            for (int i = 6; i <= 10; i ++) {
                buttons.get(i).setVisibility(View.VISIBLE);
                lessonViews.get(i).setVisibility(View.VISIBLE);
            }
            addAfternoon.setVisibility(View.GONE);
        });
    }

    public static void updateButtons(int dayOfWeek, Context context) {
        if(!weekSelected) {
            ArrayList<Lesson> dayLessons = Timetable.getDayLessons(dayOfWeek);

            //Alle Buttons und Zeiten zurücksetzen
            for (int i = 0; i <= 10; i++) {
                buttons.get(i).setText(R.string.stunde_einfuegen);
                buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.light_3));
                buttons.get(i).setVisibility(View.VISIBLE);
                lessonViews.get(i).setVisibility(View.VISIBLE);
            }

            int maxHour = 0;
            for (Lesson lesson : dayLessons) {
                int hour = lesson.hour;

                if (hour > maxHour) {
                    maxHour = hour;
                }

                try {
                    String colorButtonHex = String.format("#%06X", Timetable.getSubjectById(lesson.subject).color);
                    //Buttons werden eingefärbt
                    buttons.get(hour).setText("");
                    buttons.get(hour).setBackgroundColor(Color.parseColor(colorButtonHex));
                    buttons.get(hour).setVisibility(View.VISIBLE);

                    int colorTextViews = context.getResources().getColor(getContrastColor(colorButtonHex));
                    //TextViews werden je nach Hintergrundfarbe der Button eingefärbt
                    subjects.get(hour).setTextColor(colorTextViews);
                    rooms.get(hour).setTextColor(colorTextViews);
                    classes.get(hour).setTextColor(colorTextViews);

                } catch (NullPointerException e) { //Fach wurde gelöscht
                    buttons.get(hour).setBackgroundColor(context.getResources().getColor(R.color.light_3));
                }
            }

            //Wenn kein Nachmittag, werden die entsprechenden Buttons entfernt
            if (maxHour < 10) {
                addAfternoon.setVisibility(View.VISIBLE);
            }
            int j = Integer.max(6, maxHour + 1);
            for (int i = j; i <= 10; i++) {
                buttons.get(i).setVisibility(View.GONE);
                lessonViews.get(i).setVisibility(View.GONE);
                subjects.get(i).setVisibility(View.GONE);
                classes.get(i).setVisibility(View.GONE);
                rooms.get(i).setVisibility(View.GONE);

            }
        } else { //Wochenansicht ausgewählt
            ArrayList<Lesson> weekLessons = Timetable.getAllLessons();

            for (TextView button : buttonsWeek) {
                button.setText("+");
            }

            for (Lesson lesson : weekLessons) {
                int hour = lesson.hour;
                int day = lesson.day;
                int number = day * 11 + hour;

                try {
                    TimetableSubject subject = Timetable.getSubjectById(lesson.subject);
                    String colorButtonHex = String.format("#%06X", Timetable.getSubjectById(lesson.subject).color);
                    buttonsWeek.get(number).setText(subject.shortage); //kürzle wird in Button eingefügt
                    //Buttons werden eingefärbt
                    buttonsWeek.get(number).setBackgroundColor(Color.parseColor(colorButtonHex)); //TODO farbe richtig machen
                    buttonsWeek.get(number).setVisibility(View.VISIBLE);

                    /*int colorTextViews = context.getResources().getColor(getContrastColor(colorButtonHex));
                    //TextViews werden je nach Hintergrundfarbe eingefärbt
                    buttonsWeek.get(hour).setTextColor(colorTextViews);*/

                    TextView button = buttonsWeek.get(number);
                    Drawable background = DrawableCompat.wrap(button.getBackground()).mutate();
                    DrawableCompat.setTint(background, Color.parseColor(colorButtonHex));
                    button.setBackground(background);
                    button.setTextColor(getContrastColor(colorButtonHex));

                } catch (NullPointerException e) { //Fach wurde gelöscht
                    buttons.get(hour).setBackgroundColor(context.getResources().getColor(R.color.light_3));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e); //Es gibt irgendeine OutOfBounds Exception, aber ich weiß nicht wieso //TODO überprüfen
                }
            }
        }
    }

    public static void updateTextViews(int dayOfWeek) {
        if(!weekSelected) {
            ArrayList<Lesson> dayLessons = Timetable.getDayLessons(dayOfWeek);

            for (int i = subjects.size() - 1; i >= 0; i -= 1) {
                subjects.get(i).setVisibility(View.GONE);
                classes.get(i).setVisibility(View.GONE);
                rooms.get(i).setVisibility(View.GONE);
            }

            for (Lesson lesson : dayLessons) {
                //TextViews der Stunden werden upgedated
                //TextViews der Fächer
                try {
                    TimetableSubject subject = Timetable.getSubjectById(lesson.subject);
                    if (subject.name.length() > 13) {
                        //Name zu lang
                        subjects.get(lesson.hour).setText(subject.shortage);
                    } else {
                        subjects.get(lesson.hour).setText(subject.name);
                    }
                    subjects.get(lesson.hour).setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    Database.lessons.remove(lesson);//Falls Fach gelöscht wurde
                }

                //TextViews der Räume und Klassen
                try {
                    TimetableRoom room = Timetable.getRoomById(lesson.room);
                    rooms.get(lesson.hour).setText(String.valueOf(room.room));
                    rooms.get(lesson.hour).setVisibility(View.VISIBLE);

                    TimetableClass class_ = Timetable.getClassById(lesson.class_);
                    classes.get(lesson.hour).setText(String.valueOf(class_.name));
                    classes.get(lesson.hour).setVisibility(View.VISIBLE);
                } catch (NullPointerException e) { //Falls Raum gelöscht wurde
                    Database.lessons.remove(lesson);
                }
            }
        }
    }

    public static int getContrastColor(String bgColor) {
        int bgR, bgG, bgB;
        //int werte der einzelnen Farben werden berechnet
        bgR = Integer.parseInt(bgColor.substring(1, 3), 16);
        bgG = Integer.parseInt(bgColor.substring(3, 5), 16);
        bgB = Integer.parseInt(bgColor.substring(5, 7), 16);
        double bgLuminance = relativeLuminance(bgR / 255.0, bgG / 255.0, bgB / 255.0);

        //besser passende Farbe wird zurückgegeben
        if (bgLuminance > 0.5) {
            return R.color.black;
        } else {
            return R.color.white;
        }
    }

    private static double relativeLuminance(double r, double g, double b) {
        //Berechnung
        r = r <= 0.03928 ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = g <= 0.03928 ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = b <= 0.03928 ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
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
        updateButtons(selectedDayOfWeek, getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}