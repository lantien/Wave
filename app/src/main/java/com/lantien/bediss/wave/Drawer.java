package com.lantien.bediss.wave;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
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

public class
Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "FROM DRAWER : ";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference mStorageRef;


    private String userID;

    int areDataAvaible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager mPager = findViewById(R.id.content_drawer);

        tabPageAdapter myTabPager = new tabPageAdapter(getSupportFragmentManager());

        mPager.setAdapter(myTabPager);
        tabLayout.setupWithViewPager(mPager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updateDrawer();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void switchActivity() {
        startActivity(new Intent(Drawer.this, Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.e("TAG", "CLICKED LOGOUT");

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_button) {

            FirebaseAuth.getInstance().signOut();
            switchActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(TAG, "CLICKED SOMETHING");

        if (id == R.id.profil) {
            // Handle the camera action
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent i = new Intent(this, show_profil.class);
            i.putExtra("idProfil",userID); //"FJT2AOB6EXPFAfUVT7QUu0IVCgn2"
            startActivityForResult(i, 1);
        }/* else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Log.e(TAG, "manage clicked");
            //startActivity(new Intent(Drawer.this, setProfil.class));
            Intent i = new Intent(this, setProfil.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.nav_share) {

            Log.e(TAG, "share clicked");
            Intent i = new Intent(this, spotify_login.class);
            startActivity(i);

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        setImgDrawer();
        setDrawer();
    }

    private void setDrawer() {

        TextView txtName = findViewById(R.id.displayName);
        TextView txtUsername = findViewById(R.id.displayUsername);
        TextView txtnb_Follow = findViewById(R.id.displayFollowDrawer);
        TextView txtnb_Follower = findViewById(R.id.displayFollowerDrawer);

        txtName.setText(((MyProfil) getApplication()).getName());
        txtUsername.setText("@" + ((MyProfil) getApplication()).getUsername());
        txtnb_Follow.setText(String.valueOf(((MyProfil) getApplication()).getNb_follow()));
        txtnb_Follower.setText(String.valueOf(((MyProfil) getApplication()).getNb_follower()));


    }

    private void setImgDrawer() {
        ImageView imgView = findViewById(R.id.picProfile);
        imgView.setImageDrawable( ((MyProfil) getApplication()).getImage());
    }

    private void setItemClick(int a) {
        if(a == 2) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void updateDrawer() {



        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ((MyProfil) this.getApplication()).setUserID(userID);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = mStorageRef.child(userID + "/1.jpg");

        final long ONE_MEGABYTE = 512 * 512;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                areDataAvaible++;
                Log.e(TAG, "Succes image");


                Drawable image = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                ((MyProfil) getApplication()).setImage(image);
                setImgDrawer();
                setItemClick(areDataAvaible);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "fail img : " + exception.getMessage());
                ((MyProfil) getApplication()).setImage(null);
            }
        });

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                areDataAvaible++;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                        ((MyProfil) getApplication()).setName(document.getString("name"));
                        ((MyProfil) getApplication()).setUsername(document.getString("username"));
                        ((MyProfil) getApplication()).setBio(document.getString("bio"));
                        ((MyProfil) getApplication()).setLocation(document.getString("location"));
                        ((MyProfil) getApplication()).setWebsite(document.getString("website"));
                        ((MyProfil) getApplication()).setNb_follow(document.getLong("nb_follow"));
                        ((MyProfil) getApplication()).setNb_follower(document.getLong("nb_follower"));

                        setDrawer();


                    } else {
                        Log.e(TAG, "No such document");
                    }
                } else {
                    Log.e(TAG, "get failed with ", task.getException());
                }

                setItemClick(areDataAvaible);
            }
        });
    }
}
