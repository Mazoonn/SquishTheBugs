package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
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
