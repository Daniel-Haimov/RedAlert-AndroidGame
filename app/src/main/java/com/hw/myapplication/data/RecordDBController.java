package com.hw.myapplication.data;

import java.util.ArrayList;

public class RecordDBController {
    public static ArrayList<Record> updateRecord(ArrayList<Record> records, Record record) {
        records.add(record);
        records.sort((a, b) -> (int) (a.getScore() - b.getScore()));

        // TODO beautify
        if(records.size() > 10){
            records.remove(10);
        }
        return records;
    }
}
