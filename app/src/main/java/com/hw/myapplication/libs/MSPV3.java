package com.hw.myapplication.libs;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class MSPV3 {

    private static MSPV3 me;
    private final SharedPreferences sharedPreferences;

    public static MSPV3 getMe() {
        return me;
    }

    private MSPV3(Context context) {
        String SP_FILE = "My_SP_FILE";
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static void initHelper(Context context) {
        if (me == null) {
            me = new MSPV3(context);
        }
    }

    public void putDouble(String KEY, double defValue) {
        putString(KEY, String.valueOf(defValue));
    }

    public double getDouble(String KEY, double defValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defValue)));
    }

    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY, defValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }

    public long getLong(String KEY, long defValue) {
        return sharedPreferences.getLong(KEY, defValue);
    }

    public void putLong(String KEY, long value) {
        sharedPreferences.edit().putLong(KEY, value).apply();
    }
}
