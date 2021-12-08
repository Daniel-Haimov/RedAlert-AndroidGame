package com.hw.myapplication.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.hw.myapplication.R;


public class Activity_Splash extends AppCompatActivity {

    private static final String TAG = Activity_Splash.class.getSimpleName();

    final int ANIM_DURATION = 4400;

    private ImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();

        splash_IMG_logo.setVisibility(View.INVISIBLE);

        showViewSlideDown(splash_IMG_logo);
    }

    public void showViewSlideDown(final View v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void animationDone() {
        openHomeActivity();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, Activity_Entry.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
    }
}