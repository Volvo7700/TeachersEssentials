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
import de.teachersessentials.timetable.TimetableSubject;

public class SubjectAdapter extends ArrayAdapter<TimetableSubject> {
    private Context mContext;
    private List<TimetableSubject> mSubjects;

    public SubjectAdapter(Context context, List<TimetableSubject> subjects) {
        super(context, 0, subjects);
        mContext = context;
        mSubjects = subjects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.thing_list_item_edit, parent, false);
        }

        TimetableSubject subject = mSubjects.get(position);

        //Name Fach wird links angezeigt
        TextView subjectNameTextView = view.findViewById(R.id.thing_name);
        subjectNameTextView.setText(subject.name);

        //Button zum bearbeiten eines Fachs
        Button editButton = view.findViewById(R.id.edit_thing);
        editButton.setOnClickListener(v -> {

        });

        //Button zum löschen eines Fachs
        Button deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {

        });

        return view;
    }
}