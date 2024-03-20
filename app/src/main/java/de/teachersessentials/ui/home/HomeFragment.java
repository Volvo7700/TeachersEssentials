package de.teachersessentials.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.teachersessentials.databinding.FragmentHomeBinding;
import de.teachersessentials.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView clockTextView;
    private Handler handler;
    private ProgressBar showProgress;
    private int progressTime = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        clockTextView = root.findViewById(R.id.clock_text_view);
        showProgress = root.findViewById(R.id.progress_bar);
        handler = new Handler();

        updateClock(); //ändern: wenn Home neu geöffnet wird, dauert es eine sekunde bis die Zeit angezeigt wird

        return root;
    }
    private void updateProgress() {
        //Progress bar wird aufgefüllt
        showProgress.setProgress(progressTime);
        progressTime += 1;
        //ProgressBar wird zurückgesetz, wenn sie voll ist
        if (progressTime == showProgress.getMax()) {
            progressTime = 0;
        }
    }

    private void updateClock() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Zeit wird abgerufen
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                dateFormat.setTimeZone(TimeZone.getDefault());
                String clockText = dateFormat.format(new Date());
                //Zeit wird ins Widget gefüllt
                clockTextView.setText(clockText);
                //updateClock wird wieder aufgerufen, damit die Uhr jede Sekunde weitertickt
                updateClock();
                //progress bar wird aufgefüllt
                updateProgress();
            }
        }, 1000); //Uhr updated jede Sekunde
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}