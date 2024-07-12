package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import de.teachersessentials.R;

import de.teachersessentials.timetable.TimetableRoom;

public class RoomAdapter extends Adapter<TimetableRoom> {
    private final List<TimetableRoom> mRooms;

    public RoomAdapter(Context context, List<TimetableRoom> rooms) {
        super(context, rooms);
        mRooms = rooms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TimetableRoom room = mRooms.get(position);

        thingNameTextView.setText(room.room);

        editButton.setVisibility(View.INVISIBLE);

        buttonUp.setOnClickListener(v -> moveUpDown(1, 1, room, position));

        buttonDown.setOnClickListener(v -> moveUpDown(-1, 1, room, position));

        deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(room, 1);
            builder.create().show(); //AlertDialog wird gezeigt
            });

        return view;
    }
}