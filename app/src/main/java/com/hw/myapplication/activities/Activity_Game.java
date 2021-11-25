package com.hw.myapplication.activities;



import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hw.myapplication.data.Direction;
import com.hw.myapplication.libs.MyTicker;
import com.hw.myapplication.libs.MyVibrate;
import com.hw.myapplication.libs.NumberFormat;
import com.hw.myapplication.R;

import java.util.Random;

public class Activity_Game extends AppCompatActivity {
    private Bundle savedInstanceState;

    private long        score       = 0      ;
    private final long  FRAME_SCORE = +100   ;
    private final long  COIN_SCORE  = +500   ;
    private final long  STONE_SCORE = -2500  ;

    private final   MyVibrate   vibrator        = MyVibrate.getMe() ;
    private final   Random      rand            = new Random()      ;
    private         MyTicker    ticker                              ;
    private final   Handler     handler         = new Handler()     ;
    private         Runnable    timerRunnable                       ;

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
    private ImageButton           panel_BTN_left                                                  ;
    private ImageButton           panel_BTN_right                                                 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_game);
        findViews();
        initGame();
        settings();
    }

    private void settings() {
        /* Default Values TODO */
        // TODO
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
        findButtonsView();
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

    private void findButtonsView() {
        panel_BTN_left      = findViewById(R.id.panel_BTN_left );
        panel_BTN_right     = findViewById(R.id.panel_BTN_right);
    }

    // ~~~ INIT ~~~

    private void initGame() {
        initPlayer();
        initButtons();
        initTicker();
    }

    //init the player in begin in the middle
    private void initPlayer() {
        player_pos = NUM_OF_COLS / 2;
        fixPlayerRow();
    }

    private void initButtons() {
        panel_BTN_left.setOnClickListener(v -> {
            MovementController(Direction.LEFT);
        });

        panel_BTN_right.setOnClickListener(v -> {
            MovementController(Direction.RIGHT);
        });
    }

    private void MovementController(int direction){
        player_pos += direction;
        if (player_pos < 0 || player_pos >= NUM_OF_COLS){
            player_pos -= direction;
            return;
        }

        ImageView playerOld = panel_IMG_players[player_pos - direction];
        playerOld.setImageResource(R.drawable.ic_player );
        playerOld.setVisibility(View.INVISIBLE          );
        checkHits();
    }


    private void initTicker() {
        timerRunnable = () -> {
            updateClockView();
            handler.postDelayed(timerRunnable, MyTicker.DEF_DELAY);
        };
        ticker = new MyTicker(handler, timerRunnable);
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
        // TODO add sound
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
        // TODO add sound
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
        //TODO move to TOP10 Activity, update score in bundle
        for (ImageView heart: panel_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
            lives = MAX_LIVES;
        }
    }



}