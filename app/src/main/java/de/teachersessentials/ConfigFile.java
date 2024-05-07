package de.teachersessentials;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigFile {

    private static File file;

    public static void createFile(Context context) { //erstellt neue Datei, falls noch keine existiert
        file = new File(context.getCacheDir(), "config.txt"); //neue Datei wird im cache ordner erstellt
        try {
            if (!file.exists()) { //wenn die Datei noch nicht existiert
                file.createNewFile();
                System.out.println("File created successfully: " + file.getAbsolutePath());
                writeToFile("25\n12\n14", 1, context); //Defaulteinstellungen
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());  //schon vorhanden
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage()); //Fehler
        }
    }

    public static void writeToFile(String data, int line, Context context) throws IOException {
        String dataOld = getAllData(context); //alter Inhalt wird ausgelesen
        String[] dataOldList = dataOld.split("\n"); //jede Zeile einzeln

        if (line >= 1 && line <= dataOldList.length) { //angegebene Zeile existiert im Config file
            dataOldList[line - 1] = data; //neue Daten werden eingesetzt

            StringBuilder newContent = new StringBuilder();
            for (String lineData : dataOldList) { //Liste wird wieder zusamengefügt
                newContent.append(lineData).append("\n");
            }

            try {
                Files.write(file.toPath(), newContent.toString().getBytes()); //Datei wird mit zusammengesetztem String überschrieben
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage()); //Fehler
            }
        }
    }

    public static String getConfigData(Context context, int line) { //liest alle Daten aud dem ConfigFile
        file = new File(context.getCacheDir(), "config.txt");
        String output = "";

        try {
            output = new String(Files.readAllBytes(Paths.get(file.toURI()))); //Daten werden gelesen
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage()); //Fehler
        }

        String[] contentList = output.split("\n"); //output wird am Zeilenumbruch in Liste aufgeteilt
        return contentList[line - 1]; //nur geforderte Zeile wird zurückgegeben
    }

    public static String getAllData(Context context) {
        file = new File(context.getCacheDir(), "config.txt");
        String content = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(file.toURI()))); //Daten werden gelesen
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage()); //Fehler
        }

        return content;
    }

    public static void resetConfigFile(Context context) {
            file.delete(); //löscht Datei
        createFile(context); //erstellt neue Datei
    }
}