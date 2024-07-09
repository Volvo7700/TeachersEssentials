package de.teachersessentials.ui.settings.editthings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableClass;
import de.teachersessentials.timetable.TimetableRoom;
import de.teachersessentials.timetable.TimetableSubject;
import de.teachersessentials.ui.gallery.PopUpAdd;

public class EditThings extends Activity {
    private final int[] listViewIds = {R.id.listView_subjects, R.id.listView_rooms, R.id.listView_classes};
    private final int[] extendButtonIds = {R.id.extend_subjects, R.id.extend_rooms, R.id.extend_classes};
    private final int[] settingsAddButtonIds = {R.id.add_subject_settings, R.id.add_room_settings, R.id.add_class_settings};
    static ArrayList<ListView> listViewsThings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_things);

        for (int id : listViewIds) {
            ListView newListView = findViewById(id);

            //Größe des ListViews wird angepasst
            ViewGroup.LayoutParams params = newListView.getLayoutParams();
            params.height = 400;
            newListView.setLayoutParams(params);

            listViewsThings.add(newListView);
        }

        ArrayList<Button> settingsAddButtons = new ArrayList<>();
        for (int id : settingsAddButtonIds) {
            Button addButton = findViewById(id);
            settingsAddButtons.add(addButton);
        }

        //OnClicks der addButtons
        settingsAddButtons.get(0).setOnClickListener(v -> {
            Intent intent = new Intent(this, PopUpAdd.class);
            intent.putExtra("addId", R.id.add_subject);
            this.startActivity(intent);
        });
        settingsAddButtons.get(1).setOnClickListener(v -> {
            Intent intent = new Intent(this, PopUpAdd.class);
            intent.putExtra("addId", R.id.add_room);
            this.startActivity(intent);
        });
        settingsAddButtons.get(2).setOnClickListener(v -> {
            Intent intent = new Intent(this, PopUpAdd.class);
            intent.putExtra("addId", R.id.add_class);
            this.startActivity(intent);
        });

        updateData(this);

        for (int n = 0; n < 3; n += 1) {
            int finalN = n;
            Button extendButton = findViewById(extendButtonIds[n]);
            extendButton.setOnClickListener(v -> {

                //Alle ListViews werden versteckt
                for (ListView listView : listViewsThings) {
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = 0;
                    listView.setLayoutParams(params);
                }

                //TODO richtig anpassen, performanceeffizient

                /*int heightInDp = listViewsThings.get(finalN).getCount() * 40; // desired height in dp
                float density = getResources().getDisplayMetrics().density;
                int heightInPixels = (int) (heightInDp * density);

                ViewGroup.LayoutParams params = listViewsThings.get(finalN).getLayoutParams();
                params.height = heightInPixels;
                listViewsThings.get(finalN).setLayoutParams(params);*/
            });
        }
    }

    public static void updateData(Context context) {
        //Listview wir mit Daten befüllt
        List<TimetableRoom> rooms = Timetable.getAllRooms();
        RoomAdapter roomAdapter = new RoomAdapter(context, rooms);
        listViewsThings.get(1).setAdapter(roomAdapter);

        //Listview wir mit Daten befüllt
        List<TimetableClass> classes = Timetable.getAllClasses();
        ClassAdapter classAdapter = new ClassAdapter(context, classes);
        listViewsThings.get(2).setAdapter(classAdapter);

        //Listview wir mit Daten befüllt
        List<TimetableSubject> subjects = Timetable.getAllSubjects();
        SubjectAdapter subjectApadter = new SubjectAdapter(context, subjects);
        listViewsThings.get(0).setAdapter(subjectApadter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listViewsThings.clear();
        Database.save(this);
    }
}