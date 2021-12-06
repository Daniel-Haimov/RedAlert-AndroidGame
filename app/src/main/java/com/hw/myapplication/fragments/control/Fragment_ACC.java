package com.hw.myapplication.fragments.control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hw.myapplication.R;
import com.hw.myapplication.callbacks.CallBack_MovePlayer;
import com.hw.myapplication.data.Direction;
import com.hw.myapplication.data.KeysAndValues;


public class Fragment_ACC extends Fragment implements GameController {

    private AppCompatActivity activity;
    private CallBack_MovePlayer callBackMovePlayer;

    private SensorManager sensorManager;
    private Sensor accSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc, container, false);
        initACC();
        return view;
    }


    private void initACC() {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private final SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];

            if ( x < -3 ){
                callBackMovePlayer.movePlayer(Direction.RIGHT);
            } else if ( x > 3 ) {
                callBackMovePlayer.movePlayer(Direction.LEFT);
            }

            if ( y < -3 ){
                callBackMovePlayer.gameSpeed(KeysAndValues.SETTINGS_GAME_SPEED_FAST);
            } else if ( y > 3 ) {
                callBackMovePlayer.gameSpeed(KeysAndValues.SETTINGS_GAME_SPEED_SLOW);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
    }


    @Override
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setCallBackMovePlayer(CallBack_MovePlayer callBackMovePlayer) {
        this.callBackMovePlayer = callBackMovePlayer;
    }
}