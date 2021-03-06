package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hw.myapplication.R;
import com.hw.myapplication.data.KeysAndValues;
import com.hw.myapplication.libs.MSPV3;

public class Activity_Settings extends AppCompatActivity {

    private Bundle bundle;
    // BUTTONS <-> ACC
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch      settings_SWC_control        ;
    // SLOW <-> FAST
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch      settings_SWC_speed          ;
    private TextView    settings_TXT_slow           ;
    private TextView    settings_TXT_fast           ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.bundle = getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY);

        findViews();
        initViews();
        initListeners();
        updateSettings();
        setBackGround();
    }

    private void setBackGround() {
        ImageView settings_IMG_backGround = findViewById(R.id.settings_IMG_backGround);
        Glide.with(Activity_Settings.this).load(R.drawable.bg_settings).centerCrop().into(settings_IMG_backGround);
    }

    private void initViews() {
        if (bundle.getString(KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY, KeysAndValues.SETTINGS_PLAYER_CONTROL_DEFAULT).equals(KeysAndValues.SETTINGS_PLAYER_CONTROL_ACC)){
            settings_SWC_control.setChecked(true);
        }
        if (bundle.getLong(KeysAndValues.SETTINGS_GAME_SPEED_KEY, KeysAndValues.SETTINGS_GAME_SPEED_DEFAULT) == (KeysAndValues.SETTINGS_GAME_SPEED_FAST)){
            settings_SWC_speed.setChecked(true);
        }
    }

    private void initListeners() {
        settings_SWC_control.setOnClickListener(view -> updateSettings());
    }

    private void updateSettings() {
        if(settings_SWC_control.isChecked()){
            settings_SWC_speed  .setVisibility  (View.INVISIBLE);
            settings_TXT_slow   .setVisibility  (View.INVISIBLE);
            settings_TXT_fast   .setVisibility  (View.INVISIBLE);
        } else {
            settings_SWC_speed  .setVisibility  (View.VISIBLE)  ;
            settings_TXT_slow   .setVisibility  (View.VISIBLE)  ;
            settings_TXT_fast   .setVisibility  (View.VISIBLE)  ;
        }
        saveSettings();
    }

    private void saveSettings() {
        MSPV3 sp = MSPV3.getMe();

        if(!settings_SWC_control.isChecked()){
            sp.putString(KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY, KeysAndValues.SETTINGS_PLAYER_CONTROL_BUTTONS);
        }else {
            sp.putString(KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY, KeysAndValues.SETTINGS_PLAYER_CONTROL_ACC);
        }

        if(!settings_SWC_speed.isChecked()){
            sp.putLong(KeysAndValues.SETTINGS_GAME_SPEED_KEY, KeysAndValues.SETTINGS_GAME_SPEED_SLOW);
        }else {
            sp.putLong(KeysAndValues.SETTINGS_GAME_SPEED_KEY, KeysAndValues.SETTINGS_GAME_SPEED_FAST);
        }
    }

    private void findViews() {
        settings_SWC_control    = findViewById(R.id.settings_SWC_control);
        settings_SWC_speed      = findViewById(R.id.settings_SWC_speed  );
        settings_TXT_slow       = findViewById(R.id.settings_TXT_slow   );
        settings_TXT_fast       = findViewById(R.id.settings_TXT_fast   );
    }

}