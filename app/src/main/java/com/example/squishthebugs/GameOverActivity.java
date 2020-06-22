package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    //Elements
    private Button main,retry;
    private TextView coinsLabel,loseLabel;
    //Data from squish game
    private int coins;
    private boolean lose;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        main=findViewById(R.id.button_main_game_over);
        retry=findViewById(R.id.button_retry_game_over);
        coinsLabel=findViewById(R.id.txt_coins_game_over);
        loseLabel=findViewById(R.id.txt_you_lose_game_over);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            coins=(int)b.get("coins");
            lose=(boolean)b.get("lose");
        }

        if(!lose)
        {
            coinsLabel.setText("Coins :"+coins);
            //save it in db
        }
        else
        {
            loseLabel.setVisibility(View.VISIBLE);
            coinsLabel.setVisibility(View.INVISIBLE);
        }

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this,
                        MainActivity.class);
                startActivity(intent);}
        });


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this,
                        SquishGameActivity.class);
                startActivity(intent);}
        });


    }
}
