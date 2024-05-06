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


import java.io.IOException;

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

        //Spinner zur Auswahl der Fontsize
        Spinner selectFontSize = root.findViewById(R.id.select_font_size);

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, font_select_size);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectFontSize.setAdapter(adapter);
        selectFontSize.setSelection(2); //normale Auswahl

        selectFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //jenachdem, was ausgewählt ist wird fonsize im ConfigFile angepasst
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //auswahl winzig
                    writeFontsizeToConfig("15");
                } else if (position == 1) { //Auswahl klein
                    writeFontsizeToConfig("20");
                } else if (position == 3) { //Auswahl groß
                    writeFontsizeToConfig("30");
                } else if (position == 4) { //Auswahl riesig
                    writeFontsizeToConfig("40");
                } else { //Keine Auswahl oder normal
                    writeFontsizeToConfig("25");
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

        //Einstellungen zurücksetzen
        Button resetSettings = root.findViewById(R.id.reset_settings);
        resetSettings.setOnClickListener(v -> ConfigFile.resetConfigFile(requireActivity()));

        return root;
    }

    private void writeFontsizeToConfig(String fontsize) {
        try {
            ConfigFile.writeToFile(fontsize); //Schriftgröße ins ConfigFile
            System.out.println("Written succesfully");
        } catch (IOException e) { //Fehler
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}