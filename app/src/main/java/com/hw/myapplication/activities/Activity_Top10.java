package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hw.myapplication.callbacks.CallBack_List;
import com.hw.myapplication.data.Record;
import com.hw.myapplication.data.RecordDB;
import com.hw.myapplication.fragments.Fragment_List;
import com.hw.myapplication.fragments.Fragment_Map;
import com.hw.myapplication.R;
import com.hw.myapplication.libs.NumberFormat;

public class Activity_Top10 extends AppCompatActivity {

    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;
    private RecordDB recordDB;

    CallBack_List callBackList = (index) -> {
        Record record       = recordDB.getRecords().get(index);
        String playerName   = record.getPlayerName();
        double lat          = record.getLat();
        double lon          = record.getLon();
        String score        = NumberFormat.format(record.getScore());
        fragmentMap.setLocation(playerName, score, lat, lon);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);

        /* Top10List Fragment */
        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        fragmentList.setCallBackList(callBackList);
        // TODO update TOP10 records, send TOP10 List to fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();

        /* GoogleMap Fragment */
        fragmentMap = new Fragment_Map();
        fragmentMap.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}