package com.midiapp.song;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton image1, image2, crashB;
    ConstraintLayout layout;
    SeekBar seekBar;

    MediaPlayer baseC;
    MediaPlayer baseO;
    MediaPlayer base;
    MediaPlayer o1;
    MediaPlayer o0;
    int tbase = 0, basePlaying=0, t1;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image1 = findViewById(R.id.imageView3);
        image2 = findViewById(R.id.imageView4);
        crashB = findViewById(R.id.imageButton3);
        layout = findViewById(R.id.idLayout);
        seekBar = findViewById(R.id.seekBar);
        baseC = MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.base_c);
        baseO = MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.base_o);
        base = MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.base);
        o1 = MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.o1r);
        o0 = MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.o1);

        final String[] colors = {"#FFFFFF", "#CCCCCC", "#A3A3A3", "#828282", "#686868", "#535353", "#424242", "#353535", "#2A2A2A", "#222222", "Black"};
        image1.setColorFilter(Color.parseColor(colors[10 - seekBar.getProgress() / 10]));
        image2.setColorFilter(Color.parseColor(colors[10 - seekBar.getProgress() / 10]));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                layout.setBackgroundColor(Color.parseColor(colors[progress / 10]));
                if (progress / 10 != 10 - progress / 10) {
                    image1.setColorFilter(Color.parseColor(colors[10 - progress / 10]));
                    image2.setColorFilter(Color.parseColor(colors[10 - progress / 10]));
                    crashB.setColorFilter(Color.parseColor(colors[10 - progress / 10]));

                }
                if (progress > 66&&basePlaying!=1) {
                    if (base.isPlaying()) {
                        base.pause();
                        tbase = base.getCurrentPosition();
                    }
                    if (baseO.isPlaying()) {
                        baseO.pause();
                        tbase = baseO.getCurrentPosition();
                    }
                    baseC.seekTo(tbase);
                    baseC.start();
                    baseC.setLooping(true);
                    Log.i("seek", "playing baseC");
                    basePlaying=1;
                } else if (progress < 66 && progress > 33&&basePlaying!=2) {
                    if (baseO.isPlaying()) {
                        baseO.pause();
                        tbase = baseO.getCurrentPosition();
                    }
                    if (baseC.isPlaying()) {
                        baseC.pause();
                        tbase = baseC.getCurrentPosition();
                    }
                    base.seekTo(tbase);
                    base.start();
                    base.setLooping(true);
                    Log.i("seek", "playing base");
                    basePlaying=2;
                } else if (progress < 33&&basePlaying!=3) {
                    if (base.isPlaying()) {
                        base.pause();
                        tbase = base.getCurrentPosition();
                    }
                    if (baseC.isPlaying()) {
                        baseC.pause();
                        tbase = baseC.getCurrentPosition();
                    }
                    baseO.seekTo(tbase);
                    baseO.start();
                    baseO.setLooping(true);
                    Log.i("seek", "playing 0");
                    basePlaying=3;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        crashB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(MainActivity.super.getBaseContext(), R.raw.crash).start();
                Log.i("seek", "playing crash");
            }
        });
        final boolean[] playingfast = {false};
        image1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                VelocityTracker velocityTracker = VelocityTracker.obtain();

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1000);
                    float y = Math.abs(velocityTracker.getYVelocity());
                    float x = Math.abs(velocityTracker.getXVelocity());
                    float s = (float) Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2));
                    //2000
                    if (s > 2000 && !playingfast[0]) {
                        if (o0.isPlaying())
                            o0.pause();
                        t1 = o0.getCurrentPosition();
                        o1.seekTo(t1);
                        o1.start();
                        o1.setLooping(true);
                        Log.i("b1", "playingFast");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playingfast[0] = true;

                            }
                        }, 5000);
                    } else if (playingfast[0]) {
                        if (o1.isPlaying())
                            o1.pause();
                        t1 = o1.getCurrentPosition();
                        o0.seekTo(t1);
                        o0.start();
                        o0.setLooping(true);
                        Log.i("b1", "playingSlow");
                        playingfast[0] = false;
                    }
                    Log.i("vel1", String.valueOf(s));
                }
                return true;
            }
        });
        image2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                VelocityTracker velocityTracker = VelocityTracker.obtain();

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1000);
                    float y = Math.abs(velocityTracker.getYVelocity());
                    float x = Math.abs(velocityTracker.getXVelocity());
                    float s = (float) Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2));
                    Log.i("vel2", String.valueOf(s));
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        if (base.isPlaying())
            base.stop();
        if (baseO.isPlaying())
            baseO.stop();
        if (baseC.isPlaying())
            baseC.stop();
        if(o0.isPlaying())
            o0.stop();
        if(o1.isPlaying())
            o1.stop();

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}