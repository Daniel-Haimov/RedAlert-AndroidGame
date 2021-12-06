package com.hw.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.hw.myapplication.data.GameData;
import com.hw.myapplication.data.KeysAndValues;
import com.hw.myapplication.fragments.control.Fragment_ACC;
import com.hw.myapplication.fragments.control.Fragment_Buttons;
import com.hw.myapplication.fragments.control.GameController;
import com.hw.myapplication.libs.MyTicker;
import com.hw.myapplication.libs.MyVibrate;
import com.hw.myapplication.libs.NumberFormat;
import com.hw.myapplication.R;

import java.util.Random;

public class Activity_Game extends AppCompatActivity {

    private Bundle bundle;

    private final   Random      rand            = new Random()      ;

    private         MyTicker    ticker                              ;
    private final   Handler     handler         = new Handler()     ;
    private         Runnable    timerRunnable                       ;
    private         long        speed           = MyTicker.DEF_DELAY;

    private long        score       = 0      ;
    private int         lives = GameData.MAX_LIVES       ;
    private int         player_pos              ;
    private boolean     row_with_stone = true   ;

    private final ImageView[]     panel_IMG_hearts    = new ImageView[GameData.MAX_LIVES]                  ;
    private final ImageView[][]   panel_IMG_stones    = new ImageView[GameData.NUM_OF_ROWS][GameData.NUM_OF_COLS]   ;
    private final ImageView[]     panel_IMG_players   = new ImageView[GameData.NUM_OF_COLS]                ;
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
            initControllerFragment(new Fragment_Buttons(), cb);
            speed = bundle.getLong(KeysAndValues.SETTINGS_GAME_SPEED_KEY);
        }else{
            initControllerFragment(new Fragment_ACC(), cb);
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
        for (int i = 0; i < GameData.MAX_LIVES; i++) {
            panel_IMG_hearts[i] =
                    findViewById(getResources().getIdentifier(
                            "panel_IMG_heart_" + i,
                            "id",
                            getPackageName()));
        }
    }

    private void findPlayersView() {
        for (int i = 0; i < GameData.NUM_OF_COLS; i++) {
            panel_IMG_players[i] =
                    findViewById(getResources().getIdentifier(
                            "panel_IMG_player_" + i,
                            "id",
                            getPackageName()));
        }
    }

    private void findGameObjectsView() {
        for (int i = 0; i < GameData.NUM_OF_ROWS - 1; i++) {
            for (int j = 0; j < GameData.NUM_OF_COLS; j++) {
                panel_IMG_stones[i][j] =
                        findViewById(getResources().getIdentifier(
                                "panel_IMG_stone" + i + "_" + j,
                                "id",
                                getPackageName()));
                Log.d("stone", "i = " + i+ " j = " + j);
            }
        }
        //
        panel_IMG_stones[GameData.NUM_OF_ROWS - 1] = panel_IMG_players;
    }


    // ~~~ INIT ~~~

    private void initGame() {
        initPlayer();
        initTicker();
        initSound();
    }

    //init the player in begin in the middle
    private void initPlayer() {
        player_pos = GameData.NUM_OF_COLS / 2;
        fixPlayerRow();
    }

    private void initControllerFragment(GameController fragment, CallBack_MovePlayer cb) {
        fragment.setActivity(this);
        fragment.setCallBackMovePlayer(cb);
        getSupportFragmentManager().beginTransaction().add(R.id.panel_FRM_Controller, (Fragment) fragment).commit();
    }

    private void initSound() {
        stoneHitSound = MediaPlayer.create(this, R.raw.stone_sound);
        coinHitSound  = MediaPlayer.create(this, R.raw.coin_sound );
    }

    private void MovementController(int direction){
        int newPos = player_pos + direction;
        if (newPos < 0 || newPos >= GameData.NUM_OF_COLS){
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

//    private void updateTimerRunnableSpeed(){
//        timerRunnable = () -> {
//            updateClockView();
//            handler.postDelayed(timerRunnable, speed);
//        };
//        ticker.updateDelay(timerRunnable);
//    }

    // ~~~ UPDATE VIEW LOGIC ~~~

    private void updateClockView() {
        updateScoreBy(GameData.FRAME_SCORE);
        updateView();
        updateFirstRow();
        checkHits();
    }

    private void updateView() {
        for (int i = GameData.NUM_OF_ROWS - 1; i > 0; i--)
            for (int j = 0; j < GameData.NUM_OF_COLS; j++) {
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
        ImageView obj = panel_IMG_stones[GameData.NUM_OF_ROWS -1][player_pos];
        if (obj.getVisibility() == View.VISIBLE) {
            if (obj.getTag().equals(GameData.STONE_TAG)) {
                hitByStone();
            } else {
                hitByCoin();
            }
        } else {
            fixPlayerRow();
        }
    }

    private void hitByStone() {
        hitView(GameData.STONE_TAG);
        showToast(GameData.HIT_BY_STONE_TOAST_MSG);
        updateScoreBy(GameData.STONE_SCORE);
        MyVibrate.getMe().Vibrate();
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
        hitView(GameData.COIN_TAG);
        showToast(GameData.HIT_BY_COIN_TOAST_MSG);
        updateScoreBy(GameData.COIN_SCORE);
        coinHitSound.start();
    }

    private void showToast(String str) {
        Toast.makeText(Activity_Game.this, str, Toast.LENGTH_SHORT).show();
    }

    private void hitView(String tag) {
        ImageView player = panel_IMG_players[player_pos];
        if(tag.equals(GameData.STONE_TAG)){
            player.setRotation       (GameData.HIT_STONE_DRAW_ROTATION);
            player.setImageResource  (R.drawable.ic_stone_hit);
        }else{
            player.setRotation       (GameData.HIT_COIN_DRAW_ROTATION);
            player.setImageResource  (R.drawable.ic_coin_hit);
        }
    }

    private void fixPlayerRow() {
        ImageView player = panel_IMG_players[player_pos];
        player.setRotation       (GameData.PLAYER_DRAW_ROTATION);
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
        int col = rand.nextInt(GameData.NUM_OF_COLS);
        int obj = rand.nextInt(GameData.STONE_WEIGHT + GameData.COIN_WEIGHT);
        if (obj < GameData.STONE_WEIGHT) { // generate stone
            panel_IMG_stones[0][col].setImageResource   (R.drawable.ic_stone    )   ;
            panel_IMG_stones[0][col].setRotation        (GameData.STONE_DRAW_ROTATION    )   ;
            panel_IMG_stones[0][col].setTag             (GameData.STONE_TAG              )   ;
        }else { // generate coin
            panel_IMG_stones[0][col].setImageResource   (R.drawable.ic_coin     )   ;
            panel_IMG_stones[0][col].setRotation        (GameData.COIN_DRAW_ROTATION     )   ;
            panel_IMG_stones[0][col].setTag             (GameData.COIN_TAG               )   ;
        }
        panel_IMG_stones[0][col].setVisibility(View.VISIBLE);
    }

    private void InvisibleFirstRow() {
        for (int i = 0; i < GameData.NUM_OF_COLS; i++)
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