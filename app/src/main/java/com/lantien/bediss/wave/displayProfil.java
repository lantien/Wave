package com.lantien.bediss.wave;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class displayProfil extends AppCompatActivity {

    private static final String TAG = "FROM DISPLAY PROF : ";

    private String userID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StorageReference mStorageRef;

    Button clickSetProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profil);

        updateProfilText();
    }

    public void updateProfilText() { //
        Bundle b = getIntent().getExtras();

            if( b != null ) {
                String receivedID = b.getString("idProfil");

                updateImage(receivedID);

                Log.e(TAG, "On affiche le profil : " + receivedID);

                DocumentReference docRef = db.collection("users").document(receivedID);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            if (document != null && document.exists()) {

                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                String username = document.getString("username");
                                String name = document.getString("name");
                                String bio = document.getString("bio");
                                String location = document.getString("location");
                                String website = document.getString("website");
                                String currentlyPlaying = document.getString("currentlyPlaying");

                                TextView tvUsername = findViewById(R.id.displayUsername);
                                TextView tvName = findViewById(R.id.displayName);
                                TextView tvLocation = findViewById(R.id.displayLocation);
                                TextView tvBio = findViewById(R.id.displayBio);
                                TextView tvWebsite = findViewById(R.id.displayWebsite);
                                TextView tvCurrentMusic = findViewById(R.id.displayCurrentMusicProf);

                                tvUsername.setText(username);
                                tvName.setText(name);
                                tvLocation.setText(location);
                                tvBio.setText(bio);
                                tvWebsite.setText(website);
                                tvCurrentMusic.setText(currentlyPlaying);

                            } else {

                                Log.d(TAG, "No such document");
                            }

                        } else {

                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(userID.equals(receivedID)) {

                    Button setProf = findViewById(R.id.editProfil);
                    setProf.setVisibility(View.VISIBLE);
                    setListener();
                }

            }
    }

    public void updateImage(String idImg) {

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = mStorageRef.child(idImg + "/1.jpg");

        final long ONE_MEGABYTE = 512 * 512;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Log.e(TAG, "Succes image");
                Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                ImageView imgView = findViewById(R.id.displayPicture);
                imgView.setImageDrawable(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "fail img : " + exception.getMessage());
            }
        });
    }

    private void setListener() {
        clickSetProf = findViewById(R.id.editProfil);

        clickSetProf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.e(TAG, "EDIT CLICKED");
                Intent i = new Intent(getApplicationContext(), setProfil.class);
                startActivityForResult(i, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK)  {
            Log.e(TAG, "ACTIVITY RESULT BACK DONC UPDATE");
            updateImage(userID);
        } else {
            Log.e(TAG, "ERREUR REQUEST CODE");
        }
    }

}
