package de.teachersessentials.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.io.IOException;
import java.util.ArrayList;

import de.teachersessentials.R;
import de.teachersessentials.csv.CsvParser;
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


        // Temporärer Testcode

        // Einfacher Datenbanktest (laden und speichern von ein paar simplen Testdaten automatisiert)
        Button testbutton = (Button) root.findViewById(R.id.button_test);
        testbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textView_test = (TextView) root.findViewById(R.id.textView_test);
                ArrayList<String[]> data = new ArrayList<>();
                String[] line0 = new String[4];
                line0[0] = "hallo";
                line0[1] = "test";
                line0[2] = "hier";
                line0[3] = "CSV";
                data.add(line0);
                String[] line1 = new String[4];
                line1[0] = "und eine supertolle";
                line1[1] = "zweite Zeile";
                line1[2] = "mit Sonderzeichen:";
                line1[3] = "!\"§$%&/()=?²³{[]}\\@€+*~#'-_,.;:<>|^°";
                data.add(line1);

                String[] headers = new String[1];
                headers[0] = "test";

                Boolean saveresult = CsvParser.write("test.csv", data, headers, "Testtabelle", getContext());
                textView_test.setText("Speichervorgang erfolgreich: " + saveresult.toString());

                ArrayList<String[]> loadedData = CsvParser.read("test.csv", getContext());

                if (loadedData != null) {
                    for (String[] row : loadedData) {
                        String text = "";
                        for (String s : row)
                        {
                            text += " | ";
                            text += s;
                        }
                        textView_test.setText(textView_test.getText() + "\n" + text);
                    }
                }
                else {
                    textView_test.setText(textView_test.getText() + "\n" + "NULL");
                }
            }
        });


        // Manuelles Laden und Speichern von Daten
        // Objekte definieren
        Button loadbutton = (Button) root.findViewById(R.id.button_loadDev);
        EditText loadfilename = (EditText) root.findViewById(R.id.editText_loadDev);
        TextView loadContent = (TextView) root.findViewById(R.id.textView_loadDevContent);

        Button savebutton = (Button) root.findViewById(R.id.button_saveDev);
        EditText savefilename = (EditText) root.findViewById(R.id.editText_saveDev);
        EditText saveContent = (EditText) root.findViewById(R.id.editText_saveDevContent);

        // Aktion beim Laden
        loadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String[]> content = CsvParser.read(loadfilename.getText().toString(), getContext());

                // Text ins Interface schreiben
                loadContent.setText("");
                if (content != null) {
                    for (String[] row : content) {
                        String text = "";
                        for (String s : row)
                        {
                            text += " | ";
                            text += s;
                        }
                        loadContent.setText(loadContent.getText() + "\n" + text);
                    }
                }
                else {
                    loadContent.setText(loadContent.getText() + "\n" + "NULL");
                }
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Hier noch nen CSV-Speicheraufruf schreiben, mit Pipe als Trennsymbol
                //String[] lineData = line.split("\\|");
            }
        });

        return root;
    }

    private void writeFontsizeToConfig(String fontsize) {
        try {
            ConfigFile.writeToFile(fontsize, 1, requireActivity()); //Schriftgröße ins ConfigFile
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