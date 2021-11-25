package com.hw.myapplication.data;

import com.google.gson.Gson;
import com.hw.myapplication.libs.MSPV3;

import java.util.ArrayList;

public class RecordDBController {
    public static void updateRecord(RecordDB recordDB, Record record) {
        ArrayList<Record> records = recordDB.getRecords();
        records.add(record);
        records.sort((a, b) -> (int) (b.getScore() - a.getScore()));
        if(records.size() > KeysAndValues.TOP10_MAX_LIST_SIZE){
            records.remove(KeysAndValues.TOP10_MAX_LIST_SIZE);
        }
        saveRecordDBToSP(recordDB);
    }

    private static void saveRecordDBToSP(RecordDB recordDB) {
        MSPV3.getMe().putString(KeysAndValues.TOP10_DB_KEY, new Gson().toJson(recordDB));
    }

    public static RecordDB receiveRecordDBFromSP() {
        RecordDB recordDB = new Gson().fromJson(MSPV3.getMe().getString(KeysAndValues.TOP10_DB_KEY, null), RecordDB.class);
        if (recordDB == null){
            recordDB = new RecordDB();
        }
        return recordDB;
    }
}
