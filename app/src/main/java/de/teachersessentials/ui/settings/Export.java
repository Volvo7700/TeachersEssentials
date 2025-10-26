package de.teachersessentials.ui.settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.teachersessentials.timetable.Timetable;

public class Export {

    public static void mkExport(Context context) {
        //Download file
        final File backupsDir = new File(context.getFilesDir(), "backups");
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss", Locale.getDefault());

        //alle exporte werden gelöscht
        for (File file : Objects.requireNonNull(backupsDir.listFiles())) {
            file.delete();
        }

        File textExport = new File(backupsDir.getAbsolutePath(), "backup_" + date.format(new Date()) + ".txt");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.write(textExport.toPath(), Timetable.mkTxtFile().getBytes()); //Datei wird mit zusammengesetztem String überschrieben
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void exportEverything(Context context) {
        /*File srcDir = context.getFilesDir();
        File newDir = new File(srcDir, "export");

        File[] files = srcDir.get;

        if (!newDir.exists()) {
            newDir.mkdir();
        }*/

    }

    public static void downloadLastExport(Context context) {
        //pfad des erstellten Dokuments
        final File backupsDir = new File(context.getFilesDir(), "backups");
        File[] backups = backupsDir.listFiles();

        assert backups != null;
        File textExport = backups[backups.length - 1];

        //dokument wird ausgelesen und ausgegeben
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String decodedString = new String((Files.readAllBytes(Paths.get(textExport.toURI()))), StandardCharsets.UTF_8);
                System.out.println(decodedString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Wie und wo soll export gespeichert werden
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, textExport.getName());
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain");
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        // Get the ContentResolver
        ContentResolver resolver = context.getContentResolver();

        // Insert the file into the MediaStore
        Uri insertedUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);

        //Datei wird kopiert
        try (InputStream inputStream = new FileInputStream(textExport);
             OutputStream outputStream = resolver.openOutputStream(insertedUri)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
