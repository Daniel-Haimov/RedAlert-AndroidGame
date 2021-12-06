package com.hw.myapplication.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true) @Data
public class Record {
    private String  playerName  ;
    private long    score       ;
    private double  lat         ;
    private double  lon         ;
}