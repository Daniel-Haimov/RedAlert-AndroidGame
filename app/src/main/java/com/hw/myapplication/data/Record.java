package com.hw.myapplication.data;

public class Record {

    private String  playerName  = "Jane Roe"    ;
    private long    score       = 0             ;
    private double  lat         = 0.0           ;
    private double  lon         = 0.0           ;

    public Record() { }

    public String getPlayerName() {
        return playerName;
    }

    public Record setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public long getScore() {
        return score;
    }

    public Record setScore(long score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }
}