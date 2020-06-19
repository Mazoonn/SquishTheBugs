package com.example.squishthebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login_button_main);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser=mAuth.getCurrentUser();


        findViewById(R.id.black_bug_main).setOnClickListener(new
        View.OnClickListener()
        {
         @Override
        public void onClick(View v)
         {
             Toast.makeText(MainActivity.this,"Squish the bugs: Slava, Matan, Ashot.",Toast.LENGTH_LONG).show() ;
         }
        });

        findViewById(R.id.options_button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        OptionsActivity.class);
                startActivity(intent);}
        });

        findViewById(R.id.shop_button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ShopActivity.class);
                startActivity(intent);}
        });

        if(currentUser==null)  login.setText(R.string.login);
        else login.setText("Logout");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(currentUser==null)
                {
                    Intent intent = new Intent(MainActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    mAuth.signOut();
                    login.setText(R.string.login);
                }
                }
        });

        findViewById(R.id.play_button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        PlayActivity.class);
                startActivity(intent);}
        });


    }

}
