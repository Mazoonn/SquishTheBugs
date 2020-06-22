package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SquishGameActivity extends AppCompatActivity
{
    // Elements
    private TextView coinsLabel, startLabel;
    private ImageView bug;
    private FrameLayout frame;
    //size
    private int frameHeight;
    private int frameWidth;
    private int screenWidth;
    private int bugSize;
    private int screenHeight;
    // Speed
    private int black_bug_speed;
    // Coins
    private int coins;
    // Position
    private float blackBugY;
    private float blackBugX;
    // Timer
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    // Status
    private boolean start_flg = false;
    // SoundSquish
    //private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squish_game);

        frame=findViewById(R.id.frameLayout_game_squish);
        coinsLabel = findViewById(R.id.coins_game_squish);
        startLabel = findViewById(R.id.start_txt);

        bug=findViewById(R.id.bug_game_squish);
        bug.setX(-80.0f);
        bug.setY(-80.0f);
        // Screen Size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        frameHeight = frame.getHeight();
        frameWidth = frame.getWidth();

        screenWidth = size.x;
        screenHeight = size.y;

        black_bug_speed = Math.round(screenHeight / 100.0f); // 1184 / 60 = 19.733... => 20 1.2sec

        frame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startLabel.setVisibility(View.GONE);
                if(!start_flg)
                {
                    start_flg=true;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    changePos();
                                }
                            });
                        }
                    }, 0, 20);
                    //start the game
                }
                else
                {
                    //do something
                }

            }
        });

        bug.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                bug.setX(-80.0f);
                bug.setY(-80.0f);
                blackBugY=-1*bug.getHeight();
                blackBugX=0;
                return false;
            }
        });


    }
    public void changePos()
    {
        // Black bug
        blackBugY -= black_bug_speed;
        if ((blackBugY + bug.getHeight())< 0) {
            blackBugY = screenHeight + 20;
            blackBugX = (float)Math.floor(Math.random() * (screenWidth - bug.getWidth()));
        }
        bug.setX(blackBugX);
        bug.setY(blackBugY);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            blackBugY=-1*bug.getHeight();
        }
    }
}

