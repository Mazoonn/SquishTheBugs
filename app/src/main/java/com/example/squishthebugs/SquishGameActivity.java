package com.example.squishthebugs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SquishGameActivity extends AppCompatActivity
{
    private static final long START_TIME_IN_MILLIS=90000;
    // Elements
    private TextView coinsLabel, startLabel,countDownLabel;
    private ImageView bug,dvora,zvuv,heart1,heart2,heart3;
    private FrameLayout frame;
    //Size
    private int frameWidth;
    private int screenHeight;
    // Speed
    private int black_bug_speed,zvuv_speed,dvora_speed;
    // Coins
    private int coins;
    // Position
    private float blackBugY,zvuvY,dvoraY;
    private float blackBugX,zvuvX,dvoraX;
    // Timers
    private Timer timer;
    private Handler handler = new Handler();
    private CountDownTimer countDown;
    private long game_time=START_TIME_IN_MILLIS;
    // Status
    private boolean start_flg = false;
    private boolean game_over = false;
    private boolean pause = false;
    // Squish Sound
    private SoundPlayer soundPlayer;
    //life
    private int lifes=3;
    //options flags
    private boolean sounds;
    private boolean vibrates;
    //Vibrator
    private Vibrator vibrator;
    //Difficulty
    private String difficulty,mod;
    private double coefficient;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squish_game);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            difficulty=(String)b.get("difficulty");
            mod=(String)b.get("mod");
        }

        if(mod.equals("Short game")) game_time=60000;
        else game_time=120000;

        frame=findViewById(R.id.frameLayout_game_squish);
        coinsLabel = findViewById(R.id.coins_game_squish);
        startLabel = findViewById(R.id.start_txt);
        countDownLabel=findViewById(R.id.count_down_game_squish);

        SharedPreferences sp = getSharedPreferences("SquishTheBugs", 0);

        soundPlayer=new SoundPlayer(this);
        sounds=sp.getBoolean("sound",false);
        vibrates=sp.getBoolean("vibration",false);

        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        heart1=findViewById(R.id.heart1);
        heart2=findViewById(R.id.heart2);
        heart3=findViewById(R.id.heart3);

        bug=findViewById(R.id.bug_game_squish);
        dvora=findViewById(R.id.dvora_toxic_game_squish);
        zvuv=findViewById(R.id.zvuv_maniak_game_squish);

        dvora.setX(-300.0f);
        dvora.setY(-300.0f);

        zvuv.setX(-300.0f);
        zvuv.setY(-300.0f);

        bug.setX(-300.0f);
        bug.setY(-300.0f);

        // Screen Size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenHeight = size.y;

        //Difficulty settings
        switch(difficulty)
        {
            case "Easy":
                coefficient=1.5;
                break;
            case "Medium":
                coefficient=1;
                break;
            case "Hard":
                coefficient=0.5;
                break;
            default:break;
        }

        black_bug_speed = Math.round(screenHeight /(float)(100.0 * coefficient));
        zvuv_speed = Math.round(screenHeight / (float)(50.0 * coefficient)) ;
        dvora_speed = Math.round(screenHeight / (float)(120.0 * coefficient));

        frame.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, final MotionEvent event)
            {
                if(!game_over)
                {
                    if(!start_flg)
                    {
                        startLabel.setVisibility(View.GONE);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    if(sounds) soundPlayer.playHitSound();
                    if(vibrates) vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));

                    blackBugY=-1*bug.getHeight();
                    blackBugX=0;
                    changeCoins(10);
                }
                return false;
            }
        });

        dvora.setOnTouchListener(new View.OnTouchListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    if(sounds) soundPlayer.playFailedSound();
                    if(vibrates) vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
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
                    changeCoins(-20);
                }
                return false;
            }
        });

        zvuv.setOnTouchListener(new View.OnTouchListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!game_over)
                {
                    if(sounds) soundPlayer.playHitSound();
                    if(vibrates) vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    zvuvY=-1*zvuv.getHeight();
                    zvuvX=0;
                    changeCoins(100);
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
            blackBugX = (float)Math.floor(Math.random() * (frameWidth - bug.getWidth()));
        }
        bug.setX(blackBugX);
        bug.setY(blackBugY);
        // Zvuv
        zvuvY -= zvuv_speed;
        if ((zvuvY + zvuv.getHeight())< 0) {
            zvuvY = screenHeight + 5000;
            zvuvX = (float)Math.floor(Math.random() * (frameWidth - zvuv.getWidth()));
        }
        zvuv.setX(zvuvX);
        zvuv.setY(zvuvY);

        // Dvora
        dvoraY -= dvora_speed;
        if ((dvoraY + dvora.getHeight())< 0) {
            dvoraY = screenHeight + 50;
            dvoraX = (float)Math.floor(Math.random() * (frameWidth - dvora.getWidth()));
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
            frameWidth = frame.getWidth();
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
                //Game Over
            }
        }.start();
    }

    private void gameOver()
    {
        game_over=true;
        // Game Over!!
        soundPlayer.releaseSound();
        stopTimers();

        //go to results check if lifes=0 so end the game with no coins

        Intent intent = new Intent(SquishGameActivity.this,
                GameOverActivity.class);
        intent.putExtra("lose",lifes==0);
        intent.putExtra("coins",coins);
        intent.putExtra("difficulty",difficulty);
        intent.putExtra("mod",mod);

        startActivity(intent);
        finish();
    }

    private void updateCountDownText()
    {
        int seconds_rounded=Math.round(game_time/1000);
        int minutes=(int) (seconds_rounded)/60;
        int seconds=(int) (seconds_rounded)%60;

        String timeLeft=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDownLabel.setText("Timer:"+timeLeft);
    }

    @Override
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
        dvora.setVisibility(View.VISIBLE);
        bug.setVisibility(View.VISIBLE);
        zvuv.setVisibility(View.VISIBLE);
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

    private synchronized void changeCoins(int change)
    {
        coins=Math.max((coins+change),0);
        coinsLabel.setText("Coins :"+coins);
    }
}

