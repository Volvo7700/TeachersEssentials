package de.teachersessentials.csv;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;


public class CsvParser {
    // READ : Liest die angegebene Datei aus, bei Fehlern wird NULL zurückgegeben
    public static ArrayList<String[]> read(String fileName, Context context) {
        // Überprüfen, ob Eingabe Werte enthält, sonst null zurückgeben
        if (fileName != null && context != null){

            // Ausgabeobjekt definieren
            ArrayList<String[]> data = new ArrayList<>();

            try {
                // InputStream und Dateipfad definieren
                // Standardpfad: /data/data/de.teachersessentials/[Dateiname]

                // openFileInput() nimmt automatisch das files-Verzeichnis
                // Im Falle das dieses manuell benötigt wird, kann es wie folgt abgerufen werden:
                // String filePath = context.getFilesDir();

                FileInputStream fis = context.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    // Die erste Zeile überspringen, sie bildet die Überschrift
                    reader.readLine();
                    // Die Datei Zeilenweise einlesen
                    String line = reader.readLine();
                    while (line != null) {
                        // Die Zeile in einzelne Werte der Spalten auftrennen
                        String[] lineData = line.split("\\|");
                        // Die Zeile zum Ausgabeobjekt hinzufügen
                        data.add(lineData);
                        line = reader.readLine();
                    }
                }
                catch (IOException ex) {
                    // Wenn E/A-Lesefehler auftreten, null zurückgeben:
                    return null;
                }
            }

            catch (FileNotFoundException ex) {
                // Wenn Datei nicht gefunden, null zurückgeben
                return null;
            }
            catch (Exception ex) {
                return null;
            }
            finally {
                return data;
            }
        }

        else {
            return null;
        }
    }

    public static Boolean write(String fileName, ArrayList<String[]> data, String[] headers, String description, Context context) {
        // Überprüfen, ob die Eingabe Werte enthält, sonst Fehler schmeißen
        if (fileName != null && data != null && headers != null && context != null){
            // Rückgabewert definieren
            Boolean success = false;
            // Ausgabeobjekt definieren
            String output = "";

            // Dateiinhalt generieren
            // Dateiheader (Überschrift) generieren
            if (headers.length > 0) {
                // Zeilenwerte mit Komma zusammenfügen
                String line = description + " - " + headers[0];
                for (int i = 1; i < headers.length; i++) {
                    line += "|" + headers[i];
                }
                output = line;
            }
            // Wenn keine Header vorhanden sind, aufhören und beleidigt sein :P
            else return false;

            // List "zeilenweise" durchgehen
            for (String[] lineData : data) {
                // Überprüfen, ob Array Werte enthält
                if (lineData.length > 0) {
                    // Zeilenumbruch einfügen
                    String line = "\n";
                    // Zeilenwerte mit senkrechtem Strich zusammenfügen
                    line += lineData[0];
                    for (int i = 1; i < lineData.length; i++) {
                        // Da der senkrechte Strich das Trennzeichen ist, darf er in den Werten selbst nicht vorkommen und wird deshalb gelöscht.
                        lineData[i] = lineData[i].replace("|", "");
                        line += "|" + lineData[i];
                    }
                    output += line;
                }
            }

            try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
                fos.write(output.getBytes(StandardCharsets.UTF_8));
                // Weil der Vorgang offensichtlich erfolgreich war, Rückgabewert auf true setzen
                success = true;
            }
            catch (IOException e) {

            }
            catch (Exception ex)
            {

            }
            finally {
                return success;
            }
        }

        else {
            return false;
        }
    }
}