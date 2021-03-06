package com.lantien.bediss.wave;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "FROM LOGIN ACTIVITY : ";
    private static final int RC_SIGN_IN = 9001;

    EditText email;
    EditText password;

    private FirebaseAuth mAuth;

    GoogleApiClient mGoogleApiClient;


    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "FROM EVEN AUTH onAuthStateChanged:signed_in:" + user.getUid());
                    switchActivity();
                } else {
                    // User is signed out
                    Log.e(TAG, "FROM EVEN AUTH onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        ///////////////GOOGLE SIGN IN CFG///////////////////////////////////
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d(TAG, "BUTTON CLICKED");
                signIn();
            }
        });

        EditText emailEnter = findViewById(R.id.email);
        final EditText passwordEnter = findViewById(R.id.password);

        emailEnter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    passwordEnter.requestFocus();
                }
                return false;
            }
        });

        passwordEnter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    signInWithEmail();
                }
                return false;
            }
        });

        Button buttonLogin = findViewById(R.id.login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmail();
            }
        });


       /* Button buttonInscri = findViewById(R.id.inscri_button);

        buttonInscri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Inscription.class));//SET INSCRIPTION
            }
        });*/

        mAuth.addAuthStateListener(mAuthListener);

    }

    public void backButton(View v) {
        Log.e(TAG, "ANNULER CLICKED");
        super.onBackPressed();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void switchActivity() {
        startActivity(new Intent(Login.this, Drawer.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void switchToForgotPassword(View v) {
        startActivity(new Intent(Login.this, forgotPassword.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void signInWithEmail() {

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        boolean isSet = true;

        if(emailText.matches("")) {

            email.setError("SAISIE MAIL");
            email.requestFocus();
            isSet = false;
        }

        if(passwordText.matches("")) {

            password.setError("SAISIE PASSWORD");
            isSet = false;
        }


        if(isSet) {
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "signInWithEmail:failed", task.getException());

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {

                                    email.setError("doesnt exist");
                                    email.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {

                                    password.setError("wrong password");
                                    password.requestFocus();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }

                            } else if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "SUCCED AUTH",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }

    //////////////////////////////////////////////////GOOGLE FUNC SIGN IN///////////////////////////
    private void signIn() {
        Log.d(TAG, "CREATE ACT RESULT");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "GOOD RC");
            if (result.isSuccess()) {
                Log.d(TAG, "I M HERE");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d(TAG, "FAIL GOOGLE" + result.getStatus());
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}
