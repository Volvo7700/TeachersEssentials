package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import de.teachersessentials.timetable.TimetableClass;

public class ClassAdapter extends Adapter<TimetableClass> {
    private final List<TimetableClass> mClasses;

    public ClassAdapter(Context context, List<TimetableClass> classes) {
        super(context, classes);
        mClasses = classes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TimetableClass class_ = mClasses.get(position);

        thingNameTextView.setText(class_.name);

        editButton.setVisibility(View.INVISIBLE);

        buttonUp.setOnClickListener(v -> moveUpDown(1, 2, class_, position));

        buttonDown.setOnClickListener(v -> moveUpDown(-1, 2, class_, position));

        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(class_, 2);
            builder.create().show(); //AlertDialog wird gezeigt
        });



        return view;
    }
}