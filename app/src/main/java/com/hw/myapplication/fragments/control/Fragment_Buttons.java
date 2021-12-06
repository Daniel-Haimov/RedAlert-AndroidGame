package com.hw.myapplication.fragments.control;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hw.myapplication.R;
import com.hw.myapplication.callbacks.CallBack_MovePlayer;
import com.hw.myapplication.data.Direction;

public class Fragment_Buttons extends Fragment implements GameController {


    private CallBack_MovePlayer callBackMovePlayer;

    private ImageButton panel_BTN_left ;
    private ImageButton panel_BTN_right;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        findButtonsView(view);
        initButtons();

        return view;
    }

    private void findButtonsView(View view) {
        panel_BTN_left      = view.findViewById(R.id.panel_BTN_left );
        panel_BTN_right     = view.findViewById(R.id.panel_BTN_right);
    }

    private void initButtons() {
        panel_BTN_left.setOnClickListener(v -> callBackMovePlayer.movePlayer(Direction.LEFT));

        panel_BTN_right.setOnClickListener(v -> callBackMovePlayer.movePlayer(Direction.RIGHT));
    }

    @Override
    public void setActivity(AppCompatActivity activity) {
    }

    @Override
    public void setCallBackMovePlayer(CallBack_MovePlayer callBackMovePlayer) {
        this.callBackMovePlayer = callBackMovePlayer;
    }
}