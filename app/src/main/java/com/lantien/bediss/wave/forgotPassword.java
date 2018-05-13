package com.lantien.bediss.wave;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class forgotPassword extends AppCompatActivity {

    private static final String TAG = "FROM FORGOT PASSWORD";

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.toolbar_forgot);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button submitMailForgot = findViewById(R.id.submit_mail_forgot);

        submitMailForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = findViewById(R.id.inputForgotEmail);


                String emailStr = email.getText().toString();
                if(isValidEmail(emailStr)) {
                    sendEmailForgot(emailStr);
                } else {

                    
                    email.setError("not email");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void sendEmailForgot(String emailstr) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(emailstr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "Email sent.");

                            email.setError("Email sent");
                        } else {
                            Log.e(TAG, "Email NOT sent.");
                            email.setError("Email doesnt exist");
                        }
                    }
                });
    }

}
