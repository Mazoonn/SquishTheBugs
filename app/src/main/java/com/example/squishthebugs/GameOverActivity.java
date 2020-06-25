package com.example.squishthebugs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameOverActivity extends AppCompatActivity {

    //Elements
    private Button main, retry;
    private TextView coinsLabel, loseLabel, total_coins_Label, game_overLabel;
    //Data from squish game
    private int coins;
    private boolean lose;
    FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private int coins_in_db;
    private int total_coins;
    private boolean upload_flg = false;
    String difficulty;
    private SoundPlayer soundPlayer;
    //Sound flag
    private boolean sound_flg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        SharedPreferences sp = getSharedPreferences("SquishTheBugs", 0);
        main = findViewById(R.id.button_main_game_over);
        retry = findViewById(R.id.button_retry_game_over);
        coinsLabel = findViewById(R.id.txt_coins_game_over);
        loseLabel = findViewById(R.id.txt_you_lose_game_over);
        game_overLabel = findViewById(R.id.text_game_over);
        total_coins_Label = findViewById(R.id.txt_total_coins);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        soundPlayer = new SoundPlayer(this);
        sound_flg=sp.getBoolean("sound",false);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            coins = (int) b.get("coins");
            lose = (boolean) b.get("lose");
            difficulty = (String) b.get("difficulty");
        }

        ObjectAnimator mover = ObjectAnimator.ofFloat(game_overLabel, "translationY", 400f, 0f);
        mover.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(mover);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(sound_flg)
                {
                    if(lose) soundPlayer.playGameOverSound();
                    else soundPlayer.playWinningSound();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (!lose) {
            coinsLabel.setText("Coins :" + coins);


            //save it in db
            if (currentUser != null && coins != 0 && !upload_flg) {
                upload_flg = true;
                myRef = database.getReference("Users").child(currentUser.getUid()).child("coins");
                final DatabaseReference userToAddRef = database.getReference("Users").child(currentUser.getUid()).child("coins");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Long data = dataSnapshot.getValue(Long.class);
                        if (data != null) {
                            total_coins = data.intValue() + coins;
                            total_coins_Label.setVisibility(View.VISIBLE);
                            total_coins_Label.setText("Total Coins :" + total_coins);
                            userToAddRef.setValue(total_coins);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GameOverActivity.this, "Failed to read value." + error.toException(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            loseLabel.setVisibility(View.VISIBLE);
            coinsLabel.setVisibility(View.INVISIBLE);
        }

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this,
                        SquishGameActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
            }
        });
    }
}
