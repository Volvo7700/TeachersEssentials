package de.teachersessentials.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.teachersessentials.Fontsize;
import de.teachersessentials.databinding.FragmentHomeBinding;
import de.teachersessentials.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView clockTextView;
    private Handler handler;
    private ProgressBar showProgress;
    private SimpleDateFormat dateFormat;
    private final ColorStateList colorRed = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.RED});
    private final ColorStateList colorBlue = new ColorStateList(new int[][] {new int[] {} }, new int[] {Color.parseColor("#1414B8")});

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        clockTextView = root.findViewById(R.id.clock_text_view);
        clockTextView.setTextSize((float) (Fontsize.fontsize*2));
        showProgress = root.findViewById(R.id.progress_bar);
        handler = new Handler();

        updateClock();

        return root;
    }

    private void updateClock() {
            //Zeit wird abgerufen
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getDefault());
        String clockText = dateFormat.format(new Date());
            //Zeit wird ins Widget gefüllt
            clockTextView.setText(clockText);
            //progress bar wird aufgefüllt
            updateProgress();
        handler.postDelayed(() -> { //handler erst hier, damit beim ersten erstellen kein delay auftritt
            //updateClock wird wieder aufgerufen, damit die Uhr jede Sekunde weitertickt
            updateClock();
        }, 1000); //Uhr updated jede Sekunde
    }

    private void updateProgress() {
        //Zeit zur nächsten vollen Stunde in millis
        long progressTime = System.currentTimeMillis() % 3600000;
        //Progress bar wird aufgefüllt bzw. leert sich
        showProgress.setProgress(3600000 - (int) progressTime);

        if (progressTime > 3300000) { //wahrscheinlich nicht effizient jedes mal neu abzufragen, müssen wir sowieso ändern, wenn eine Nachricht gesendet werden soll
            //Progress bar wird rot
            showProgress.setProgressTintList(colorRed);
            //Hier möglicherweise Benachrictigung schicken
        }
        else {
            //progress bar ist sonst blau
            showProgress.setProgressTintList(colorBlue);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
