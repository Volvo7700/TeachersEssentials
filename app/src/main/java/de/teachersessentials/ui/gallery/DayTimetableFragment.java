import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.teachersessentials.R;

public class DayFragment extends Fragment {

    private String day;

    public static DayFragment newInstance(String day) {
        DayFragment fragment = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("day", day);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString("day");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        TextView textView = view.findViewById(R.id.day_text);
        textView.setText("This is " + day);

        return view;
    }
}