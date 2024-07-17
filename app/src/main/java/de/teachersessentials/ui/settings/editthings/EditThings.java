package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.ui.gallery.PopUpAdd;

public class EditThings extends AppCompatActivity {
    private final int[] listViewIds = {R.id.listView_subjects, R.id.listView_rooms, R.id.listView_classes};
    private final int[] extendButtonIds = {R.id.extend_subjects, R.id.extend_rooms, R.id.extend_classes};
    private final int[] settingsAddButtonIds = {R.id.add_subject_settings, R.id.add_room_settings, R.id.add_class_settings};
    static ArrayList<ListView> listViewsThings = new ArrayList<>();
    private static final ArrayList<Integer> collapsedExtended = new ArrayList<>(Arrays.asList(0, 0, 0));
    static ArrayList<ImageButton> extendButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_things);

        //Zurück Pfeil
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_arrow);

        for (int id : listViewIds) {
            ListView newListView = findViewById(id);

            //Größe des ListViews wird angepasst
            ViewGroup.LayoutParams params = newListView.getLayoutParams();
            params.height = -100;
            newListView.setLayoutParams(params);

            listViewsThings.add(newListView);
        }

        ArrayList<ImageButton> settingsAddButtons = new ArrayList<>();
        for (int id : settingsAddButtonIds) {
            ImageButton addButton = findViewById(id);
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

        updateDataAll(this);

        //Buttons zum ausklappen/verstecken
        for (int Id : extendButtonIds) {
            ImageButton extendButton = findViewById(Id);
            extendButtons.add(extendButton);
        }

        for (ImageButton button : extendButtons) {
            int n = extendButtons.indexOf(button);
            //OnClick für jeden Button
            //TODO listen beim erneuten öffnen richtig machen
            button.setOnClickListener(v -> updateSize(n, button, true));
        }
    }

    public static void updateSize(int n, ImageButton button, boolean change) {
        ListView listView = listViewsThings.get(n);
        updateDataSingle(listView.getContext(), n);

        if (collapsedExtended.get(n) == 0 || !change) { //ausgewählte List view ist versteckt und soll ausgeklappt werden
            ListAdapter mAdapter = listView.getAdapter();
            //Höhe eines einzelnen Items wird gemessen
            View mView = mAdapter.getView(0, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            int heightSingular = mView.getMeasuredHeight();

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //Höhe wird so angepasst, dass alles genausichtbar ist
            //TODO  evt. performance
            params.height = heightSingular * mAdapter.getCount() + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
            listView.setLayoutParams(params);

            collapsedExtended.set(n, 1);
            button.setRotation(180);
        } else { //ausgewählte List view ist ausgeklappt und soll versteckt werden
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = -50;
            listView.setLayoutParams(params);

            collapsedExtended.set(n, 0);
            button.setRotation(90);
        }
    }

    public static void updateDataAll(Context context) {
        for (int i = 0; i <= 2; i++) {
            updateDataSingle(context, i);
        }
    }

    public static void updateDataSingle(Context context, int index) {
        int firstVisiblePosition = 0;
        int topPixel = 0;

        //Listview wir mit Daten befüllt
        ListView listView = listViewsThings.get(index);
        ListAdapter adapter = getAdapter(index, context);
        try {
            //Aktuelle Position der Scrollleiste
            firstVisiblePosition = listView.getFirstVisiblePosition();
            View firstVisibleView = listView.getChildAt(0);
            topPixel = firstVisibleView.getTop();
        } catch (RuntimeException ignored) {
        }
        //Neue Daten
        listView.setAdapter(adapter);
        //Position Scrollleiste
        listView.setSelectionFromTop(firstVisiblePosition, topPixel);
    }

    private static ListAdapter getAdapter(int index, Context context) {
        switch (index) {
            case 0:
                return new SubjectAdapter(context, Timetable.getAllSubjects());
            case 1:
                return new RoomAdapter(context, Timetable.getAllRooms());
            case 2:
                return new ClassAdapter(context, Timetable.getAllClasses());
            default:
                return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void resume() { //eigene Methode, damit auch von außerhalb aktiv gecalled werden kann
        //Größe, falls etwas hinzugefügt wurde wird angepasst
        for (int j = 0; j < 3; j++) {
            if (collapsedExtended.get(j) == 1) {
                updateSize(j, extendButtons.get(j), false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listViewsThings.clear();

        Database.save(this);
    }
}