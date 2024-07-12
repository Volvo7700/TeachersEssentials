package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.TimetableSubject;
import de.teachersessentials.ui.gallery.PopUpAdd;

public class SubjectAdapter extends Adapter<TimetableSubject> {
    private final Context mContext;
    private final List<TimetableSubject> mSubjects;

    public SubjectAdapter(Context context, List<TimetableSubject> subjects) {
        super(context, subjects);
        mContext = context;
        mSubjects = subjects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TimetableSubject subject = mSubjects.get(position);

        //Name Fach wird links angezeigt
        thingNameTextView.setText(subject.name);
        //thingNameTextView.setTextColor(Color.parseColor(String.format("#%06X", subject.color)));

        //Button zum bearbeiten eines Fachs
        editButton = view.findViewById(R.id.edit_thing);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PopUpAdd.class);
            intent.putExtra("addId", R.id.add_subject);
            intent.putExtra("header", subject.name);
            intent.putExtra("shortage", subject.shortage);
            mContext.startActivity(intent);
        });

        buttonUp.setOnClickListener(v -> moveUpDown(1, 0, subject, position));

        buttonDown.setOnClickListener(v -> moveUpDown(-1, 0, subject, position));

        //Button zum löschen eines Fachs
        deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(subject, 0);
            builder.create().show(); //AlertDialog wird gezeigt
        });

        return view;
    }
}