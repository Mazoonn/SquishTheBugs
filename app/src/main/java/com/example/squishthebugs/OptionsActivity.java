package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SharedPreferences sp = getSharedPreferences("SquishTheBugs", 0);
        final SharedPreferences.Editor sedt = sp.edit();
        Switch sound = findViewById(R.id.switch_sound);
        Switch vibration = findViewById(R.id.switch_vibration);

        sound.setChecked(sp.getBoolean("sound",false));
        vibration.setChecked(sp.getBoolean("vibration",false));

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sedt.putBoolean("sound",isChecked);
                sedt.commit();
        }
    });

        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sedt.putBoolean("vibration",isChecked);
                sedt.commit();
            }
        });

        findViewById(R.id.information).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView txtView = findViewById(R.id.text_information);
                if (txtView.getVisibility() == View.VISIBLE)
                    txtView.setVisibility(View.INVISIBLE);
                else
                    txtView.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.button_return).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this,
                        MainActivity.class);
                startActivity(intent);}
        });
    }
}
