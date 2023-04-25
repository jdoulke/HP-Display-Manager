package com.example.hpdisplaymanager;

import static android.content.Context.MODE_PRIVATE;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrinterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("SpellCheckingInspection")
public class PrinterFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrinterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrinterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrinterFragment newInstance(String param1, String param2) {
        PrinterFragment fragment = new PrinterFragment();
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

    private ExpandableListView expandableListView;
    private List<String> groupData;
    private List<List<String>> childData;

    public void onPause() {
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_printer, container, false);

        // Find the ExpandableListView by its ID
        expandableListView = view.findViewById(R.id.listview);
        //expandableListView.setGroupIndicator(ContextCompat.getDrawable(getContext(), R.drawable.custom_group_indicator));

        groupData = new ArrayList<>();
        childData = new ArrayList<>();

        groupData.add("DeskJets");
        groupData.add("LaserJets");
        groupData.add("OfficeJets");
        groupData.add("SmartTanks");
        groupData.add("NeverStops");

        List<String> deskjets = new ArrayList<>();
        deskjets.add("Deskjets");
        List<String> laserjets = new ArrayList<>();
        laserjets.add("Laserjets");

        List<String> officejets = new ArrayList<>();
        officejets.add("Officejets");

        List<String> smarttanks = new ArrayList<>();
        smarttanks.add("Smarttanks");

        List<String> neverstops = new ArrayList<>();
        neverstops.add("Neverstops");

        childData.add(deskjets);
        childData.add(laserjets);
        childData.add(officejets);
        childData.add(smarttanks);
        childData.add(neverstops);

