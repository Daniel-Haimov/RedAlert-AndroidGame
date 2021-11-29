package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hw.myapplication.callbacks.CallBack_MovePlayer;
import com.hw.myapplication.data.KeysAndValues;
import com.hw.myapplication.fragments.Fragment_ACC;
import com.hw.myapplication.fragments.Fragment_Buttons;
import com.hw.myapplication.libs.MyTicker;
import com.hw.myapplication.libs.MyVibrate;
import com.hw.myapplication.libs.NumberFormat;
import com.hw.myapplication.R;

import java.util.Random;

public class Activity_Game extends AppCompatActivity {

    private Bundle bundle;

    private long        score       = 0      ;
    private final long  FRAME_SCORE = +320   ;
    private final long  COIN_SCORE  = +750   ;
    private final long  STONE_SCORE = -1200  ;

    private final   MyVibrate   vibrator        = MyVibrate.getMe() ;
    private final   Random      rand            = new Random()      ;

    private         MyTicker    ticker                              ;
    private final   Handler     handler         = new Handler()     ;
    private         Runnable    timerRunnable                       ;
    private         long        speed           = MyTicker.DEF_DELAY;

    private final int   NUM_OF_ROWS = 8 + 1     ; /* 1 for players row */
    private final int   NUM_OF_COLS = 5         ;
    private final int   MAX_LIVES = 3           ;
    private int         lives = MAX_LIVES       ;
    private int         player_pos              ;
    private boolean     row_with_stone = true   ;

    private final int STONE_WEIGHT  = 5 ;
    private final int COIN_WEIGHT   = 2 ;

    private final String STONE_TAG              = "STONE_TAG"   ;
    private final String HIT_BY_STONE_TOAST_MSG = "HIT"         ;
    private final String COIN_TAG               = "COIN_TAG"    ;
    private final String HIT_BY_COIN_TOAST_MSG  = "COIN"        ;

    private final int             PLAYER_DRAW_ROTATION      = 0     ;
    private final int             STONE_DRAW_ROTATION       = 135   ;
    private final int             HIT_STONE_DRAW_ROTATION   = 135   ;
    private final int             COIN_DRAW_ROTATION        = 0     ;
    private final int             HIT_COIN_DRAW_ROTATION    = 0     ;

    private final ImageView[]     panel_IMG_hearts    = new ImageView[MAX_LIVES]                  ;
    private final ImageView[][]   panel_IMG_stones    = new ImageView[NUM_OF_ROWS][NUM_OF_COLS]   ;
    private final ImageView[]     panel_IMG_players   = new ImageView[NUM_OF_COLS]                ;
    private TextView              panel_TXT_score                                                 ;

    private MediaPlayer stoneHitSound   ;
    private MediaPlayer coinHitSound    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.bundle = getIntent().getBundleExtra(KeysAndValues.BUNDLE_KEY);

