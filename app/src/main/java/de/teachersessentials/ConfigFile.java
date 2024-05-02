package de.teachersessentials;

import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ConfigFile {

    public static void createFile(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName); //neue Datei wird im cache ordner erstellt
        try {
            if (!file.exists()) { //wenn die Datei noch niht existiert
                file.createNewFile();
                System.out.println("File created successfully: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());  //schon vorhanden
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage()); //Fehler
        }
    }

    public static void writeToFile(File file, String data) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(data);
        printWriter.close();
        fileWriter.close();
        System.out.println("Data written to file: " + file.getAbsolutePath());
    }
}