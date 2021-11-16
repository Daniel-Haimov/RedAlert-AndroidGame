package com.hw.myapplication;

import android.os.Handler;

public class MyTicker {
    public static int DEF_DELAY = 1000;
    private int Delay = 1000;
    private Handler handler;
    private Runnable timerRunnable;

    public MyTicker(){
    }

    public MyTicker(Handler handler, Runnable timerRunnable){
        this();
        this.handler = handler;
        this.timerRunnable = timerRunnable;
    }

    public int getDelay() {
        return Delay;
    }

    public void setDelay(int delay) {
        this.Delay = delay;
    }


    public void startTicker() {
        handler.postDelayed(timerRunnable, Delay);
    }

    public void stopTicker() {
        handler.removeCallbacks(timerRunnable);
    }

}
