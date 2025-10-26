package de.teachersessentials.util.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import de.teachersessentials.MainActivity;
import de.teachersessentials.R;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.util.ConfigFile;

public class Notifications {
    private static final String CHANNEL_ID = "channel";
    public static final int REQUEST_POST_NOTIFICATIONS = 100;
    private static NotificationManager notificationManager;

    /*public static void sendNotification(Context context) {
        if(ConfigFile.getConfigData(context, 3) == 1) { //Nachricht wird nur gesendet, wenn in den Einstellungen aktiviert

            //Weiterleitung zu App bei klick auf die Nachricht
            Intent intent = new Intent(context, AlertDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    //Eigenschaften der Nachricht
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) //was passiert, wenn die Nachricht ageklickt wird
                    .setAutoCancel(true);

            //NotificationManager wird erstellt
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Nachricht wird tatsächlich ans Handy geschickt
            int notificationId = 12346; //einzigartige ID TODO: ändern
            notificationManager.notify(notificationId, builder.build());
        }
    }*/

    public static void sendNotification(Context context, Lesson nextLesson) {
        NotificationManager existingNotificationManager = notificationManager;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("Passt");
            //öffnet app when notification gedrückt wurde
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            //notification wird erstellt
            Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle("Stunde endet in 5 Minuten")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent);

            try {
                builder.setContentText("Nächste Stunde: " + Timetable.getSubjectById(nextLesson.subject).name + " in der Klasse " +
                        Timetable.getClassById(nextLesson.class_).name + " in Raum " + Timetable.getRoomById(nextLesson.room).room);
            } catch (NullPointerException e) {
                builder.setContentText("Nächste Stunde: ");
            }

            Notification notification = builder.build();

            //wird gesendet, wenn benaachrichtigungen aktiviert
            if (ConfigFile.getConfigData(context, 3) == 1) {
                notificationManager.notify(1, notification);
            }
        }
    }

    public static void sendNotificationDEV(Context context) {
        //öffnet app when notification gedrückt wurde
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //notification wird erstellt
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle("Background")
                    .setContentText("[DEV] Ausprobieren")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        //wird gesendet, wenn benachrichtigungen aktiviert
        if (ConfigFile.getConfigData(context, 3) == 1) {
            notificationManager.notify(1, notification);
        }
    }

    public static void createNotificationChannel(Context context) {
        //NotificationChannel, über den die Nachricht verschickt wird
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Integer.toString(R.string.channel_name);
            String description = Integer.toString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            //Channel wird im System registriert
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void askPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) { //Wenn keine Berechtigung erteilt ist
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_POST_NOTIFICATIONS);
            }
        }
    }
}
