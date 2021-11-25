package com.hw.myapplication.data;


import java.util.Date;


public class KeysAndValues {

    /* KEYS */
    public final static String BUNDLE_KEY        = "BUNDLE_KEY"       ;

    public final static String SETTINGS_PLAYER_CONTROL_KEY        = "SETTINGS_PLAYER_CONTROL_KEY"       ;

    public final static String SETTINGS_GAME_SPEED_KEY        = "SETTINGS_GAME_SPEED_KEY"     ;

    public final static String PLAYER_USERNAME_KEY        = "PLAYER_USERNAME_KEY"     ;
    public final static String PLAYER_SCORE_KEY           = "PLAYER_SCORE_KEY"        ;
    public final static String PLAYER_LOCATION_LAT_KEY    = "PLAYER_LOCATION_LAT_KEY" ;
    public final static String PLAYER_LOCATION_LON_KEY    = "PLAYER_LOCATION_LON_KEY" ;

    public final static String TOP10_DB_KEY = "TOP10_DB_KEY";


    /* VALUES */
    public final static String SETTINGS_PLAYER_CONTROL_BUTTONS    = "SETTINGS_PLAYER_CONTROL_BUTTONS"   ;
    public final static String SETTINGS_PLAYER_CONTROL_ACC        = "SETTINGS_PLAYER_CONTROL_ACC"       ;

    public final static int SETTINGS_GAME_SPEED_SLOW       = 1000    ;
    public final static int SETTINGS_GAME_SPEED_FAST       = 700     ;


    /* DEFAULTS VALUES */
    public final static String    SETTINGS_PLAYER_CONTROL_DEFAULT    = SETTINGS_PLAYER_CONTROL_BUTTONS;
    public final static int       SETTINGS_GAME_SPEED_DEFAULT        = SETTINGS_GAME_SPEED_SLOW       ;
    public final static String    PLAYER_USERNAME_DEFAULT            = new Date().toString()          ;
    public final static long      PLAYER_SCORE_DEFAULT               = -1L                            ;
    public final static double    PLAYER_LOCATION_LAT_DEFAULT        = 0.0D                           ;
    public final static double    PLAYER_LOCATION_LON_DEFAULT        = 0.0D                           ;
    public final static RecordDB  TOP10_DB_DEFAULT                   = new RecordDB()                 ;
    public final static int       TOP10_MAX_LIST_SIZE                = 10                             ;


}
