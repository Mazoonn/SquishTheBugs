package com.example.squishthebugs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email, password, confirm_password, birthday;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.text_view_email);
        password = findViewById(R.id.text_view_password);
        confirm_password = findViewById(R.id.text_view_confirm_password);
        birthday=findViewById(R.id.text_view_birthday);
        register = findViewById(R.id.register_button);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";                   //email regular exprastion
                final String email_string=email.getText().toString();
                final String birthday_string=birthday.getText().toString();
                String password_string=password.getText().toString();
                String confirm_password_string=confirm_password.getText().toString();
                TextView register_errors=findViewById(R.id.register_errors);
                ProgressBar pb=findViewById(R.id.progressBar);

                register_errors.setText("");

                if(email_string.isEmpty()||password_string.isEmpty()||confirm_password_string.isEmpty()||birthday_string.isEmpty())
                {
                    register_errors.setText("you must fill in all the required fields");
                    return;
                }
                if(!email_string.matches(emailRegex))
                {
                    email.setError("invalid email address");
                    return;
                }
                if(password_string.length()<6)
                {
                    password.setError("password must be at least 6 characters");
                    return;
                }
                if(!password_string.equals(confirm_password_string))
                {
                    confirm_password.setError("password and confirm password don't match");
                    return;
                }
                //check if date is valid

                pb.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email_string, password_string)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    // Sign in success, update UI with the signed-in user's information
                                    // Write a message to the database
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String uid=user.getUid();
                                    User aUser = new User(email_string,birthday_string,0);

                                    DatabaseReference userToAddRef = database.getReference("Users").child(uid);
                                    userToAddRef.setValue(aUser);

                                    Intent intent = new Intent(Register.this,
                                            MainActivity.class);
                                    startActivity(intent);

                                }

                                else
                                {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });
        findViewById(R.id.button_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
