package de.teachersessentials.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.teachersessentials.timetable.Lesson;

public class CsvParser {
    private static ArrayList<String[]> read(String path) {
        // InputStream und Dateipfad definieren
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // Zweidimensionales Ausgangsobjekt definieren
        ArrayList<String[]> data = new ArrayList<>();

        try {
            String line;
            // Die Datei Zeilenweise einlesen
            while ((line = reader.readLine()) != null) {
                // Jede Zeile in einzelne Werte der Spalten auftrennen
                String[] rowData = line.split(",");
                //
                data.add(rowData);
            }
        }
        // Wenn E/A-Lesefehler auftreten, hier fangen und reagieren:
        catch (IOException ex) {
            // handle exception
        }
        return data;
    }
}