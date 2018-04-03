package com.lantien.bediss.wave;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class setProfil extends AppCompatActivity {

    private static final String TAG = "FROM SETPROFIL : ";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    Uri myImage;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profil);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Button signInButton = findViewById(R.id.setChanges);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d(TAG, "Upload clicked");

                    EditText nom = findViewById(R.id.inputNom);
                    EditText prenom = findViewById(R.id.inputPrenom);

                    String Snom = nom.getText().toString();
                    String Sprenom = prenom.getText().toString();

                    User user = new User(Snom, Sprenom, "03/03/2000");

                    db.collection("users").document(userID).set(user);

                    if(myImage != null) {
                        uploadImage();
                    } else {
                        goBack();
                    }



            }
        });
/*
        Button getImage = findViewById(R.id.imageButtn);

        getImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);

            }
        });*/

    }

    private void uploadImage() {
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child(userID + "/1.jpg");
        UploadTask uploadTask = riversRef.putFile(myImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d(TAG, "Upload succes");
                goBack();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            myImage = data.getData( );
            Toast.makeText(this, "Get image URI", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void goBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","cc");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



}
