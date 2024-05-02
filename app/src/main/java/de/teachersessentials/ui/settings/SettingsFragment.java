package de.teachersessentials.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import de.teachersessentials.Shared;
import de.teachersessentials.R;
import de.teachersessentials.databinding.FragmentSettingsBinding;
import de.teachersessentials.ConfigFile;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private final String[] font_select_size = {"winzig", "klein", "normal", "groß", "riesig"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner selectFontSize = root.findViewById(R.id.select_font_size);

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, font_select_size);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectFontSize.setAdapter(adapter);
        selectFontSize.setSelection(2);

        selectFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //jenachdem, was ausgewählt ist wird fonsize angepasst
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //auswahl winzig
                    Shared.fontsize = 15;
                } else if (position == 1) { //Auswahl klein
                    Shared.fontsize = 20;
                } else if (position == 3) { //Auswahl groß
                    Shared.fontsize = 30;
                } else if (position == 4) { //Auswahl riesig
                    Shared.fontsize = 40;
                } else { //Keine Auswahl oder normal
                    Shared.fontsize = 25;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //App schließen
        Button closeApp = root.findViewById(R.id.close_app);
        closeApp.setOnClickListener(v -> {
            requireActivity().finish();
            requireActivity().finishAffinity();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}