package com.hw.myapplication;

import android.app.Application;

import com.hw.myapplication.libs.GPS;
import com.hw.myapplication.libs.MSPV3;
import com.hw.myapplication.libs.MyVibrate;

public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        MSPV3.initHelper(this);
        GPS.initHelper(this);
        MyVibrate.initHelper(this);
    }
}
