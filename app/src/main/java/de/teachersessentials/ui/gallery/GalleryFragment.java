package de.teachersessentials.ui.gallery;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import de.teachersessentials.R;
import de.teachersessentials.csv.CsvParser;
import de.teachersessentials.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewTest;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

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


                CsvParser.save("test.csv", data, headers, "Testtabelle", getActivity().getApplicationContext());
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