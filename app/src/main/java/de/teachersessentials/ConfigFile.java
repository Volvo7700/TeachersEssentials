package de.teachersessentials;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ConfigFile {

    private static File file;

    public static void createFile(Context context) { //erstellt neue Datei, falls noch keine existiert
        file = new File(context.getCacheDir(), "config.txt"); //neue Datei wird im cache ordner erstellt
        try {
            if (!file.exists()) { //wenn die Datei noch nicht existiert
                file.createNewFile();
                System.out.println("File created successfully: " + file.getAbsolutePath());
                writeToFile("25"); //Standardschriftgröße 25
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());  //schon vorhanden
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage()); //Fehler
        }
    }

    public static void writeToFile(String data) throws IOException { //löscht alles in der Datei und speichert nur neue Daten
        FileWriter fileWriter = new FileWriter(file, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.print(data); //Daten werde in der Datei gespeichert
        printWriter.close();
        fileWriter.close();

        System.out.println("Data written to file: " + file.getAbsolutePath());
    }

    public static String getConfigData(Context context) { //liest alle Daten aud dem ConfigFile
        file = new File(context.getCacheDir(), "config.txt");
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) { //Fehler
            System.err.println("Error reading file: " + e.getMessage());
        }

        return stringBuilder.toString();
    }

    public static void resetConfigFile(Context context) {
        file.delete(); //löscht Datei
        createFile(context); //erstellt neue Datei
    }
}