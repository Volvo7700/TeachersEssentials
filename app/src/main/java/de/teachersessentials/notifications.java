package de.teachersessentials;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class notifications {
    private static final String CHANNEL_ID = "channel";

    public static void sendNotification(Context context) {
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
        int notificationId = 12345; //einzigartige ID TODO: ändern
        notificationManager.notify(notificationId, builder.build());
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
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
