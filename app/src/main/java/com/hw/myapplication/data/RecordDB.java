package com.hw.myapplication.data;

import java.util.ArrayList;
import lombok.Data;

@Data
public class RecordDB {
    private ArrayList<Record> records = new ArrayList<>();
}
