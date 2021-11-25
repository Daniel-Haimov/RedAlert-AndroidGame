package com.hw.myapplication.data;

import java.util.ArrayList;

public class RecordDBController {
    public static void updateRecord(ArrayList<Record> records, Record record) {
        records.add(record);
        records.sort((a, b) -> (int) (b.getScore() - a.getScore()));

        if(records.size() < 10){
            return;
        }else{
            records.remove(10);
        }
    }
}
