package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hw.myapplication.R;

public class Activity_Entry extends AppCompatActivity {

    private EditText    entry_TXTF_UserName ;
    private Button      entry_BTN_Play      ;
    private Button      entry_BTN_Settings  ;
    private Button      entry_BTN_Top10     ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        findViews();
        initButtons();
    }

    private void findViews() {
        entry_TXTF_UserName = findViewById(R.id.entry_TXTF_UserName );
        entry_BTN_Play      = findViewById(R.id.entry_BTN_Play      );
        entry_BTN_Settings  = findViewById(R.id.entry_BTN_Settings  );
        entry_BTN_Top10     = findViewById(R.id.entry_BTN_Top10     );
    }

    private void initButtons() {
        entry_BTN_Play          .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Activity_Game.class);
            }
        });
        entry_BTN_Settings      .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Activity_Settings.class);
            }
        });
        entry_BTN_Top10         .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Activity_Top10.class);
            }
        });
    }

    private void startGame(Class activity) {
        Intent myIntent = new Intent(this, activity);
        Bundle bundle = new Bundle();
        myIntent.putExtra("Bundle", bundle);
        startActivity(myIntent);
    }

}