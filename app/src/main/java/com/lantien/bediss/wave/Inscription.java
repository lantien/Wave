package com.lantien.bediss.wave;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Inscription extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public static final String TAG = "FROM INSCRI : ";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button inscri = (Button) findViewById(R.id.inscri_button);

        inscri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inscription();
            }
        });

        mAuth = FirebaseAuth.getInstance();


    }

    private void inscription() {

        final EditText saisieEmail = findViewById(R.id.inputEmail);
        final EditText saisiePassword = findViewById(R.id.inputPassword);

        String email = saisieEmail.getText().toString();
        String password = saisiePassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            setUserData();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            saisieEmail.setError(task.getException().getMessage());
                            saisieEmail.requestFocus();
                        }

                        // ...
                    }
                });
    }

    private void setUserData() {

        EditText saisiePrenom = findViewById(R.id.inputPrenom);
        EditText saisieNom = findViewById(R.id.inputNom);
        EditText saisieBirthday = findViewById(R.id.inputNaissance);

        User user = new User(saisieNom.getText().toString(),
                saisiePrenom.getText().toString(),
                saisieBirthday.getText().toString());

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user);
    }



}
