package de.teachersessentials.ui.settings.editthings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.timetable.TimetableClass;
import de.teachersessentials.timetable.TimetableRoom;
import de.teachersessentials.timetable.TimetableSubject;

public class Adapter<T> extends ArrayAdapter<T> {
    private final Context mContext;
    TextView thingNameTextView;
    ImageView buttonDown;
    ImageView buttonUp;
    ImageButton editButton;
    ImageButton deleteButton;

    public Adapter(Context context, List<T> content) {
        super(context, 0, content);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.thing_list_item_edit, parent, false);
        }

        //T content_ = mContent.get(position);

        //Name Fach wird links angezeigt
        thingNameTextView = view.findViewById(R.id.thing_name);

        //Sachen nach oben/unten verschieben
        buttonDown = view.findViewById(R.id.button_dwon);

        buttonUp = view.findViewById(R.id.button_up);

        //Button zum bearbeiten
        editButton = view.findViewById(R.id.edit_thing);

        //Button zum löschen
        deleteButton = view.findViewById(R.id.delete_thing);

        return view;
    }

    @NonNull
    public AlertDialog.Builder getBuilder(T content, int index) {
        //Daten von content werden je nach indey ausgelesen
        Object[] data = getData(content, index);
        String name = (String) data[0];
        int id = (int) data[1];
        String type = "";
        switch (index) {
            case 0:
                type = "das Fach ";
                break;
            case 1:
                type = "der Raum ";
                break;
            case 2:
                type = "die Klasse ";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Zu entfernende Stunden werden gesucht
        ArrayList<Lesson> lessonsToRemove = new ArrayList<>();
        for (Lesson lesson : Timetable.getAllLessons()) {
            if (ofLessonGetContentId(lesson, index) == id) {
                lessonsToRemove.add(lesson);
            }
        }

        builder.setTitle(name + " löschen");

        int n = lessonsToRemove.size();
        if (n == 0) {
            //keine Stunde mit entsprechenderder Eintragung vorhanden
            builder.setMessage("Soll " + type + name + " wirklich gelöscht werden?");
        } else if (n == 1) {
            //eine Stunde vorhanden
            builder.setMessage("Soll " + type + name + " wirklich gelöscht werden?\nEine eingetragene Stunde mit " + type + name + " wird ebenfalls gelöscht.");
        } else {
            //mehrer Stunden vorhanden
            builder.setMessage("Soll " + type + name + " wirklich gelöscht werden?\n" + n + " eingetragene Stunden mit " + type + name + " werden ebenfalls gelöscht.");
        }

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            //Je nach indexx werden die jeweiligen Daten gelöscht
            switch (index) {
                case 0:
                    Database.subjects.remove(content);
                    break;
                case 1:
                    Database.rooms.remove(content);
                    break;
                case 2:
                    Database.classes.remove(content);
                    break;
            }

            for (Lesson lesson : lessonsToRemove) {
                Database.lessons.remove(lesson);
            }
            Database.save(getContext());

            //Daten werden upgedated
            EditThings.updateDataSingle(getContext(), index);

            //resume Methode: Größe und Content der ListViews
            EditThings.resume();

            Toast.makeText(mContext, name + " und die entsprechenden Stunden wurden gelöscht", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
        }));

        return builder;
    }

    public void moveUpDown(int upDown, int index, List<T> contentAll, int position) {
        //Sache, die verschoben werden soll
        T content = contentAll.get(position);

        //Sache wird aus Liste entfernt
        contentAll.remove(content);

        int newPosition = position - upDown;
        //newPosition ist absolute Position, an der content wieder eingefügt wird
        while (newPosition < 0 || newPosition > contentAll.size()) {
            if (upDown > 0) {
                newPosition++;
            } else {
                newPosition--;
            }
        }

        //wird wieder an neuer Position eingefügt
        contentAll.add(newPosition, content);
        //Updates von Database und EditThings
        writeToDatabase(index, (ArrayList<T>) contentAll);
        Database.save(getContext());
        EditThings.updateDataSingle(getContext(), index);
    }

    private Object[] getData(T content, int index) {
        switch (index) {
            case 0: {
                TimetableSubject subject = (TimetableSubject) content;
                return new Object[] {subject.name, subject.id, subject.shortage};
            }
            case 1: {
                TimetableRoom room = (TimetableRoom) content;
                return new Object[] {room.room, room.id};
            }
            case 2: {
                TimetableClass class_ = (TimetableClass) content;
                return new Object[] {class_.name, class_.id};
            } default:
                return null;
        }
    }

    private int ofLessonGetContentId(Lesson lesson, int index) {
        switch (index) {
            case 0: {
                return lesson.subject;
            }
            case 1: {
                return lesson.room;
            }
            case 2: {
                return lesson.class_;
            } default:
                return -1;
        }
    }

    private void writeToDatabase(int index, ArrayList<T> data) {
        switch (index) {
            case 0:
                Database.subjects = (ArrayList<TimetableSubject>) data;
                break;
            case 1:
                Database.rooms = (ArrayList<TimetableRoom>) data;
                break;
            case 2:
                Database.classes = (ArrayList<TimetableClass>) data;
                break;
        }
    }
}