        findViews();
        settings();
        initGame();
    }

    private void settings() {
        CallBack_MovePlayer cb = new CallBack_MovePlayer() {
            @Override
            public void movePlayer(int direction) {
                MovementController(direction);
            }

            @Override
            public void gameSpeed(long speed) {
                updateGameSpeed(speed);
            }
        };
        speed = KeysAndValues.SETTINGS_GAME_SPEED_DEFAULT;
        String controller = bundle.getString(KeysAndValues.SETTINGS_PLAYER_CONTROL_KEY, KeysAndValues.SETTINGS_PLAYER_CONTROL_DEFAULT);
        if (controller.equals(KeysAndValues.SETTINGS_PLAYER_CONTROL_BUTTONS)){
            initButtonsFragment(cb);
            speed = bundle.getLong(KeysAndValues.SETTINGS_GAME_SPEED_KEY);
        }else{
            initAccFragment(cb);
        }
    }

    private void updateGameSpeed(long speed) {
        this.speed = speed;

    }

    @Override
    protected void onStart() {
        super.onStart();
        ticker.startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ticker.stopTicker();
    }

    // ~~~ VIEW ~~~

    private void findViews() {
        setBackGround();
        findHeartsView();
        panel_TXT_score = findViewById(R.id.panel_TXT_score);
        findPlayersView();
        findGameObjectsView();
    }

    private void setBackGround() {
        ImageView panel_IMG_BG = findViewById(R.id.panel_IMG_backGround);
        Glide.with(Activity_Game.this).load(R.drawable.bg_game_5roads).centerCrop().into(panel_IMG_BG);
    }

    private void findHeartsView(){
        for (int i = 0; i < MAX_LIVES; i++) {
            panel_IMG_hearts[i] =
                    findViewById(getResources().getIdentifier(
                            "panel_IMG_heart_" + i,
                            "id",
                            getPackageName()));
        }
    }

    private void findPlayersView() {
        for (int i = 0; i < NUM_OF_COLS; i++) {
            panel_IMG_players[i] =
                    findViewById(getResources().getIdentifier(
                            "panel_IMG_player_" + i,
                            "id",
                            getPackageName()));
        }
    }

    private void findGameObjectsView() {
        for (int i = 0; i < NUM_OF_ROWS - 1; i++) {
            for (int j = 0; j < NUM_OF_COLS; j++) {
                panel_IMG_stones[i][j] =
                        findViewById(getResources().getIdentifier(
                                "panel_IMG_stone" + i + "_" + j,
                                "id",
                                getPackageName()));
                Log.d("stone", "i = " + i+ " j = " + j);
            }
        }
        //
        panel_IMG_stones[NUM_OF_ROWS - 1] = panel_IMG_players;
    }


    // ~~~ INIT ~~~

    private void initGame() {
        initPlayer();
        initTicker();
        initSound();
    }

    //init the player in begin in the middle
    private void initPlayer() {
        player_pos = NUM_OF_COLS / 2;
        fixPlayerRow();
    }

    private void initAccFragment(CallBack_MovePlayer cb) {
        /* ACC Control Fragment */
        Fragment_ACC fragmentACC = new Fragment_ACC();
        fragmentACC.setActivity(this);
        fragmentACC.setCallBackMovePlayer(cb);
        getSupportFragmentManager().beginTransaction().add(R.id.panel_FRM_Controller, fragmentACC).commit();
    }

    private void initButtonsFragment(CallBack_MovePlayer cb) {
        /* Buttons Fragment */
        Fragment_Buttons fragmentButtons = new Fragment_Buttons();
        fragmentButtons.setActivity(this);
        fragmentButtons.setCallBackMovePlayer(cb);
        getSupportFragmentManager().beginTransaction().add(R.id.panel_FRM_Controller, fragmentButtons).commit();
    }

    private void initSound() {
        stoneHitSound = MediaPlayer.create(this, R.raw.stone_sound);
        coinHitSound  = MediaPlayer.create(this, R.raw.coin_sound );
    }

    private void MovementController(int direction){
        int newPos = player_pos + direction;
        if (newPos < 0 || newPos >= NUM_OF_COLS){
            return;
        }
        player_pos = newPos;

        ImageView playerOld = panel_IMG_players[player_pos - direction];
        playerOld.setImageResource(R.drawable.ic_player );
        playerOld.setVisibility(View.INVISIBLE          );
        checkHits();
    }


    private void initTicker() {
        timerRunnable = () -> {
            updateClockView();
            handler.postDelayed(timerRunnable, speed);
        };
        ticker = new MyTicker(handler, timerRunnable);
    }

    private void updateTimerRunnableSpeed(){
        timerRunnable = () -> {
            updateClockView();
            handler.postDelayed(timerRunnable, speed);
        };
        ticker.updateDelay(timerRunnable);
    }


    // ~~~ UPDATE VIEW LOGIC ~~~

    private void updateClockView() {
        updateScoreBy(FRAME_SCORE);
        updateView();
        updateFirstRow();
        checkHits();
    }

    private void updateView() {
        for (int i = NUM_OF_ROWS - 1; i > 0; i--)
            for (int j = 0; j < NUM_OF_COLS; j++) {
                ImageView objOld = panel_IMG_stones[i - 1][j];
                ImageView objNew = panel_IMG_stones[i][j];

                objNew.setImageDrawable (objOld.getDrawable()   )   ;
                objNew.setVisibility    (objOld.getVisibility() )   ;
                objNew.setRotation      (objOld.getRotation()   )   ;
                objNew.setTag           (objOld.getTag()        )   ;
            }
    }

    private void checkHits() {
        // If there is object in player position
        ImageView obj = panel_IMG_stones[NUM_OF_ROWS -1][player_pos];
        if (obj.getVisibility() == View.VISIBLE) {
            if (obj.getTag().equals(STONE_TAG)) {
                hitByStone();
            } else {
                hitByCoin();
            }
        } else {
            fixPlayerRow();
        }
    }

    private void hitByStone() {
        hitView(STONE_TAG);
        showToast(HIT_BY_STONE_TOAST_MSG);
        updateScoreBy(STONE_SCORE);
        vibrator.Vibrate();
        lowerLife();
        stoneHitSound.start();
    }

    private void lowerLife() {
        lives--;
        if (lives <= 0) {//remove the '=' after changes unlimited lives
            gameOver();
        } else {
            panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
        }
    }

    private void hitByCoin(){
        hitView(COIN_TAG);
        showToast(HIT_BY_COIN_TOAST_MSG);
        updateScoreBy(COIN_SCORE);
        coinHitSound.start();
    }

    private void showToast(String str) {
        Toast.makeText(Activity_Game.this, str, Toast.LENGTH_SHORT).show();
    }

    private void hitView(String tag) {
        ImageView player = panel_IMG_players[player_pos];
        if(tag.equals(STONE_TAG)){
            player.setRotation       (HIT_STONE_DRAW_ROTATION);
            player.setImageResource  (R.drawable.ic_stone_hit);
        }else{
            player.setRotation       (HIT_COIN_DRAW_ROTATION);
            player.setImageResource  (R.drawable.ic_coin_hit);
        }
    }

    private void fixPlayerRow() {
        ImageView player = panel_IMG_players[player_pos];
        player.setRotation       (PLAYER_DRAW_ROTATION);
        player.setImageResource  (R.drawable.ic_player);
        player.setVisibility     (View.VISIBLE        );
    }

    private void updateFirstRow() {
        if (row_with_stone)
            randomizeFirstRow();
        else
            InvisibleFirstRow();
        row_with_stone = !row_with_stone;
    }

    private void randomizeFirstRow() {
        int col = rand.nextInt(NUM_OF_COLS);
        int obj = rand.nextInt(STONE_WEIGHT + COIN_WEIGHT);
        if (obj < STONE_WEIGHT) { // generate stone
            panel_IMG_stones[0][col].setImageResource   (R.drawable.ic_stone    )   ;
            panel_IMG_stones[0][col].setRotation        (STONE_DRAW_ROTATION    )   ;
            panel_IMG_stones[0][col].setTag             (STONE_TAG              )   ;
        }else { // generate coin
            panel_IMG_stones[0][col].setImageResource   (R.drawable.ic_coin     )   ;
            panel_IMG_stones[0][col].setRotation        (COIN_DRAW_ROTATION     )   ;
            panel_IMG_stones[0][col].setTag             (COIN_TAG               )   ;
        }
        panel_IMG_stones[0][col].setVisibility(View.VISIBLE);
    }

    private void InvisibleFirstRow() {
        for (int i = 0; i < NUM_OF_COLS; i++)
            panel_IMG_stones[0][i].setVisibility(View.INVISIBLE);
    }

    private void updateScoreBy(long value){
        score = Math.max(score + value, 0L);
        String str = NumberFormat.format(score);
        panel_TXT_score.setText(str);
    }

    private void gameOver() {
        Intent myIntent = new Intent(this, Activity_Top10.class);
        bundle.putLong(KeysAndValues.PLAYER_SCORE_KEY, score);
        myIntent.putExtra(KeysAndValues.BUNDLE_KEY, bundle);
        startActivity(myIntent);
        finish();
    }

}