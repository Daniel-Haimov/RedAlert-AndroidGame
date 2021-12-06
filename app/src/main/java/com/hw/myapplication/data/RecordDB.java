package com.hw.myapplication.data;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RecordDB {
    private ArrayList<Record> records = new ArrayList<>();
}
