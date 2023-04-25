package com.example.hpdisplaymanager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonitorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonitorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonitorFragment newInstance(String param1, String param2) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_monitor, container, false);
        TextView textView;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Monitors", MODE_PRIVATE);
        List<String> manufactures = getManufactures();
        for (String manufacturer : manufactures) {
            String saveddesktop = manufacturer + "_primary_monitor_string";
            String textViewText = sharedPreferences.getString(saveddesktop, "");
            textView = view.findViewById(getResources().getIdentifier(saveddesktop, "id", getActivity().getPackageName()));
            if (textViewText.isEmpty()) {
                textViewText = "0";
            }
            textView.setText(textViewText);
            saveddesktop = manufacturer + "_secondary_monitor_string";
            textViewText = sharedPreferences.getString(saveddesktop, "");
            textView = view.findViewById(getResources().getIdentifier(saveddesktop, "id", getActivity().getPackageName()));
            if (textViewText.isEmpty()) {
                textViewText = "0";
            }
            textView.setText(textViewText);
        }

        MaterialToolbar toolbar = view.findViewById(R.id.materialToolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.clear_button) {
                clearScreen(view);
                return true;
            }
            return false;
        });


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Monitors", Context.MODE_PRIVATE);

        // Save the TextView values
        TextView textView;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<String> manufactures = getManufactures();
        for (String manufacturer : manufactures) {
            String saveddesktop = manufacturer + "_primary_monitor_string";
            textView = getView().findViewById(getResources().getIdentifier(saveddesktop, "id", getActivity().getPackageName()));
            String count = textView.getText().toString();
            editor.putString(saveddesktop, count);
            saveddesktop = manufacturer + "_secondary_monitor_string";
            textView = getView().findViewById(getResources().getIdentifier(saveddesktop, "id", getActivity().getPackageName()));
            count = textView.getText().toString();
            editor.putString(saveddesktop, count);
        }
        editor.apply();
    }


    protected void plus(String idName) {

        String resourceID = getPlusResourceID(idName);
        int id = getResources().getIdentifier(resourceID, "id", requireActivity().getPackageName());
        TextView primaryView = requireView().findViewById(id);
        int count = Integer.parseInt(primaryView.getText().toString()) + 1;
        primaryView.setText(Integer.toString(count));

    }

    protected void minus(String idName) {

        String resourceID = getPlusResourceID(idName);
        int id = getResources().getIdentifier(resourceID, "id", requireActivity().getPackageName());
        TextView primaryView = requireView().findViewById(id);
        int count = Integer.parseInt(primaryView.getText().toString());
        if(count > 0 )
            count--;
        primaryView.setText(Integer.toString(count));

    }

    private String getPlusResourceID(String idName) {
        String resourceID;
        if(idName.contains("hp")){
            if(idName.contains("primary"))
                resourceID = "hp_primary_monitor_string";
            else
                resourceID = "hp_secondary_monitor_string";
        }else if(idName.contains("dell")) {
            if (idName.contains("primary"))
                resourceID = "dell_primary_monitor_string";
            else
                resourceID = "dell_secondary_monitor_string";
        }else if(idName.contains("lg")) {
            if (idName.contains("primary"))
                resourceID = "lg_primary_monitor_string";
            else
                resourceID = "lg_secondary_monitor_string";
        }else if(idName.contains("samsung")) {
            if (idName.contains("primary"))
                resourceID = "samsung_primary_monitor_string";
            else
                resourceID = "samsung_secondary_monitor_string";
        }else if(idName.contains("lenovo")) {
            if (idName.contains("primary"))
                resourceID = "lenovo_primary_monitor_string";
            else
                resourceID = "lenovo_secondary_monitor_string";
        }else if(idName.contains("acer")) {
            if (idName.contains("primary"))
                resourceID = "acer_primary_monitor_string";
            else
                resourceID = "acer_secondary_monitor_string";
        }else if(idName.contains("philips")) {
            if (idName.contains("primary"))
                resourceID = "philips_primary_monitor_string";
            else
                resourceID = "philips_secondary_monitor_string";
        }else if(idName.contains("asus")) {
            if (idName.contains("primary"))
                resourceID = "asus_primary_monitor_string";
            else
                resourceID = "asus_secondary_monitor_string";
        }else if(idName.contains("aoc")) {
            if (idName.contains("primary"))
                resourceID = "aoc_primary_monitor_string";
            else
                resourceID = "aoc_secondary_monitor_string";
        }else if(idName.contains("benq")) {
            if (idName.contains("primary"))
                resourceID = "benq_primary_monitor_string";
            else
                resourceID = "benq_secondary_monitor_string";
        }else if(idName.contains("msi")) {
            if (idName.contains("primary"))
                resourceID = "msi_primary_monitor_string";
            else
                resourceID = "msi_secondary_monitor_string";
        }else{
            if (idName.contains("primary"))
                resourceID = "others_primary_monitor_string";
            else
                resourceID = "others_secondary_monitor_string";
        }
        return resourceID;
    }

    public static List<String> getManufactures() {
        List<String> allManufactures = new ArrayList<>();

        allManufactures.add("hp");
        allManufactures.add("lg");
        allManufactures.add("asus");
        allManufactures.add("dell");
        allManufactures.add("samsung");
        allManufactures.add("philips");
        allManufactures.add("aoc");
        allManufactures.add("acer");
        allManufactures.add("benq");
        allManufactures.add("msi");
        allManufactures.add("lenovo");
        allManufactures.add("others");

        return allManufactures;
    }

    public void clearScreen(View view) {

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(40);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Είστε σίγουρος πως θέλετε να μηδενίσετε όλες τις τιμές?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    TextView textView;
                    List<String> manufactures = getManufactures();
                    for (String manufacturer : manufactures) {
                        String savedMonitor = manufacturer + "_primary_monitor_string";
                        textView = view.findViewById(getResources().getIdentifier(savedMonitor, "id", getActivity().getPackageName()));
                        textView.setText("0");
                        savedMonitor = manufacturer + "_secondary_monitor_string";
                        textView = view.findViewById(getResources().getIdentifier(savedMonitor, "id", getActivity().getPackageName()));
                        textView.setText("0");
                        vibrator.vibrate(40);
                    }
                })
                .setNegativeButton("No", (dialog, id) -> {
                    vibrator.vibrate(40);
                });
        builder.create().show();
    }

}