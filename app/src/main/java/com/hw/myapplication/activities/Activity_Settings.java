package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hw.myapplication.R;
import com.hw.myapplication.data.KeysAndValues;

public class Activity_Settings extends AppCompatActivity {

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.bundle = getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY);

        findViews();
        initListeners();
    }

    private void initListeners() {
        // TODO set listeners and update values in SP
    }

    private void findViews() {
        // TODO find settings view
    }
}