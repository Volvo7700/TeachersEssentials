package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableRoom;

public class RoomAdapter extends ArrayAdapter<TimetableRoom> {
    private final Context mContext;
    private final List<TimetableRoom> mRooms;

    public RoomAdapter(Context context, List<TimetableRoom> rooms) {
        super(context, 0, rooms);
        mContext = context;
        mRooms = rooms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.thing_list_item_edit, parent, false);
        }

        TimetableRoom room = mRooms.get(position);

        TextView roomNameTextView = view.findViewById(R.id.thing_name);
        roomNameTextView.setText(room.room);

        Button editButton = view.findViewById(R.id.edit_thing);
        editButton.setVisibility(View.INVISIBLE);


        Button deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(room);
            builder.create().show(); //AlertDialog wird gezeigt
            });

        return view;
    }

    @NonNull
    private AlertDialog.Builder getBuilder(TimetableRoom room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int n = 0;
        for (Lesson lesson: Timetable.getAllLessons()) {
            if (lesson.room == room.id) {
                n += 1;
            }
        }

        builder.setTitle(room.room + " löschen");
        builder.setMessage("Soll der Raum " + room.room + " wirklich gelöscht werden?\n" + n + " eingetragene Stunden mit Raum " + room.room + " werden ebenfalls gelöscht.");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            Timetable.removeRoom(room.room);

            ArrayList<Lesson> lessonsToRemove = new ArrayList<>();
            for (Lesson lesson : Timetable.getAllLessons()) {
                if (lesson.room == room.id) {
                    lessonsToRemove.add(lesson);
                }
            }
            for (Lesson lesson : lessonsToRemove) {
                Timetable.removeLesson(lesson.day, lesson.hour);
            }
            Toast.makeText(getContext(), room.room + " und die entsprechenden Stunden wurden gelöscht", Toast.LENGTH_SHORT).show();

            EditThings.updateData(getContext());
        });
        builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
        }));

        return builder;
    }
}