package com.hw.myapplication.fragments.control;

import androidx.appcompat.app.AppCompatActivity;

import com.hw.myapplication.callbacks.CallBack_MovePlayer;

public interface GameController {

    void setActivity(AppCompatActivity activity);

    void setCallBackMovePlayer(CallBack_MovePlayer cb);
}
