package com.example.squishthebugs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.TimeUtils;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password, confirm_password, birthday,nickname;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.text_view_email_register);
        password = findViewById(R.id.text_view_password_register);
        nickname=findViewById(R.id.text_view_nickname_register);
        confirm_password = findViewById(R.id.text_view_confirm_password_register);
        birthday=findViewById(R.id.text_view_birthday_register);
        register = findViewById(R.id.register_button_register);
        final Calendar myCalendar = Calendar.getInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(Register.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }
        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId==EditorInfo.IME_ACTION_NEXT)
                {
                    InputMethodManager inputManager = (InputMethodManager) Register.this.getSystemService(Register.this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(Register.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    birthday.callOnClick();
                }
                return false;
            }

        });
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";                   //email regular exprestion
                String nicknameRegex = "^[A-Z][a-zA-Z0-9_]*$";
                String passwordRegex="^.*[0-9].*$";
                final String email_string=email.getText().toString();
                final String birthday_string=birthday.getText().toString();
                String password_string=password.getText().toString();
                String confirm_password_string=confirm_password.getText().toString();
                final String nickname_string=nickname.getText().toString();
                final TextView register_errors=findViewById(R.id.register_errors_register);
                final ProgressBar pb=findViewById(R.id.progressBar_register);

                register_errors.setText("");

                if(email_string.isEmpty()||password_string.isEmpty()||confirm_password_string.isEmpty()||birthday_string.isEmpty()||nickname_string.isEmpty())
                {
                    register_errors.setText("You must fill in all the required fields");
                    return;
                }
                if(!email_string.matches(emailRegex))
                {
                    email.setError("Invalid email address");
                    return;
                }
                if(!nickname_string.matches(nicknameRegex))
                {
                    nickname.setError("Nickname must begin with capital letter");
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
                if(!password_string.equals(confirm_password_string))
                {
                    confirm_password.setError("Password and confirm password don't match");
                    return;
                }
                if(Calendar.getInstance().get(Calendar.YEAR)-myCalendar.get(Calendar.YEAR)<6)
                {
                    register_errors.setText("You must be at least 6 years old");
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
                                    User aUser = new User(email_string,nickname_string,birthday_string,0);

                                    DatabaseReference userToAddRef = database.getReference("Users").child(uid);
                                    userToAddRef.setValue(aUser);

                                    Intent intent = new Intent(Register.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else
                                {
                                    // If sign in fails, display a message to the user.
                                    register_errors.setText(task.getException().getMessage());
                                    pb.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
            }
        });
        findViewById(R.id.button_return_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateLabel(Calendar calendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(calendar.getTime()));
    }
}
