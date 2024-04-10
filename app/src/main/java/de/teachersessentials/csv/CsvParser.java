package de.teachersessentials.csv;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.teachersessentials.ApplicationContext;
import de.teachersessentials.timetable.Lesson;
import android.os.Bundle;
import android.view.Menu;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import de.teachersessentials.databinding.ActivityMainBinding;




public class CsvParser {
    public static ArrayList<String[]> read(String fileName) {
        // Ausgabeobjekt definieren
        ArrayList<String[]> data = new ArrayList<>();

        try {
            // InputStream und Dateipfad definieren
            // Standardpfad: /storage/.../Android/de.teachersessentials/[Dateiname]
            String filePath = ApplicationContext.getAppContext().getApplicationInfo().dataDir + "fileName";
            FileInputStream fis = ApplicationContext.getAppContext().openFileInput(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                // Die erste Zeile überspringen, sie bildet die Überschrift
                reader.readLine();
                // Die Datei Zeilenweise einlesen
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    // Die Zeile in einzelne Werte der Spalten auftrennen
                    String[] lineData = line.split(",");
                    // Die Zeile zum Ausgabeobjekt hinzufügen
                    data.add(lineData);
                }
            }
            catch (IOException ex) {
                // Wenn E/A-Lesefehler auftreten, null zurückgeben:
                data = null;
            }
        }

        catch (FileNotFoundException ex) {
            // Wenn Datei nicht gefunden, null zurückgeben
            data = null;
        }
        finally {
            return data;
        }
    }

    public static Boolean save(String fileName, ArrayList<String[]> data, String[] headers, String description) {
        // Rückgabewert definieren
        Boolean success = false;
        // Ausgabeobjekt definieren
        String output = "";

        // Dateiinhalt generieren
        // Dateiheader (Überschrift) generieren
        if (headers.length > 0) {
            // Zeilenwerte mit Komma zusammenfügen
            String line = String.format("{0} - {1}", description, headers[0]);
            for (int i = 1; i < headers.length; i++) {
                line += ", " + headers[i];
            }
            output = line;
        }

        // List "zeilenweise" durchgehen
        for (String[] lineData : data) {
            // Überprüfen, ob Array Werte enthält
            if (lineData.length > 0) {
                // Zeilenumbruch einfügen
                String line = System.getProperty("line.seperator");
                // Zeilenwerte mit Komma zusammenfügen
                line += lineData[0];
                for (int i = 1; i < lineData.length; i++) {
                    line += ", " + lineData[i];
                }
                output += line;
            }
        }

        try (FileOutputStream fos = ApplicationContext.getAppContext().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(output.getBytes(StandardCharsets.UTF_8));
            // Weil der Vorgang erfolgreich war, Rückgabewert auf true setzen
            success = true;
        }
        catch (FileNotFoundException e) {

        }
        catch (IOException e) {

        }
        finally {
            return success;
        }
    }
}