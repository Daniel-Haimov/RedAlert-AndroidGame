package com.hw.myapplication.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.hw.myapplication.R;
import com.hw.myapplication.activities.Activity_Top10;
import com.hw.myapplication.callbacks.CallBack_List;
import com.hw.myapplication.data.GameData;
import com.hw.myapplication.data.Record;
import com.hw.myapplication.libs.NumberFormat;

import java.util.ArrayList;


public class Fragment_List extends Fragment {

    private final int NUM_OF_ROWS = 10;
    private CallBack_List callBackList;
    private ArrayList<Record> records;
    private LinearLayout[]      list_ROW_rows       = new LinearLayout[NUM_OF_ROWS] ;
    private TextView    []      list_TXT_userNames  = new TextView    [NUM_OF_ROWS] ;
    private TextView    []      list_TXT_scores     = new TextView    [NUM_OF_ROWS] ;
    private Activity activity;


    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        updateRows();
        return view;
    }

    public void updateRows() {
        int i = 0;
        for (Record record: this.records) {
            list_TXT_userNames[i].setText(record.getPlayerName());
            list_TXT_scores[i].setText(NumberFormat.format(record.getScore()));
            list_ROW_rows[i].setVisibility(View.VISIBLE);
            i++;
        }
    }


    private void initViews() {
        for (int index = 0; index < NUM_OF_ROWS; index++) {
            list_ROW_rows[index].setOnClickListener(setOnMap(index));
        }
    }

    private View.OnClickListener setOnMap(int index) {
        return view -> callBackList.rowSelected(index);
    }

    private void findViews(View view) {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            list_TXT_userNames[i] = view.findViewById(getResources().getIdentifier(
                    "list_TXT_userName" + i,
                    "id",
                    activity.getPackageName()));
            list_TXT_scores[i] = view.findViewById(getResources().getIdentifier(
                    "list_TXT_score" + i,
                    "id",
                    activity.getPackageName()));
            list_ROW_rows[i] = view.findViewById(getResources().getIdentifier(
                    "list_ROW_row" + i,
                    "id",
                    activity.getPackageName()));
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}