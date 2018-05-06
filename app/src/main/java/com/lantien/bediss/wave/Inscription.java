package com.lantien.bediss.wave;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Inscription extends AppCompatActivity {

    final static String DATE_FORMAT = "dd/MM/yyyy";

    static DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    private FirebaseAuth mAuth;

    public static final String TAG = "FROM INSCRI : ";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean isUsernameTaken = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button inscri = (Button) findViewById(R.id.inscri_button);

        inscri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CREATION CALLED");
                isFormValid();
            }
        });

        EditText listenEmail = findViewById(R.id.inputEmail);

        listenEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText saisieEmail = findViewById(R.id.inputEmail);

                String email = saisieEmail.getText().toString();
                if(isValidEmail(email)) {

                    DocumentReference docRef = db.collection("mailList").document(email);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData() + " donc already user");
                                    saisieEmail.setError("Already used");
                                } else {
                                    Log.d(TAG, "mail dispo");
                                    saisieEmail.setError("GOOD");
                                }

                            } else {

                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                } else {

                    Log.d(TAG, "badly formatted email");
                    saisieEmail.setError("badly formatted email");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText listenUsername = findViewById(R.id.inputUsername);

        listenUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText listenUsername = findViewById(R.id.inputUsername);

                String username = listenUsername.getText().toString();
                isUsernameTaken = false;
                if(longEnough(3, username)) {

                    DocumentReference docRef = db.collection("usernameList").document(username);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData() + " donc already user");
                                    listenUsername.setError("Already used");
                                } else {
                                    Log.d(TAG, "username dispo");
                                    isUsernameTaken = true;
                                    listenUsername.setError("GOOD");
                                }

                            } else {

                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                } else {

                    Log.d(TAG, "Too short");
                    listenUsername.setError("Too short");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText listenNom = findViewById(R.id.inputNom);

        listenNom.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText listenUsername = findViewById(R.id.inputNom);

                String name = listenUsername.getText().toString();
                if(longEnough(3, name)) {
                    Log.d(TAG, "long enough ! ");
                    listenUsername.setError("GOOD");


                } else {

                    Log.d(TAG, "Too short");
                    listenUsername.setError("Too short");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText listenName = findViewById(R.id.inputNaissance);

        listenName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText listenDate = findViewById(R.id.inputNaissance);

                String date = listenDate.getText().toString();
                if(isDateValid(date)) {
                    listenDate.setError("GOOD");
                } else {
                    listenDate.setError("BAD DATE");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText listenPassword = findViewById(R.id.inputPassword);

        listenPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText listenPw = findViewById(R.id.inputPassword);

                String password = listenPw.getText().toString();

                if(longEnough(6, password)) {

                    listenPw.setError("GOOD");

                } else {
                    listenPw.setError("Too short");

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText listenConfPassword = findViewById(R.id.inputPassword2);

        listenConfPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final EditText listenPw = findViewById(R.id.inputPassword);
                final EditText listenConfPw = findViewById(R.id.inputPassword2);

                String password = listenPw.getText().toString();
                String confPassword = listenConfPw.getText().toString();

                if(passwordMatch(password, confPassword)) {

                    listenConfPw.setError("PASSWORD MATCHES");

                } else {
                    listenConfPw.setError("PASSWORDS DONT MATCH");

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mAuth = FirebaseAuth.getInstance();


    }

    private static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

   private boolean passwordMatch(String password, String confPassword) {
        if(password.matches(confPassword)) {
            return true;
        } else {
            return false;
        }

   }

   private boolean longEnough(int size, String strToTest) {
        if(strToTest.length()>size) {
            return true;
        } else {
            return false;
        }
   }

    private void isFormValid() {

        EditText saisieEmail = findViewById(R.id.inputEmail);
        EditText saisieUsername = findViewById(R.id.inputUsername);
        EditText saisieNom = findViewById(R.id.inputNom);
        EditText saisieNaissance = findViewById(R.id.inputNaissance);
        EditText saisiePassword = findViewById(R.id.inputPassword);
        EditText saisieConfPassword = findViewById(R.id.inputPassword2);


        String email = saisieEmail.getText().toString();
        String username = saisieUsername.getText().toString();
        String nom = saisieNom.getText().toString();
        String dateNaissance = saisieNaissance.getText().toString();
        String password = saisiePassword.getText().toString();
        String confPassword = saisieConfPassword.getText().toString();

        if(isUsernameTaken) {
            if(passwordMatch(password, confPassword)) {
                if(isDateValid(dateNaissance)) {
                    if(isDateValid(dateNaissance)) {
                        Log.d(TAG, "GOOD FORM CREATION....");
                        inscription(email, password, dateNaissance, username, nom);
                    }
                }
            }
        }


    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void inscription(final String email, String password, final String dateNaiss, final String username, final String nom) {

        final EditText saisieEmail = findViewById(R.id.inputEmail);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            setUserData(username, dateNaiss, nom, email);

                        } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            saisieEmail.setError(task.getException().getMessage());
                            saisieEmail.requestFocus();
                        }

                        // ...
                    }
                });
    }

    private void setUserData(String username, String dateNaiss, String nom, String email) {


        User user = new User(username,
                            nom,
                            dateNaiss, "", "", "", "", "");

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userID).set(user);

        HashMap<String, String> mapEmail = new HashMap<>();
        mapEmail.put("email", email);
        mapEmail.put("userID", userID);

        HashMap<String, String> mapUsername = new HashMap<>();
        mapUsername.put("username", username);
        mapUsername.put("userID", userID);

        HashMap<String, Number> mapFollow = new HashMap<>();
        mapFollow.put("follow_count", 0);

        HashMap<String, Number> mapFollower = new HashMap<>();
        mapFollower.put("follower_count", 0);

        db.collection("follow_"+userID).document("nb_follow").set(mapFollow);
        db.collection("follower_"+userID).document("nb_follower").set(mapFollower);
        db.collection("mailList").document(email).set(mapEmail);
        db.collection("usernameList").document(username).set(mapUsername);
        startActivity(new Intent(Inscription.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    public void backButton(View v) {
        Log.e(TAG, "ANNULER CLICKED");
        super.onBackPressed();
    }



}
