package de.teachersessentials.ui.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.csv.CsvParser;
import de.teachersessentials.databinding.FragmentSettingsBinding;
import de.teachersessentials.util.ConfigFile;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Switch messages;
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

        messages = root.findViewById(R.id.messages);
        messages.setChecked(ConfigFile.getConfigData(requireActivity(), 3) == 1);

        messages.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ConfigFile.writeToFile("1", 3, requireActivity()); //ConfigFile änderung
                alertDialogDismiss();

            } else {
                ConfigFile.writeToFile("0", 3, requireActivity()); //ConfigFile änderung
                Toast.makeText(requireActivity(), R.string.benachrichtigungen_deaktiviert, Toast.LENGTH_SHORT).show();
            }
        });

        //App schließen
        Button closeApp = root.findViewById(R.id.close_app);
        closeApp.setOnClickListener(v -> {
            requireActivity().finish();
            requireActivity().finishAffinity();
        });

        //Einstellungen zurücksetzen
        Button resetSettings = root.findViewById(R.id.reset_settings);
        resetSettings.setOnClickListener(v -> {
            ConfigFile.resetConfigFile(requireActivity());
            selectFontSize.setSelection(2);
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                messages.setChecked(true);
            } else {
                messages.setChecked(false);
                ConfigFile.writeToFile("0", 3, requireActivity());
            }
        });


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
                if (saveContent.getText() != null){
                    // Text in Zeilen aufteilen
                    List<String> lines = Arrays.asList(saveContent.getText().toString().split("\n"));

                    // Die ersten beiden Zeilen rausnehmen und extra vorbereiten als Titel und Headerarray
                    String title = lines.get(0);
                    String[] headers = lines.get(1).split("\\|");
                    String[] linesarr = (String[]) Arrays.copyOfRange(lines.toArray(), 2, lines.size());

                    ArrayList<String[]> data = new ArrayList<>();
                    for (String line : linesarr) {
                        String[] values = line.split("\\|");
                        data.add(values);
                    }
                    CsvParser.write(savefilename.getText().toString(), data, headers, title, getContext());
                }
            }
        });

        Button seperatorbutton = (Button) root.findViewById(R.id.button_seperator);

        seperatorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s0 = saveContent.getSelectionStart();
                int s1 = saveContent.getSelectionEnd();
                saveContent.setText(saveContent.getText().insert(s1, "|"));
                saveContent.setSelection(s1 + 1);
            }
        });

        return root;
    }

    private void alertDialogDismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.alertPermissionHeadline);
                builder.setMessage(R.string.alertPermissionText);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    //Weiterleitung in die Einstellungen
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });
                builder.setNegativeButton("Abbrechen", (dialog, which) -> messages.setChecked(false));
                builder.create().show(); //AlertDialog wird gezeigt
            }
        }
    }

    private void writeFontsizeToConfig(String fontsize) {
        ConfigFile.writeToFile(fontsize, 1, requireActivity()); //Schriftgröße ins ConfigFile
        System.out.println("Written succesfully");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}