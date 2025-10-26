package de.teachersessentials.ui.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.teachersessentials.R;
import de.teachersessentials.csv.CsvParser;
import de.teachersessentials.databinding.FragmentSettingsBinding;
import de.teachersessentials.timetable.Database;
import de.teachersessentials.timetable.Lesson;
import de.teachersessentials.timetable.Timetable;
import de.teachersessentials.ui.settings.editthings.EditThings;
import de.teachersessentials.util.ConfigFile;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Switch messages;
    TextView textMessagesPermission;
    private final String[] font_select_size = {"winzig", "klein", "normal", "groß", "riesig"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Spinner zur Auswahl der Fontsize
        Spinner selectFontSize = root.findViewById(R.id.select_font_size);

        //Layout, in dem die Schriftröße ausgewählt wird
        RelativeLayout layoutFontsize = root.findViewById(R.id.layout_fontsize);
        layoutFontsize.setOnClickListener(v -> selectFontSize.performClick());

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, font_select_size);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectFontSize.setAdapter(adapter);

        //Automatische Auswahl der Schriftgröße
        int[] fontsizes = {15, 20, 25, 30, 40};
        for (int f = 0; f <= 4; f += 1) {
            if (fontsizes[f] == ConfigFile.getConfigData(requireActivity(), 1)) {
                selectFontSize.setSelection(f);
            }
        }

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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //ConstarintLayout in dem die Messages drin sind
        ConstraintLayout layoutMessages = root.findViewById(R.id.layout_messages);
        layoutMessages.setOnClickListener(v -> messages.setChecked(!messages.isChecked())); //switch ist auf ganzem Layout cickbar

        //Text zum Anzeigen, ob Berechtigung da ist
        textMessagesPermission = root.findViewById(R.id.text_messages_permission);

        messages = root.findViewById(R.id.messages);
        messages.setChecked(ConfigFile.getConfigData(requireActivity(), 3) == 1);

        messages.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { //Aktiviert
                ConfigFile.writeToFile("1", 3, requireActivity()); //ConfigFile Änderung
                //Wenn kein Berechtigung vorhanden ist, muss nachgefragt werden
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    alertDialogDismiss();
                } //else {
                    //Toast.makeText(requireActivity(), "Benachrichtigungen aktiviert", Toast.LENGTH_SHORT).show();
                //}
            } else { //Deaktiviert
                ConfigFile.writeToFile("0", 3, requireActivity()); //ConfigFile Änderung
                //Toast.makeText(requireActivity(), R.string.benachrichtigungen_deaktiviert, Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            textMessagesPermission.setText("Berechtigung bereits erteilt");
        } else {
            textMessagesPermission.setText("Keine Berechtigung");
            ConfigFile.writeToFile("0", 3, requireActivity()); //ConfigFile Änderung
            messages.setChecked(false);
        }

        LinearLayout editThings = root.findViewById(R.id.edit_things);
        editThings.setOnClickListener(v -> {
            //Activity zum Bearbeiten der Voreinstellungen
            Intent intent = new Intent(getActivity(), EditThings.class);
            startActivity(intent);
        });

        //Einstellungen zurücksetzen
        TextView resetSettings = root.findViewById(R.id.reset_settings);
        resetSettings.setOnClickListener(v -> {
            ConfigFile.resetConfigFile(requireActivity());
            selectFontSize.setSelection(2);
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                messages.setChecked(true);
            } else {
                messages.setChecked(false);
                ConfigFile.writeToFile("0", 3, requireActivity());
            }
            Toast.makeText(requireActivity(), "Einstellungen zurückgesetzt", Toast.LENGTH_SHORT).show();
        });

        TextView export = root.findViewById(R.id.export);
        export.setOnClickListener(v -> {
            //Export wird erstellt
            Export.mkExport(requireContext());
        });

        //App schließen
        TextView closeApp = root.findViewById(R.id.close_app);
        closeApp.setOnClickListener(v -> {
            requireActivity().finish();
            requireActivity().finishAffinity();
        });

        TextView openSettings = root.findViewById(R.id.open_settings);
        openSettings.setOnClickListener(v -> {
            //Weiterleitung in die Einstellungen
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Manuelles Laden und Speichern von Daten
        // Objekte definieren
        Button loadbutton = root.findViewById(R.id.button_loadDev);
        EditText loadfilename = root.findViewById(R.id.editText_loadDev);
        TextView loadContent = root.findViewById(R.id.textView_loadDevContent);

        Button savebutton = root.findViewById(R.id.button_saveDev);
        EditText savefilename = root.findViewById(R.id.editText_saveDev);
        EditText saveContent = root.findViewById(R.id.editText_saveDevContent);

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
                        for (String s : row) {
                            text += " | ";
                            text += s;
                        }
                        loadContent.setText(loadContent.getText() + "\n" + text);
                    }
                } else {
                    loadContent.setText(loadContent.getText() + "\n" + "NULL");
                }
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Hier noch nen CSV-Speicheraufruf schreiben, mit Pipe als Trennsymbol
                //String[] lineData = line.split("\\|");
                if (saveContent.getText() != null) {
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

        Button seperatorbutton = root.findViewById(R.id.button_seperator);

        seperatorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s0 = saveContent.getSelectionStart();
                int s1 = saveContent.getSelectionEnd();
                saveContent.setText(saveContent.getText().insert(s1, "|"));
                saveContent.setSelection(s1 + 1);
            }
        });

        //Standarddaten hinzufügen
        TextView addDefaultsButton = root.findViewById(R.id.button_AddDefaults);
        addDefaultsButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

            builder.setTitle("Standarddaten zurücksetzen");
            builder.setMessage("Die eingestellten Fächer, Räume und Klassen werden zurückgesetzt. Dabei werden benutzerdefinierte Änderungen nicht übernommen und evt. auch Stunden gelöscht.");
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                //Daten werden zurükgestzt
                Database.generateDefaults(getContext());
                Database.load(getContext());
                Toast.makeText(getContext(), "Standarddaten wurden hinzugefügt", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Abbrechen", ((dialog, which) -> {
            }));
            builder.create().show();
        });

        //Button zum löschen des gesamten Stundenplans
        TextView removeEntireTable = root.findViewById(R.id.delete_entire_table);
        removeEntireTable.setOnClickListener(v -> {
            ArrayList<Lesson> allLessons = Timetable.getAllLessons();

            if (!allLessons.isEmpty()) {
                //AlertDialog zum sichergehen
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Stundenplan löschen");
                builder.setMessage("Der gesamte eingetragene Stundenplan wird gelöscht");
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    //Kopie nötig wegen ConcurrentModificationException
                    ArrayList<Lesson> allLessons_ = (ArrayList<Lesson>) allLessons.clone();
                    for (Lesson lesson : allLessons_) {
                        //jede Studnde wird einzeln gelöscht
                        Database.lessons.remove(lesson);
                    }
                    Database.save(requireActivity());
                    Toast.makeText(getContext(), "Stundenplan wurde gelöscht", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Abbrechen", (dialog, which) -> {
                });
                builder.create().show(); //AlertDialog wird gezeigt
            } else {
                //keine Stunden eingetragen
                Toast.makeText(getContext(), "Keine Stunden eingetragen", Toast.LENGTH_SHORT).show();
            }

        });

        return root;
    }

    private void alertDialogDismiss() {
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
        builder.setNegativeButton("Abbrechen", (dialog, which) -> {
            messages.setChecked(false);
            ConfigFile.writeToFile("0", 3, requireActivity()); //ConfigFile Änderung
        });
        builder.create().show(); //AlertDialog wird gezeigt

    }

    private void writeFontsizeToConfig(String fontsize) {
        ConfigFile.writeToFile(fontsize, 1, requireActivity()); //Schriftgröße ins ConfigFile
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConfigFile.getConfigData(requireActivity(), 3) == 1
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            messages.setChecked(true);
            textMessagesPermission.setText("Berechtigung bereits erteilt");

        } else {
            ConfigFile.writeToFile("0", 3, requireActivity()); //ConfigFile Änderung
            messages.setChecked(false);
            textMessagesPermission.setText("Keine Berechtigung");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}