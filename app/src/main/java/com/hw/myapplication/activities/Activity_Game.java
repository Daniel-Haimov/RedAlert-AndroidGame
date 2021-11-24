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
import com.hw.myapplication.R;

import java.util.Random;

public class Activity_Game extends AppCompatActivity {

    private final int MAX_LIVES = 3 ;
    private int lives = MAX_LIVES   ;
    private int score = 0           ;

    private final MyVibrate vibrator = MyVibrate.getMe();

    private final int NUM_OF_ROWS = 8 + 1   ; // 1 for players row
    private final int NUM_OF_COLS = 5       ;
    private boolean row_with_stone = true   ;
    private final Random rand = new Random()      ;
    private int player_pos;

    private MyTicker ticker                 ;
    final Handler handler = new Handler()   ;
    private Runnable timerRunnable          ;

    private final int             STONE_DRAW_ROTATION = 135                                       ;

    private final ImageView[]     panel_IMG_hearts    = new ImageView[MAX_LIVES]                  ;
    private final ImageView[][]   panel_IMG_stones    = new ImageView[NUM_OF_ROWS][NUM_OF_COLS]   ;
    private final ImageView[]     panel_IMG_players   = new ImageView[NUM_OF_COLS]                ;
    private TextView              panel_TXT_score                                                 ;
    private ImageButton           panel_BTN_left                                                  ;
    private ImageButton           panel_BTN_right                                                 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        initGame();
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
        Glide.with(Activity_Game.this).load(R.drawable.bg).centerCrop().into(panel_IMG_BG);
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

    private void initPlayer() {
        player_pos = NUM_OF_COLS / 2;
        fixPlayerRow();
    }

    private void initButtons() {
        panel_BTN_left.setOnClickListener(v -> {
            if (player_pos > 0){
                MovementController(Direction.LEFT);
            }
        });

        panel_BTN_right.setOnClickListener(v -> {
            if (player_pos < NUM_OF_COLS - 1){
                MovementController(Direction.RIGHT);
            }
        });
    }

    private void MovementController(int direction){
        panel_IMG_players[player_pos    ].setImageResource(R.drawable.ic_player );
        panel_IMG_players[player_pos    ].setVisibility(View.INVISIBLE          );
        player_pos += direction;
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
        updateScoreBy(10);
        updateView();
        updateFirstRow();
        checkHits();
    }

    private void updateView() {
        for (int i = NUM_OF_ROWS - 1; i > 0; i--)
            for (int j = 0; j < NUM_OF_COLS; j++) {
                Drawable id = panel_IMG_stones[i - 1][j].getDrawable();
                panel_IMG_stones[i][j].setImageDrawable(id);
                panel_IMG_stones[i][j].setVisibility(panel_IMG_stones[i - 1][j].getVisibility() );
                panel_IMG_stones[i][j].setRotation  (panel_IMG_stones[i - 1][j].getRotation()   );
            }
    }

    private void checkHits() {
        // If there is object in player position
        ImageView obj = panel_IMG_stones[NUM_OF_ROWS -1][player_pos];
        if (obj.getVisibility() == View.VISIBLE) {
            //TODO check if obj is stone or coin
            if (obj.getRotation() == STONE_DRAW_ROTATION){
                hitByStone();
            }else{
                hitByCoin();
            }
        }else
            hitView(false);
    }

    private void hitByStone() {
        hitView(true);
        showToast("HIT");
        vibrator.Vibrate();
        updateScoreBy(-100);
        lives--;
        if (lives <= 0)//remove the '=' after changes unlimited lives
            gameOver();
        else
            panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
    }

    private void hitByCoin(){
        // TODO
        updateScoreBy(50);
        showToast("COIN");
    }

    private void showToast(String str) {
        Toast.makeText(Activity_Game.this, str, Toast.LENGTH_SHORT).show();
    }

    private void hitView(boolean hit) {
        if(hit){
            int HIT_DRAW_ROTATION = 0;
            panel_IMG_players[player_pos].setRotation       (HIT_DRAW_ROTATION);
            panel_IMG_players[player_pos].setImageResource  (R.drawable.ic_hit_png  );
        }else{
            fixPlayerRow();
        }
    }

    private void fixPlayerRow() {
        int PLAYER_DRAW_ROTATION = 0;
        panel_IMG_players[player_pos].setRotation       (PLAYER_DRAW_ROTATION);
        panel_IMG_players[player_pos].setImageResource  (R.drawable.ic_player);
        panel_IMG_players[player_pos].setVisibility     (View.VISIBLE        );
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
        panel_IMG_stones[0][col].setVisibility(View.VISIBLE);
    }

    private void InvisibleFirstRow() {
        for (int i = 0; i < NUM_OF_COLS; i++)
            panel_IMG_stones[0][i].setVisibility(View.INVISIBLE);
    }

    private void updateScoreBy(int value){
        score = Math.max(score + value, 0);
        String str = convertScore(score);

        panel_TXT_score.setText(str);
    }

    private String convertScore(int score) {
        String str = "";
        String LEFT   = "";
        String RIGHT  = "";
        if(score >= 1000000){
            LEFT    += score / 1000000;
            RIGHT   += ((score % 1000000) / 10000);
            str     += LEFT + "." + RIGHT + "M";
        }else if(score >= 1000){
            LEFT    += score / 1000;
            RIGHT   += ((score % 1000) / 10);
            str     += LEFT + "." + RIGHT + "K";
        }else{
            str += score;
        }
        return str;
    }

    private void gameOver() {
        //TODO
        for (ImageView heart: panel_IMG_hearts) {
            heart.setVisibility(View.VISIBLE);
            lives = MAX_LIVES;
        }
    }



}