package com.hw.myapplication.libs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.hw.myapplication.callbacks.CallBack_GPS;
import com.hw.myapplication.data.KeysAndValues;

public class GPS {
    private static GPS me;
    Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public static GPS getMe() {
        return me;
    }

    private GPS(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void initHelper(Context context) {
        if (me == null) {
            me = new GPS(context);
        }
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     *
     * @param cb
     */
    public void getLocation(CallBack_GPS cb){
        if (checkPermission()) {
            fusedLocationClient.getLastLocation().addOnCompleteListener( task->{
                Location location = task.getResult();
                if(location!=null){
                    cb.getLocation(location.getLatitude(), location.getLongitude());
                }
                else{
                    cb.getLocation(KeysAndValues.PLAYER_LOCATION_LAT_DEFAULT, KeysAndValues.PLAYER_LOCATION_LON_DEFAULT);
                }
            });
        }
        else {
            cb.getLocation(KeysAndValues.PLAYER_LOCATION_LAT_DEFAULT, KeysAndValues.PLAYER_LOCATION_LON_DEFAULT);
        }
    }

}
