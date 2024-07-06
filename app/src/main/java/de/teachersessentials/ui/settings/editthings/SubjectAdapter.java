package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.content.Intent;
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
import de.teachersessentials.timetable.TimetableSubject;
import de.teachersessentials.ui.gallery.PopUpAdd;

public class SubjectAdapter extends ArrayAdapter<TimetableSubject> {
    private final Context mContext;
    private final List<TimetableSubject> mSubjects;

    public SubjectAdapter(Context context, List<TimetableSubject> subjects) {
        super(context, 0, subjects);
        mContext = context;
        mSubjects = subjects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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
            Context context = getContext();
            Intent intent = new Intent(context, PopUpAdd.class);
            intent.putExtra("addId", R.id.add_subject);
            intent.putExtra("header", subject.name);
            intent.putExtra("shortage", subject.shortage);
            context.startActivity(intent);
        });

        //Button zum löschen eines Fachs
        Button deleteButton = view.findViewById(R.id.delete_thing);
        deleteButton.setOnClickListener(v -> {
            //AlertDialog, ob Raum wirklich gelöscht werden soll
            AlertDialog.Builder builder = getBuilder(subject);
            builder.create().show(); //AlertDialog wird gezeigt
        });

        return view;
    }

    @NonNull
    private AlertDialog.Builder getBuilder(TimetableSubject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int n = 0;
        for (Lesson lesson: Timetable.getAllLessons()) {
            if (lesson.subject == subject.id) {
                n += 1;
            }
        }

        builder.setTitle(subject.name + " löschen");
        builder.setMessage("Soll das Fach " + subject.name + " wirklich gelöscht werden?\n" + n + " eingetragene Stunden mit Fach " + subject.name + " werden ebenfalls gelöscht.");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            Timetable.removeSubject(subject.shortage);

            ArrayList<Lesson> lessonsToRemove = new ArrayList<>();
            for (Lesson lesson : Timetable.getAllLessons()) {
                if (lesson.subject == subject.id) {
                    lessonsToRemove.add(lesson);
                }
            }
            for (Lesson lesson : lessonsToRemove) {
                Timetable.removeLesson(lesson.day, lesson.hour);
            }
            Toast.makeText(getContext(), subject.name + " und die entsprechenden Stunden wurden gelöscht", Toast.LENGTH_SHORT).show();

            EditThings.updateData(getContext());
        });
        builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
        }));

        return builder;
    }
}