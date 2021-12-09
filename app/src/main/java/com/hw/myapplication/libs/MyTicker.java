package com.hw.myapplication.libs;

import android.os.Handler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unused")
@NoArgsConstructor
public class MyTicker {
    public static int DEF_DELAY = 1000;
    @Getter @Setter
    int Delay;
    private Handler handler;
    private Runnable timerRunnable;

    public MyTicker(Handler handler, Runnable timerRunnable){
        this();
        setDelay(DEF_DELAY);
        this.handler = handler;
        this.timerRunnable = timerRunnable;
    }

    public void startTicker() {
        handler.postDelayed(timerRunnable, Delay);
    }

    public void stopTicker() {
        handler.removeCallbacks(timerRunnable);
    }

}
