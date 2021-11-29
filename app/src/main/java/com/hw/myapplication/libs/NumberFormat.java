package com.hw.myapplication.libs;

import java.text.DecimalFormat;
// import java.util.Map;
// import java.util.NavigableMap;
// import java.util.TreeMap;

public class NumberFormat {

    public static String format(long value) {
        float fValue = value;
        String[] arr = {"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((fValue / 1000) >= 1) {
            fValue = fValue / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s%s", decimalFormat.format(fValue), arr[index]);
    }
}
