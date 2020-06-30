package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PlayActivity extends AppCompatActivity {
    private RadioButton difficulty_radio_button,mod_radio_button;
    private RadioGroup difficulty,mods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        difficulty=findViewById(R.id.group_difficulty_play);
        mods=findViewById(R.id.radioGroup_mods_play);

        findViewById(R.id.button_return_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,
                        MainActivity.class);
                startActivity(intent);}
        });
        findViewById(R.id.start_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int radio_difficulty_id=difficulty.getCheckedRadioButtonId();
                int radio_mods_id=mods.getCheckedRadioButtonId();
                if(radio_difficulty_id!=-1&&radio_mods_id!=-1)
                {
                    difficulty_radio_button=findViewById(radio_difficulty_id);
                    mod_radio_button=findViewById(radio_mods_id);

                    Intent intent = new Intent(PlayActivity.this,
                            SquishGameActivity.class);
                    intent.putExtra("difficulty",difficulty_radio_button.getText());
                    intent.putExtra("mod",mod_radio_button.getText());
                    startActivity(intent);
                }
            }
        });

    }
}
