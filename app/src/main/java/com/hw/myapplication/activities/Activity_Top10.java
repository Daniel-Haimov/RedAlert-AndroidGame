package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hw.myapplication.callbacks.CallBack_List;
import com.hw.myapplication.data.KeysAndValues;
import com.hw.myapplication.data.Record;
import com.hw.myapplication.data.RecordDB;
import com.hw.myapplication.data.RecordDBController;
import com.hw.myapplication.fragments.Fragment_List;
import com.hw.myapplication.fragments.Fragment_Map;
import com.hw.myapplication.R;
import com.hw.myapplication.libs.GPS;
import com.hw.myapplication.libs.NumberFormat;

public class Activity_Top10 extends AppCompatActivity {

    private Bundle bundle;

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

        this.bundle = getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY);


        /* Top10List Fragment */
        fragmentList = new Fragment_List();
        fragmentList.setCallBackList(callBackList);
        fragmentList.setActivity(this);
        updateRecordDB();


        /* GoogleMap Fragment */
        fragmentMap = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();
        setBackGround();
    }

    private void setBackGround() {
        ImageView top10_IMG_backGround = findViewById(R.id.top10_IMG_backGround);
        Glide.with(Activity_Top10.this).load(R.drawable.bg_top10).centerCrop().into(top10_IMG_backGround);
    }

    private void updateRecordDB() {
        recordDB = RecordDBController.receiveRecordDBFromSP();

        long score = bundle.getLong(KeysAndValues.PLAYER_SCORE_KEY, KeysAndValues.PLAYER_SCORE_DEFAULT);
        if (score == KeysAndValues.PLAYER_SCORE_DEFAULT){
            refreshList();
            return;
        }

        GPS.getMe().getLocation((lat, lon) -> updateCurrentRecord(lat, lon, score));
    }

    private void updateCurrentRecord(double lat, double lon, long score) {
        String playerName   = bundle.getString(KeysAndValues.PLAYER_USERNAME_KEY    );
        Record currentRecord = new Record().setPlayerName(playerName).setScore(score).setLat(lat).setLon(lon);
        RecordDBController.updateRecord(recordDB, currentRecord);
        refreshList();
    }

    private void refreshList() {
        fragmentList.setRecords(recordDB.getRecords());
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

}