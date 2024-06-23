package de.teachersessentials;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import de.teachersessentials.databinding.ActivityMainBinding;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.util.ConfigFile;
import de.teachersessentials.util.notifications.NotificationService;
import de.teachersessentials.util.notifications.notifications;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigFile.createFile(this); //config File wird erstellt

        if (ConfigFile.getConfigData(this, 4) == 1) {
            Database.generateDefaults(this);
        }

        //TODO daten werden aus irgendeinem grund immer beim schließen der App gelöscht

        ConfigFile.writeToFile("0", 4, this); //App wurde bereits einmal geöffnet

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        notifications.askPermission(this, this); //fragt nach Erlaubnis
        notifications.createNotificationChannel(this); //NotificationChannel wird erstellt

        //if(!foregroundServiceRunning()) {
          //  Intent serviceIntent = new Intent(this, Background.class);
            //startForegroundService(serviceIntent);
        //}

        Database.load(this);
    }


    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(Background.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //sagt dem Config File ob Berechtigung gegeben wurde oder nicht
        if (requestCode == notifications.REQUEST_POST_NOTIFICATIONS) { //nur wenn es um Berechtigung für Nachrichtengeht
            int granted = grantResults[0];
            ConfigFile.writeToFile(String.valueOf(granted + 1), 3, this); //ConfigFile wird entsprechend der Auswahl geändert
        }
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.benachrichtigungen_deaktiviert, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStop(){
        super.onStop();
        startService(new Intent(this, NotificationService.class)); //activiert nachricht schicken
        finish(); //beendet app
        Database.save(this);
    }
}