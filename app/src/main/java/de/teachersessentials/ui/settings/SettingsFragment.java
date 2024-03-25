package de.teachersessentials.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import de.teachersessentials.Fontsize;
import de.teachersessentials.R;
import de.teachersessentials.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner spinner = root.findViewById(R.id.select_font_size);
        //Liste mit Auswahlmöglichkeiten
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("winzig");
        arrayList.add("klein");
        arrayList.add("normal");
        arrayList.add("groß");
        arrayList.add("riesig");

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //jenachdem, was ausgewählt ist wird fonsize angepasst
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //auswahl winzig
                    Fontsize.fontsize = 15;
                } else if (position == 1) { //Auswahl klein
                    Fontsize.fontsize = 20;
                } else if (position == 3) { //Auswahl groß
                    Fontsize.fontsize = 30;
                } else if (position == 4) { //Auswahl riesig
                    Fontsize.fontsize = 40;
                } else { //Keine Auswahl oder normal
                    Fontsize.fontsize = 25;
                }
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