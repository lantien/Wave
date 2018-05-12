package com.lantien.bediss.wave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        ImageView mImageView = findViewById(R.id.setImagePost);

        mImageView.setImageDrawable(((MyProfil) getApplication()).getImage());


        final EditText setNom = findViewById(R.id.setNom);
        setNom.setText(((MyProfil) getApplication()).getName());

        final EditText setBio = findViewById(R.id.setBio);
        setBio.setText(((MyProfil) getApplication()).getBio());

        final EditText setLocation = findViewById(R.id.setLocalisation);
        setLocation.setText(((MyProfil) getApplication()).getLocation());

        final EditText setWebsite = findViewById(R.id.setWebLink);
        setWebsite.setText(((MyProfil) getApplication()).getWebsite());

        Button signInButton = findViewById(R.id.setChanges);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d(TAG, "Upload clicked");

                DocumentReference myProf = db.collection("users").document(userID);

                Map<String, Object> data = new HashMap<>();

                String name = setNom.getText().toString();
                String bio = setBio.getText().toString();
                String location = setLocation.getText().toString();
                String website = setWebsite.getText().toString();

                ((MyProfil) getApplication()).setName(name);
                ((MyProfil) getApplication()).setBio(bio);
                ((MyProfil) getApplication()).setLocation(location);
                ((MyProfil) getApplication()).setWebsite(website);

                data.put("name", name);
                data.put("bio", bio);
                data.put("location", location);
                data.put("website", website);


                myProf.update(data);

                    if(myImage != null) {
                        uploadImage();
                    }

                    goBack();
            }
        });

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
            }
        });

    }

    public void openGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            myImage = data.getData( );
            updateImage();
            Toast.makeText(this, "Get image URI", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateImage() {
        ImageView mImageView = findViewById(R.id.setImagePost);
        Bitmap bitmap;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myImage);
            mImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "UpdateImage IOExce", Toast.LENGTH_SHORT).show();
        }


    }

    private void goBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","cc");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



}
