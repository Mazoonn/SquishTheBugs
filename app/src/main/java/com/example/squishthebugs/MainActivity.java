package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.black_bug).setOnClickListener(new
        View.OnClickListener()
        {
         @Override
        public void onClick(View v)
         {
             Toast.makeText(MainActivity.this,"Squish the bugs: Slava, Matan, Ashot.",Toast.LENGTH_LONG).show() ;
         }
        });

        findViewById(R.id.options_buttom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        OptionsActivity.class);
                startActivity(intent);}
        });

        findViewById(R.id.shop_buttom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ShopActivity.class);
                startActivity(intent);}
        });

        findViewById(R.id.login_buttom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);}
        });


    }

}
