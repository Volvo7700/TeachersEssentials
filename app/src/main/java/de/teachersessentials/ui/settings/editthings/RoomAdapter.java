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

import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.TimetableRoom;

public class RoomAdapter extends ArrayAdapter<TimetableRoom> {
    private Context mContext;
    private List<TimetableRoom> mRooms;

    public RoomAdapter(Context context, List<TimetableRoom> rooms) {
        super(context, 0, rooms);
        mContext = context;
        mRooms = rooms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.thing_list_item_edit, parent, false);
        }

        TimetableRoom room = mRooms.get(position);

        TextView roomNameTextView = view.findViewById(R.id.thing_name);
        roomNameTextView.setText(room.room);

        Button editButton = view.findViewById(R.id.edit_thing);
        editButton.setOnClickListener(v -> {
            // Handle edit button click
            Toast.makeText(mContext, "Edit button clicked for room " + room.room, Toast.LENGTH_SHORT).show();
        });

        Button deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            Toast.makeText(mContext, "Delete button clicked for room " + room.room, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}