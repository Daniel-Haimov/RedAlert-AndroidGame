package com.hw.myapplication.libs;

import android.os.Handler;

import com.hw.myapplication.data.KeysAndValues;

import java.util.Locale;

public class MyTicker {
    public static int DEF_DELAY = 1000;
    private int Delay;
    private Handler handler;
    private Runnable timerRunnable;

    public MyTicker(){
    }

    public MyTicker(Handler handler, Runnable timerRunnable){
        this();
        setDelay(DEF_DELAY);
        this.handler = handler;
        this.timerRunnable = timerRunnable;
    }

    public int getDelay() {
        return Delay;
    }

    public void setDelay(int delay) {
        this.Delay = delay;
    }

    public void updateDelay(Runnable timerRunnable) {
        stopTicker();
        this.timerRunnable = timerRunnable;
        startTicker();
    }

    public void startTicker() {
        handler.postDelayed(timerRunnable, Delay);
    }

    public void stopTicker() {
        handler.removeCallbacks(timerRunnable);
    }

}
