package de.teachersessentials.ui.settings.editthings;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableRoom;

public class EditThings extends Activity {
    private final int[] listViewIds = {R.id.listView_subjects, R.id.listView_rooms, R.id.listView_classes};
    private final int[] extendButtonIds = {R.id.extend_subjects, R.id.extend_rooms, R.id.extend_classes};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_things);

        ArrayList<ListView> listViewsThings = new ArrayList<>();
        for (int id : listViewIds) {
            ListView newListView = findViewById(id);

            //Größe des ListViews wird angepasst
            ViewGroup.LayoutParams params = newListView.getLayoutParams();
            params.height = 100;
            newListView.setLayoutParams(params);

            listViewsThings.add(newListView);
        }

        //Listview wir mit Daten befüllt
        List<TimetableRoom> rooms = Timetable.getAllRooms();
        RoomAdapter roomAdapter = new RoomAdapter(this, rooms);
        listViewsThings.get(0).setAdapter(roomAdapter);

        /*//Listview wir mit Daten befüllt
        List<TimetableRoom> rooms = Timetable.getAllRooms();
        RoomAdapter roomAdapter = new RoomAdapter(this, rooms);
        listViewsThings.get(0).setAdapter(roomAdapter);

        //Listview wir mit Daten befüllt
        List<TimetableRoom> rooms = Timetable.getAllRooms();
        RoomAdapter roomAdapter = new RoomAdapter(this, rooms);
        listViewsThings.get(0).setAdapter(roomAdapter);*/

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
}