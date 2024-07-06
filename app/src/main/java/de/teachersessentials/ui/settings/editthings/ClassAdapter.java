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
import de.teachersessentials.timetable.TimetableClass;

public class ClassAdapter extends ArrayAdapter<TimetableClass> {
    private final Context mContext;
    private final List<TimetableClass> mClasses;

    public ClassAdapter(Context context, List<TimetableClass> classes) {
        super(context, 0, classes);
        mContext = context;
        mClasses = classes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.thing_list_item_edit, parent, false);
        }

        TimetableClass class_ = mClasses.get(position);

        //Name Fach wird links angezeigt
        TextView subjectNameTextView = view.findViewById(R.id.thing_name);
        subjectNameTextView.setText(class_.name);

        //Button zum bearbeiten wird nict angezeigt
        Button editButton = view.findViewById(R.id.edit_thing);
        editButton.setVisibility(View.INVISIBLE);

        //Button zum löschen eines Fachs
        Button deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(class_);
            builder.create().show(); //AlertDialog wird gezeigt
        });

        return view;
    }

    @NonNull
    private AlertDialog.Builder getBuilder(TimetableClass class_) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int n = 0;
        for (Lesson lesson : Timetable.getAllLessons()) {
            if (lesson.class_ == class_.id) {
                n += 1;
            }
        }

        builder.setTitle(class_.name + " löschen");
        builder.setMessage("Soll die Klasse " + class_.name + " wirklich gelöscht werden?\n" + n + " eingetragene Stunden mit Klasse " + class_.name + " werden ebenfalls gelöscht.");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            Timetable.removeClass(class_.name);

            ArrayList<Lesson> lessonsToRemove = new ArrayList<>();
            for (Lesson lesson : Timetable.getAllLessons()) {
                if (lesson.class_ == class_.id) {
                    lessonsToRemove.add(lesson);
                }
            }
            for (Lesson lesson : lessonsToRemove) {
                Timetable.removeLesson(lesson.day, lesson.hour);
            }
            Toast.makeText(getContext(), class_.name + " und die entsprechenden Stunden wurden gelöscht", Toast.LENGTH_SHORT).show();

            EditThings.updateData(getContext());
        });
        builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
        }));

        return builder;
    }
}