package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;


public class OptionsActivity extends AppCompatActivity {

    Button btnVibrate;
    Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //MediaPlayer mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        //mediaPlayer.setLooping(true);
        //mediaPlayer.start();

        final AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        btnVibrate = findViewById(R.id.vibration_button);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        btnVibrate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vibrator.vibrate(1000);
            }
        });

        findViewById(R.id.main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this,
                        MainActivity.class);
                startActivity(intent);}
        });




    }
}
