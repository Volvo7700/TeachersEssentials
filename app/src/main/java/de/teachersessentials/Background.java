package de.teachersessentials;

import static de.teachersessentials.ui.home.HomeFragment.updateTimeAndDay;
import static de.teachersessentials.ui.home.HomeFragment.currentDay;
import static de.teachersessentials.ui.home.HomeFragment.timeInDay;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.ui.home.HomeFragment;
import de.teachersessentials.util.notifications.Notifications;

public class Background extends Service {

    private static final String TAG = "NotificationService";
    private final static int DELAY = 5000;

    private Handler handler;
    private Runnable runnable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        handler = new Handler();
        runnable = () -> Notifications.sendNotificationDEV(getApplicationContext());
        System.out.println("Started");

        //erst nach delay starten
        handler.postDelayed(runnable, DELAY);

        updateTimeAndDay();
        updateClock();

        //restarted service falls beendet wird
        return START_STICKY;
    }

/*    private Lesson getCurrentLesson() {
        int currentLesson = Timetable.getLessonNumber(timeInDay); //Aktuelle Stunde

        return Timetable.getLesson(currentDay, currentLesson);
    }*/

    private Lesson getNextLesson() {
        int currentLesson = Timetable.getLessonNumber(timeInDay); //Nächste Stunde

        return Timetable.getLesson(currentDay, currentLesson + 1);
    }

    private void updateClock() {
        updateTimeAndDay();
        updateProgress();

        System.out.println("updateClock");

        Handler handler = new Handler();
        handler.postDelayed(this::updateClock, 1000); //Uhr updated jede Sekunde
    }

    private void updateProgress() { //Progress bar wird aufgefüllt bzw. leert sich
        long timeUntilNextLesson = HomeFragment.getTimeUntilNextLesson();

        System.out.println(timeUntilNextLesson);

        //Benachrichtigung
        if (timeUntilNextLesson >= 350000 && timeUntilNextLesson <= 351000) {
            Notifications.sendNotification(getApplicationContext(), getNextLesson());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}