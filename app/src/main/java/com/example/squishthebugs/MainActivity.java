package com.example.squishthebugs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button login,play;
    TextView nickname;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String nickname_string;

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        AlertDialog.Builder ad1=new AlertDialog.Builder(this);
        ad1.setMessage("Are you sure you want to exit the Game? ");
        ad1.setCancelable(false);


        ad1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });

        ad1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finishAffinity();
                System.exit(0);
            }
        });
        AlertDialog alert=ad1.create();
        alert.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login_button_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        nickname=findViewById(R.id.text_nickname_main);
        play=findViewById(R.id.play_button_main);
        SharedPreferences sp = getSharedPreferences("SquishTheBugs", 0);
        final SharedPreferences.Editor sedt = sp.edit();
        database = FirebaseDatabase.getInstance();




        if(currentUser!=null)
            {
            nickname_string=sp.getString("nickname",null);
            nickname.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);
            if(nickname_string==null)
            {
                myRef=database.getReference("Users").child(currentUser.getUid()).child("nickname");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        nickname_string = dataSnapshot.getValue(String.class);
                        sedt.putString("nickname",nickname_string);
                        nickname.setText("Hi "+nickname_string+"!");
                        sedt.commit();
                    }
                    @Override
                    public void onCancelled(DatabaseError error)
                    {
                        Toast.makeText(MainActivity.this,"Failed to read value." + error.toException(),Toast.LENGTH_LONG).show();
                        nickname_string="";
                    }
                });
            }
            else nickname.setText("Hi "+nickname_string+"!");
            login.setText("Logout");
        }

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
                    nickname.setVisibility(View.INVISIBLE);
                    sedt.remove("nickname").commit();
                    login.setText(R.string.login);
                    play.setVisibility(View.INVISIBLE);
                    currentUser=null;
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
