package com.example.squishthebugs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SquishGameActivity extends AppCompatActivity
{
    private static final long START_TIME_IN_MILLIS=60000;
    // Elements
    private TextView coinsLabel, startLabel,countDownLabel;
    private ImageView bug,dvora,zvuv,heart1,heart2,heart3;
    private FrameLayout frame;
    //size
    private int frameHeight;
    private int frameWidth;
    private int screenWidth;
    private int bugSize;
    private int screenHeight;
    // Speed
    private int black_bug_speed,zvuv_speed,dvora_speed;
    // Coins
    private int coins;
    // Position
    private float blackBugY,zvuvY,dvoraY;
    private float blackBugX,zvuvX,dvoraX;
    // Timer
    private Timer timer;
    private Handler handler = new Handler();
    private CountDownTimer countDown;
    private long game_time=START_TIME_IN_MILLIS;
    // Status
    private boolean start_flg = false;
    private boolean game_over = false;
    private boolean pause = false;
    // SoundSquish
    //life
    private int lifes=3;
    //private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squish_game);

        frame=findViewById(R.id.frameLayout_game_squish);
        coinsLabel = findViewById(R.id.coins_game_squish);
        startLabel = findViewById(R.id.start_txt);
        countDownLabel=findViewById(R.id.count_down_game_squish);

        heart1=findViewById(R.id.heart1);
        heart2=findViewById(R.id.heart2);
        heart3=findViewById(R.id.heart3);

        bug=findViewById(R.id.bug_game_squish);
        dvora=findViewById(R.id.dvora_toxic_game_squish);
        zvuv=findViewById(R.id.zvuv_maniak_game_squish);

        dvora.setX(-80.0f);
        dvora.setY(-80.0f);

        zvuv.setX(-80.0f);
        zvuv.setY(-80.0f);

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

        black_bug_speed = Math.round(screenHeight / 100.0f);
        zvuv_speed = Math.round(screenHeight / 50.0f);
        dvora_speed = Math.round(screenHeight / 120.0f);



        frame.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, final MotionEvent event)
            {
                if(!game_over)
                {
                    startLabel.setVisibility(View.GONE);
                    if(!start_flg)
                    {
                        start_flg=true;
                        countDownLabel.setVisibility(View.VISIBLE);
                        startGame();
                    }
                }
                return false;
            }
        }
        );


        bug.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    blackBugY=-1*bug.getHeight();
                    blackBugX=0;
                    coins+=10;
                    coinsLabel.setText("Coins :"+coins);
                }
                return false;
            }
        });

        dvora.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    switch (lifes)
                    {
                        case 3:
                            heart1.setVisibility(View.INVISIBLE);
                        break;
                        case 2:
                            heart2.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            heart3.setVisibility(View.INVISIBLE);
                            break;
                        default: break;
                    }
                    lifes--;
                    if(lifes==0)
                    {
                        //game over and lose the game no coins
                        gameOver();
                    }
                    dvoraY=-1*bug.getHeight();
                    dvoraX=0;
                    coins-=20;
                    coinsLabel.setText("Coins :"+Math.max(coins,0));
                }
                return false;
            }
        });

        zvuv.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    zvuvY=-1*zvuv.getHeight();
                    zvuvX=0;
                    coins+=100;
                    coinsLabel.setText("Coins :"+coins);
                }
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
        // Zvuv
        zvuvY -= zvuv_speed;
        if ((zvuvY + zvuv.getHeight())< 0) {
            zvuvY = screenHeight + 2000;
            zvuvX = (float)Math.floor(Math.random() * (screenWidth - zvuv.getWidth()));
        }
        zvuv.setX(zvuvX);
        zvuv.setY(zvuvY);

        // Dvora
        dvoraY -= dvora_speed;
        if ((dvoraY + dvora.getHeight())< 0) {
            dvoraY = screenHeight + 50;
            dvoraX = (float)Math.floor(Math.random() * (screenWidth - dvora.getWidth()));
        }
        dvora.setX(dvoraX);
        dvora.setY(dvoraY);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !pause && !start_flg)
        {
            zvuvY=-1*zvuv.getHeight();
            dvoraY=-1*dvora.getHeight();
            blackBugY=-1*bug.getHeight();
        }
    }

    private void startCountDown()
    {
        countDown=new CountDownTimer(game_time,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                game_time=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish()
            {
                gameOver();
                Toast.makeText(SquishGameActivity.this,"Game Over time left.",Toast.LENGTH_LONG).show() ;
                //Game Over
            }
        }.start();
    }

    private void gameOver()
    {
        //score for the next screen;

        //check if timer is over or life is over
        game_over=true;
        // Game Over!!

        stopTimers();

        bug.setX(-80.0f);
        bug.setY(-80.0f);

        zvuv.setX(-80.0f);
        zvuv.setY(-80.0f);

        dvora.setX(-80.0f);
        dvora.setY(-80.0f);
        //go to results check if life=0 so end the game with no coins
    }

    private void updateCountDownText()
    {
        int seconds_rounded=Math.round(game_time/1000);
        int minutes=(int) (seconds_rounded)/60;
        int seconds=(int) (seconds_rounded)%60;

        String timeLeft=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDownLabel.setText("Timer:"+timeLeft);
    }
    //hit check
    /*public void hitCheck(float x,float y)
    {
        float x1=bug.getX();
        float x2=x1+bug.getWidth();
        float y1=bug.getY();
        float y2=y1+bug.getHeight();

        if (x1 <= x && x <= x2 &&
        y1 <= y && y <= y2)
        {
            blackBugY=-1*bug.getHeight();
            blackBugX=0;
            coins+=10;
            coinsLabel.setText("Coins :"+coins);
        }
    }*/

    public void onBackPressed()
    {
        //super.onBackPressed();
        pause=true;

        stopTimers();

        AlertDialog.Builder ad1=new AlertDialog.Builder(this);
        ad1.setMessage("Are you sure you want finish the Game? ");
        ad1.setCancelable(false);

        ad1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                pause=false;
                startGame();
            }
        });

        ad1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent intent = new Intent(SquishGameActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert=ad1.create();
        alert.show();
    }

    private void stopTimers()
    {
        if (timer != null)
        {
            timer.cancel();
            timer=null;
        }

        if (countDown != null)
        {
            countDown.cancel();
            countDown=null;
        }
    }
    private void startGame()
    {
        timer =new Timer();
        startCountDown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        if(!pause) changePos();
                    }
                });
            }
        }, 0, 20);
    }
}

