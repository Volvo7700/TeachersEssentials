/*
package de.teachersessentials;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Background extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            System.out.println("system running");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        ).start();

        //TODO: Verschieben der nötigen Stundentafel in Background
        //TODO: Background wird nach Reboot neu gestartet
        //TODO: Listener, dass änderungen der Einstellungen registriert werde auch, wenn die App geschlossen ist
        //TODO: richtige notifications senden lassen
        //TODO: Durch DummyBackgroundKlasse benachrichtigung entfernen

        final String CHANNEL_ID = "foreground_service";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentText("Running...")
                .setContentTitle("Foreground Service")
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        startForeground(999, notification.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
*/
