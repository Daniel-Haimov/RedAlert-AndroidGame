package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hw.myapplication.R;
import com.hw.myapplication.data.KeysAndValues;
import com.hw.myapplication.libs.MSPV3;

public class Activity_Entry extends AppCompatActivity {
    private Bundle bundle;

    private EditText    entry_TXTF_UserName ;
    private Button      entry_BTN_Play      ;
    private Button      entry_BTN_Settings  ;
    private Button      entry_BTN_Top10     ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grantLocationPermission();
        if (getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY) != null){
            this.bundle = getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY);
        } else {
            this.bundle = new Bundle();
        }

        setContentView(R.layout.activity_entry);
        findViews();
        setBackGround();
        initButtons();
    }


    private void setBackGround() {
        ImageView entry_IMG_backGround = findViewById(R.id.entry_IMG_backGround);
        Glide.with(Activity_Entry.this).load(R.drawable.bg_entry).centerCrop().into(entry_IMG_backGround);
    }

    private void findViews() {
        entry_TXTF_UserName = findViewById(R.id.entry_TXTF_UserName );

        entry_BTN_Play      = findViewById(R.id.entry_BTN_Play      );
        entry_BTN_Settings  = findViewById(R.id.entry_BTN_Settings  );
        entry_BTN_Top10     = findViewById(R.id.entry_BTN_Top10     );
    }

    private void initButtons() {
        entry_BTN_Play      .setOnClickListener(v -> startGame(Activity_Game    .class      ));
        entry_BTN_Settings  .setOnClickListener(v -> startGame(Activity_Settings.class      ));
        entry_BTN_Top10     .setOnClickListener(v -> startGame(Activity_Top10   .class      ));
    }

    private void startGame(Class<?> activity) {
        Intent myIntent = new Intent(this, activity);
        initBundle();
        myIntent.putExtra(KeysAndValues.BUNDLE_KEY, bundle);
        startActivity(myIntent);
    }

    private void initBundle() {
        MSPV3 sp = MSPV3.getMe();
        // Username
        String username = entry_TXTF_UserName.getText().toString();
        if (username.equals("")){
            username = KeysAndValues.PLAYER_USERNAME_DEFAULT;
        }

        bundle.putString(KeysAndValues.PLAYER_USERNAME_KEY      , username                                 );
        bundle.putLong  (KeysAndValues.PLAYER_SCORE_KEY         , KeysAndValues.PLAYER_SCORE_DEFAULT       );
        bundle.putDouble(KeysAndValues.PLAYER_LOCATION_LAT_KEY  , KeysAndValues.PLAYER_LOCATION_LAT_DEFAULT);
        bundle.putDouble(KeysAndValues.PLAYER_LOCATION_LON_KEY  , KeysAndValues.PLAYER_LOCATION_LON_DEFAULT);

        //Settings
        bundle.putString(KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY  , sp.getString  (KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY  , KeysAndValues.SETTINGS_PLAYER_CONTROL_DEFAULT ));
        bundle.putLong  (KeysAndValues.SETTINGS_GAME_SPEED_KEY      , sp.getLong    (KeysAndValues.SETTINGS_GAME_SPEED_KEY      , KeysAndValues.SETTINGS_GAME_SPEED_DEFAULT     ));

    }


    private void grantLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}  , 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }

}