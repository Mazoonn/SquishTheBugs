package com.example.squishthebugs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.edit_text_email_login);
        password=findViewById(R.id.edit_text_passwrord_login);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.register_button_login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        Register.class);
                startActivity(intent);}
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if(actionId== EditorInfo.IME_ACTION_DONE) findViewById(R.id.login_button_login).callOnClick();
            return false;
        }
    });

        findViewById(R.id.login_button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView errors = findViewById(R.id.txt_errors_login);
                errors.setText("");
                final String email_string=email.getText().toString();
                final String password_string=password.getText().toString();
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";                   //email regular exprastion
                String passwordRegex="^.*[0-9].*$";

                if (email_string.isEmpty() || password_string.isEmpty())
                {
                    errors.setText("You must fill in all the required fields");
                    return;
                }

                if(!email_string.matches(emailRegex))
                {
                    email.setError("Invalid email address");
                    return;
                }

                if(password_string.length()<8)
                {
                    password.setError("Password must be at least 8 characters");
                    return;
                }
                if(password_string.length()>15)
                {
                    password.setError("Password must be maximum 15 characters");
                    return;
                }
                if(!password_string.matches(passwordRegex))
                {
                    password.setError("Password must contain at least one digit");
                    return;
                }

                findViewById(R.id.progressBar_login).setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email_string, password_string)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    errors.setText(task.getException().getMessage());
                                    findViewById(R.id.progressBar_login).setVisibility(View.INVISIBLE);
                                }
                            }

                        });
            }
        });
                findViewById(R.id.button_return_login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);}
                });
    }
}

