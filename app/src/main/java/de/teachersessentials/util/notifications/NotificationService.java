package de.teachersessentials.util.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import de.teachersessentials.MainActivity;
import de.teachersessentials.R;
import de.teachersessentials.util.ConfigFile;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    private final static int DELAY = 5000;
    private static final String CHANNEL_ID = "my_channel_01";

    private Handler handler;
    private Runnable runnable;
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //notification manager wird erstellt
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //falls version passt wird ein notification channel erstellt
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }
                }
                sendNotification();
            }
        };
        //erst nach delay starten
        handler.postDelayed(runnable, DELAY);
        //restarted service falls beendet wird
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void sendNotification() {
        //öffnet app when notification gedrückt wurde
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //notification wird erstellt
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("My Notification")
                    .setContentText("This is my notification")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        //wird gesendet
        if (ConfigFile.getConfigData(getApplicationContext(), 3) == 1) {
            notificationManager.notify(1, notification);
        }
    }
}