package de.teachersessentials.ui.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import de.teachersessentials.csv.CsvParser;
import de.teachersessentials.databinding.FragmentGalleryBinding;
import de.teachersessentials.R;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private final String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    @SuppressLint("NewApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Spinner zur Auswahl der Tage
        Spinner selectDay = root.findViewById(R.id.select_day);

        //Liste wird in Spinner geladen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        selectDay.setAdapter(adapter);

        selectDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Auswahl der Tage
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //Montag

                } else if (position == 1) { //Dienstag

                } else if (position == 2) { //Mittwoch

                } else if (position == 3) { //Donnerstag

                } else if (position == 4) { //Freitag
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button testbutton = (Button) root.findViewById(R.id.button_test);
        testbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textView_test = (TextView) root.findViewById(R.id.textView_test);
                ArrayList<String[]> data = new ArrayList<>();
                String[] line = new String[4];
                line[0] = "hallo";
                line[1] = "test";
                line[2] = "hier";
                line[3] = "CSV";
                data.add(line);

                String[] headers = new String[1];
                headers[0] = "test";


                //CsvParser.save("test.csv", data, headers, "Testtabelle", getActivity().getApplicationContext());
                ArrayList<String[]> loadedData = CsvParser.read("test.csv", getActivity().getApplicationContext());


                textView_test.setText(loadedData.get(0).toString());
                String text = new String();
                for (String s : loadedData.get(0) )
                {
                    text += s;
                }
                textView_test.setText(text);
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