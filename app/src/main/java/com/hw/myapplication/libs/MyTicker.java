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

    public void setDelay(String delayValue) {
        switch (delayValue){
            case KeysAndValues.SETTINGS_GAME_SPEED_SLOW:
                setDelay(DEF_DELAY * 2);
                break;
            case KeysAndValues.SETTINGS_GAME_SPEED_FAST:
                setDelay(DEF_DELAY / 2);
                break;
        }
    }




    public void startTicker() {
        handler.postDelayed(timerRunnable, Delay);
    }

    public void stopTicker() {
        handler.removeCallbacks(timerRunnable);
    }

}
