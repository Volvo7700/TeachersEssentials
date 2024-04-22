package de.teachersessentials.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.teachersessentials.Shared;
import de.teachersessentials.databinding.FragmentGalleryBinding;
import de.teachersessentials.R;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private Spinner selectDay;
    private String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Spinner zur Auswahl der Tage
        selectDay = root.findViewById(R.id.select_day);

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectDay.setAdapter(adapter);

        selectDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //jenachdem, was ausgewählt ist wird fonsize angepasst
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //Montag
                    Shared.fontsize = 15;
                } else if (position == 1) { //Dienstag
                    Shared.fontsize = 20;
                } else if (position == 2) { //Mittwoch
                    Shared.fontsize = 30;
                } else if (position == 3) { //Donnerstag
                    Shared.fontsize = 40;
                } else if (position == 4) //Freitag
                    Shared.fontsize = 25;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}