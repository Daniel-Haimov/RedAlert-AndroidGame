package com.hw.myapplication.data;

public class GameData {
    public final static long  FRAME_SCORE = +320   ;
    public final static long  COIN_SCORE  = +750   ;
    public final static long  STONE_SCORE = -1200  ;

    public final static int   NUM_OF_ROWS = 8 + 1     ; /* 1 for players row */
    public final static int   NUM_OF_COLS = 5         ;
    public final static int   MAX_LIVES = 3           ;

    public final static int STONE_WEIGHT  = 5 ;
    public final static int COIN_WEIGHT   = 2 ;

    public final static String STONE_TAG              = "STONE_TAG"   ;
    public final static String HIT_BY_STONE_TOAST_MSG = "HIT"         ;
    public final static String COIN_TAG               = "COIN_TAG"    ;
    public final static String HIT_BY_COIN_TOAST_MSG  = "COIN"        ;

    public final static int             PLAYER_DRAW_ROTATION      = 0     ;
    public final static int             STONE_DRAW_ROTATION       = 135   ;
    public final static int             HIT_STONE_DRAW_ROTATION   = 135   ;
    public final static int             COIN_DRAW_ROTATION        = 0     ;
    public final static int             HIT_COIN_DRAW_ROTATION    = 0     ;

}