        // Create and set the ExpandableListAdapter
        createExpandableListAdapter();



        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            loadData(groupPosition);
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(40);
            if (expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition); // Collapse the group
            } else {
                expandableListView.expandGroup(groupPosition); // Expand the group
            }
            return true;
        });

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

    private void saveData(String idName, int count) {
        String savedPrinter;
        String manufacturer;

        if (idName.contains("plus"))
            manufacturer = extractTextBetweenCharacters(idName, "_", "_plus_");
        else
            manufacturer = extractTextBetweenCharacters(idName, "_", "_minus_");

        HashMap<String, String> printerTypeMap = new HashMap<>();
        printerTypeMap.put("deskjets", "Deskjets");
        printerTypeMap.put("laserjets", "Laserjets");
        printerTypeMap.put("officejets", "Officejets");
        printerTypeMap.put("smarttanks", "Smarttanks");
        printerTypeMap.put("neverstops", "Neverstops");

        String printerType = "";
        for (String key : printerTypeMap.keySet()) {
            if (idName.contains(key)) {
                printerType = printerTypeMap.get(key);
                break;
            }
        }

        if (printerType != null && !printerType.isEmpty()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(printerType, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (idName.contains("primary")) {
                savedPrinter = printerType.toLowerCase() + "_" + manufacturer + "_string_primary";
            } else {
                savedPrinter = printerType.toLowerCase() + "_" + manufacturer + "_string_secondary";
            }
            editor.putString(savedPrinter, String.valueOf(count));
            editor.apply();
        }
    }



    @SuppressLint({"InflateParams", "DiscouragedApi"})
    private View loadData(int groupPosition) {
        String groupName = groupData.get(groupPosition);
        TextView textView;
        SharedPreferences sharedPreferences;
        List<String> manufactures;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view;

        if ("DeskJets".equals(groupName)) {
            sharedPreferences = getActivity().getSharedPreferences("Deskjets", MODE_PRIVATE);
            manufactures = getDeskjetsManufactures();
            view = inflater.inflate(R.layout.list_item_deskjets, null);
        } else if ("LaserJets".equals(groupName)) {
            sharedPreferences = getActivity().getSharedPreferences("Laserjets", MODE_PRIVATE);
            manufactures = getLaserjetsManufactures();
            view = inflater.inflate(R.layout.list_item_laserjets, null);
        } else if ("OfficeJets".equals(groupName)) {
            sharedPreferences = getActivity().getSharedPreferences("Officejets", MODE_PRIVATE);
            manufactures = getOfficejetsManufactures();
            view = inflater.inflate(R.layout.list_item_officejets, null);
        } else if ("SmartTanks".equals(groupName)) {
            sharedPreferences = getActivity().getSharedPreferences("Smarttanks", MODE_PRIVATE);
            manufactures = getSmarttanksAndNeverstopsManufactures();
            view = inflater.inflate(R.layout.list_item_smarttanks, null);
        } else if ("NeverStops".equals(groupName)) {
            sharedPreferences = getActivity().getSharedPreferences("Neverstops", MODE_PRIVATE);
            manufactures = getSmarttanksAndNeverstopsManufactures();
            view = inflater.inflate(R.layout.list_item_neverstops, null);
        } else {
            return null;
        }

        for (String manufacturer : manufactures) {
            String savedPrinter =  groupName.toLowerCase() + "_" + manufacturer + "_string_primary";
            String textViewText = sharedPreferences.getString(savedPrinter, "");
            textView = view.findViewById(getResources().getIdentifier(savedPrinter, "id", getActivity().getPackageName()));
            if (textViewText.isEmpty()) {
                textViewText = "0";
            }
            textView.setText(textViewText);

            savedPrinter = groupName.toLowerCase() + "_" + manufacturer + "_string_secondary";
            textViewText = sharedPreferences.getString(savedPrinter, "");
            textView = view.findViewById(getResources().getIdentifier(savedPrinter, "id", getActivity().getPackageName()));
            if (textViewText.isEmpty()) {
                textViewText = "0";
            }
            textView.setText(textViewText);
        }

        return view;
    }




    private void createExpandableListAdapter() {
        // Create and set the ExpandableListAdapter with the initialized group data and child data
        ExpandableListAdapter expandableListAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return groupData.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return childData.get(groupPosition).size();
            }

            @Override
            public String getGroup(int groupPosition) {
                return groupData.get(groupPosition);
            }

            @Override
            public String getChild(int groupPosition, int childPosition) {
                return childData.get(groupPosition).get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    convertView = inflater.inflate(R.layout.list_group, parent, false);
                }

                TextView textView = convertView.findViewById(R.id.groupTitleTextView);
                textView.setText(getGroup(groupPosition));



                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                convertView = null;
                String groupName = getGroup(groupPosition);

                if ("DeskJets".equals(groupName)) {
                    convertView = loadData(0);
                }
                else if ("LaserJets".equals(groupName)) {
                    convertView = loadData(1);
                }
                else if ("OfficeJets".equals(groupName)) {
                    convertView = loadData(2);
                }
                else if ("SmartTanks".equals(groupName)) {
                    convertView = loadData(3);
                }
                else if ("NeverStops".equals(groupName)) {
                    convertView = loadData(4);
                }



                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };

        expandableListView.setAdapter(expandableListAdapter);
    }

    public static List<String> getDeskjetsManufactures() {
        List<String> allManufactures = new ArrayList<>();

        allManufactures.add("hp");
        allManufactures.add("canon");
        allManufactures.add("epson");
        allManufactures.add("others");
        allManufactures.add("xerox");
        allManufactures.add("brother");
        allManufactures.add("lexmark");
        allManufactures.add("kyocera");
        allManufactures.add("ricoh");

        return allManufactures;
    }

    public static List<String> getLaserjetsManufactures() {
        List<String> allManufactures = new ArrayList<>();

        allManufactures.add("hp");
        allManufactures.add("samsung");
        allManufactures.add("ricoh");
        allManufactures.add("others");

        return allManufactures;
    }


    public static List<String> getSmarttanksAndNeverstopsManufactures() {
        List<String> allManufactures = new ArrayList<>();

        allManufactures.add("hp");
        allManufactures.add("epson");
        allManufactures.add("canon");

        return allManufactures;
    }


    public static List<String> getOfficejetsManufactures() {
        List<String> allManufactures = new ArrayList<>();

        allManufactures.add("hp");
        allManufactures.add("epson");
        allManufactures.add("others");

        return allManufactures;
    }

    protected void plus(String idName) {

        String resourceID = getPlusResourceID(idName);
        @SuppressLint("DiscouragedApi") int id = getResources().getIdentifier(resourceID, "id", requireActivity().getPackageName());
        TextView primaryView = requireView().findViewById(id);
        int count = Integer.parseInt(primaryView.getText().toString()) + 1;
        primaryView.setText(Integer.toString(count));
        saveData(idName, count);

    }

    @SuppressLint("SetTextI18n")
    protected void minus(String idName) {

        String resourceID = getPlusResourceID(idName);
        int id = getResources().getIdentifier(resourceID, "id", requireActivity().getPackageName());
        TextView primaryView = requireView().findViewById(id);
        int count = Integer.parseInt(primaryView.getText().toString());
        if(count > 0 )
            count--;
        primaryView.setText(Integer.toString(count));
        saveData(idName, count);

    }

    public static String extractTextBetweenCharacters(String input, String startChar, String endChar) {
        String patternString = Pattern.quote(startChar) + "(.*?)" + Pattern.quote(endChar);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public void clearScreen(View view) {

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(40);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Είστε σίγουρος πως θέλετε να μηδενίσετε όλες τις τιμές?")
                .setPositiveButton("Yes", (dialog, id) -> {

                    List<String> manufactures = getDeskjetsManufactures();
                    resetData(manufactures, view, "deskjets");
                    manufactures = getLaserjetsManufactures();
                    resetData(manufactures, view, "laserjets");
                    manufactures = getOfficejetsManufactures();
                    resetData(manufactures, view, "officejets");
                    manufactures = getSmarttanksAndNeverstopsManufactures();
                    resetData(manufactures, view, "smarttanks");
                    resetData(manufactures, view, "neverstops");
                    for (int i = 0; i < 5; i++) {
                        expandableListView.collapseGroup(i);
                    }
                    vibrator.vibrate(40);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    vibrator.vibrate(40);
                });
        builder.create().show();
    }

    private void resetData(List<String> manufactures, View view, String category) {
        TextView textView;

        String savedPrinter;
        String printerType = category.substring(0, 1).toUpperCase() + category.substring(1);

        for (String manufacturer : manufactures) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(printerType, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            savedPrinter = category + "_" + manufacturer + "_string_primary";
            editor.putString(savedPrinter, "0");
            savedPrinter = category + "_" + manufacturer + "_string_secondary";
            editor.putString(savedPrinter, "0");
            editor.apply();
        }

    }


    private String getPlusResourceID(String idName) {
        String resourceID = "";
        if (idName.contains("deskjets")) {
            if (idName.contains("hp")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_hp_string_primary";
                else
                    resourceID = "deskjets_hp_string_secondary";
            } else if (idName.contains("canon")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_canon_string_primary";
                else
                    resourceID = "deskjets_canon_string_secondary";
            } else if (idName.contains("epson")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_epson_string_primary";
                else
                    resourceID = "deskjets_epson_string_secondary";
            } else if (idName.contains("others")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_others_string_primary";
                else
                    resourceID = "deskjets_others_string_secondary";
            } else if (idName.contains("xerox")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_xerox_string_primary";
                else
                    resourceID = "deskjets_xerox_string_secondary";
            } else if (idName.contains("brother")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_brother_string_primary";
                else
                    resourceID = "deskjets_brother_string_secondary";
            } else if (idName.contains("lexmark")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_lexmark_string_primary";
                else
                    resourceID = "deskjets_lexmark_string_secondary";
            } else if (idName.contains("kyocera")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_kyocera_string_primary";
                else
                    resourceID = "deskjets_kyocera_string_secondary";
            } else if (idName.contains("ricoh")) {
                if (idName.contains("primary"))
                    resourceID = "deskjets_ricoh_string_primary";
                else
                    resourceID = "deskjets_ricoh_string_secondary";
            }
        } else if (idName.contains("laserjets")) {
            if (idName.contains("hp")) {
                if (idName.contains("primary"))
                    resourceID = "laserjets_hp_string_primary";
                else
                    resourceID = "laserjets_hp_string_secondary";
            }else if (idName.contains("samsung")) {
                if (idName.contains("primary"))
                    resourceID = "laserjets_samsung_string_primary";
                else
                    resourceID = "laserjets_samsung_string_secondary";
            }else if (idName.contains("ricoh")) {
                if (idName.contains("primary"))
                    resourceID = "laserjets_ricoh_string_primary";
                else
                    resourceID = "laserjets_ricoh_string_secondary";
            }else if (idName.contains("others")) {
                if (idName.contains("primary"))
                    resourceID = "laserjets_others_string_primary";
                else
                    resourceID = "laserjets_others_string_secondary";
            }
        }else if (idName.contains("officejets")) {
            if (idName.contains("hp")) {
                if (idName.contains("primary"))
                    resourceID = "officejets_hp_string_primary";
                else
                    resourceID = "officejets_hp_string_secondary";
            }else if (idName.contains("epson")) {
                if (idName.contains("primary"))
                    resourceID = "officejets_epson_string_primary";
                else
                    resourceID = "officejets_epson_string_secondary";
            }else if (idName.contains("others")) {
                if (idName.contains("primary"))
                    resourceID = "officejets_others_string_primary";
                else
                    resourceID = "officejets_others_string_secondary";
            }
        }else if (idName.contains("smarttanks")) {
            if (idName.contains("hp")) {
                if (idName.contains("primary"))
                    resourceID = "smarttanks_hp_string_primary";
                else
                    resourceID = "smarttanks_hp_string_secondary";
            }else if (idName.contains("epson")) {
                if (idName.contains("primary"))
                    resourceID = "smarttanks_epson_string_primary";
                else
                    resourceID = "smarttanks_epson_string_secondary";
            }else if (idName.contains("canon")) {
                if (idName.contains("primary"))
                    resourceID = "smarttanks_canon_string_primary";
                else
                    resourceID = "smarttanks_canon_string_secondary";
            }
        }else if (idName.contains("neverstops")) {
            if (idName.contains("hp")) {
                if (idName.contains("primary"))
                    resourceID = "neverstops_hp_string_primary";
                else
                    resourceID = "neverstops_hp_string_secondary";
            }else if (idName.contains("epson")) {
                if (idName.contains("primary"))
                    resourceID = "neverstops_epson_string_primary";
                else
                    resourceID = "neverstops_epson_string_secondary";
            }else if (idName.contains("canon")) {
                if (idName.contains("primary"))
                    resourceID = "neverstops_canon_string_primary";
                else
                    resourceID = "neverstops_canon_string_secondary";
            }
        }
        return resourceID;
    }



}