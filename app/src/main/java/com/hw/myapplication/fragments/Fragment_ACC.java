package com.hw.myapplication.fragments;

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


public class Fragment_ACC extends Fragment {

    private AppCompatActivity activity;
    private CallBack_MovePlayer callBackMovePlayer;

    private SensorManager sensorManager;
    private Sensor accSensor;

    private float x_old = 0;
    private float y_old = 0;
    private float z_old = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc, container, false);
        findViews(view);
        initACC();

        return view;
    }

    private void findViews(View view) {
    }

    private void initACC() {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x_new = event.values[0];
            float y_new = event.values[1];
            float z_new = event.values[2];

            float dx = x_new - x_old;
            float dy = y_new - y_old;
            float dz = z_new - z_old;

            if ( dx < -1 ){
                callBackMovePlayer.movePlayer(Direction.RIGHT);
            } else if ( dx > 1 ) {
                callBackMovePlayer.movePlayer(Direction.LEFT);
            }

            x_old = x_new;
            y_old = y_new;
            z_old = z_new;
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

    public boolean isSensorExist(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackMovePlayer(CallBack_MovePlayer callBackMovePlayer) {
        this.callBackMovePlayer = callBackMovePlayer;
    }
}