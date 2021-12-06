package com.hw.myapplication.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class Record {
    private String  playerName  ;
    private long    score       ;
    private double  lat         ;
    private double  lon         ;
}