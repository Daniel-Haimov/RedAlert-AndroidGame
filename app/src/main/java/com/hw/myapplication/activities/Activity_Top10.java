package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hw.myapplication.callbacks.CallBack_List;
import com.hw.myapplication.callbacks.CallBack_Map;
import com.hw.myapplication.fragments.Fragment_List;
import com.hw.myapplication.fragments.Fragment_Map;
import com.hw.myapplication.R;

public class Activity_Top10 extends AppCompatActivity {

    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);

        /* Top10List Fragment */
        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        fragmentList.setCallBackList(callBackList);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();

        /* GoogleMap Fragment */
        fragmentMap = new Fragment_Map();
        fragmentMap.setActivity(this);
        fragmentMap.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();


    }


    CallBack_Map callBack_map = new CallBack_Map() {
        // unused
    };

    CallBack_List callBackList = new CallBack_List() {
        @Override
        public void rowSelected(String name) {
            String[] values = name.split(",");
            String playerName = values[0];
            double lat = Double.parseDouble(values[1]);
            double lan = Double.parseDouble(values[2]);
            int score = Integer.parseInt(values[3]);
            fragmentMap.setLocation(lat, lan, playerName, score);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

}