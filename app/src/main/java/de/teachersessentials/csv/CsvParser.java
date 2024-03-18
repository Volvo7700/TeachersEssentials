package de.teachersessentials.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.teachersessentials.timetable.Lesson;

public class CsvParser {
    private void Read() {
        InputStream is;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                int id = Integer.parseInt(RowData[0]);
                int day = Integer.parseInt(RowData[0]);
                int lesson = Integer.parseInt(RowData[0]);
                int subject = Integer.parseInt(RowData[0]);
                int class_ = Integer.parseInt(RowData[0]);
                int room = Integer.parseInt(RowData[0]);

                Lesson lesson1 = new Lesson(id, day, lesson, subject, class);
                ArrayList<Lesson> LessonStor = new ArrayList<Lesson>;
                LessonStor.add(lesson1);
                String value = RowData[1];
                int temp = Integer.parseInt(value);

                // do something with "data" and "value"
            }
        }
        catch (IOException ex) {
            // handle exception
        }
    }
}
