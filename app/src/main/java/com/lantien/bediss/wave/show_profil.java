package com.lantien.bediss.wave;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class show_profil extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    private static final String TAG = "FROM DISPLAY PROF : ";

    private String userID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StorageReference mStorageRef;

    Button clickSetProf;

    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profil);

        mFunctions = FirebaseFunctions.getInstance();

        updateProfilText();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_profil, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position==0) {
                return new tab2();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void updateProfilText() { //
        Bundle b = getIntent().getExtras();

        if( b != null ) {
            final String receivedID = b.getString("idProfil");

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
                            //TextView tvCurrentMusic = findViewById(R.id.displayCurrentMusicProf);

                            tvUsername.setText(username);
                            tvName.setText(name);
                            tvLocation.setText(location);
                            tvBio.setText(bio);
                            tvWebsite.setText(website);
                            //tvCurrentMusic.setText(currentlyPlaying);

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
            } else { // should we show follow button or not etc...

                DocumentReference followRef = db.collection("follow_"+userID).document(receivedID);
                followRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Button followButton = findViewById(R.id.followerButton);
                                followButton.setVisibility(View.VISIBLE);
                                Log.d(TAG, "FOLLOWER");

                                unFollow(followButton, userID, receivedID);

                            } else {
                                Button followButton = findViewById(R.id.followButton);
                                followButton.setVisibility(View.VISIBLE);
                                setFollowListener(followButton, userID, receivedID);
                                Log.d(TAG, "NOT FOLLOWER");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());

                        }
                    }
                });

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

    private void setFollowListener(final Button follow, final String userID, final String theOneID) {



        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "FOLLOW CLICKED");

                //final DocumentReference sfDocRef = db.collection("follow").document(userID);
                final DocumentReference followedRef = db.collection("follower").document(theOneID);

                Map<String, Object> data = new HashMap<>();
                data.put("iFollow",theOneID);
                data.put("id", userID);

                Task<String> myTask =  mFunctions
                        .getHttpsCallable("followSomeone")
                        .call(data)
                        .continueWith(new Continuation<HttpsCallableResult, String>() {
                            @Override
                            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                // This continuation runs on either success or failure, but if the task
                                // has failed then getResult() will throw an Exception which will be
                                // propagated down.
                                String result = (String) task.getResult().getData();
                                return result;
                            }
                        });

                myTask.addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "succes call !" + task.getResult());
                            switchFollow(theOneID);
                        } else {
                            Log.d(TAG, "Fail call !" + task.getException());
                        }
                    }
                });
            }
        });
    }

    private void switchFollow(String receivedID) {
        Button followerButton = findViewById(R.id.followerButton);
        Button followButton = findViewById(R.id.followButton);

        if(followerButton.getVisibility() == View.VISIBLE) {

            followerButton.setVisibility(View.INVISIBLE);
            followButton.setVisibility(View.VISIBLE);
            setFollowListener(followButton, userID, receivedID);

        } else {
            followerButton.setVisibility(View.VISIBLE);
            followButton.setVisibility(View.INVISIBLE);
            unFollow(followButton, userID, receivedID);
        }

    }





    private void unFollow(Button unFollowButton, final String userID, final String theOneID) {

        Log.d(TAG, "UNFOLLOW CLICKED");

        unFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DocumentReference followDocRef = db.collection("follow_"+userID).document(theOneID);
                final DocumentReference nbFollowDocRef = db.collection("follow_"+userID).document("nb_follow");

                final DocumentReference followerDocRef = db.collection("follower_"+theOneID).document(userID);
                final DocumentReference nbFollowerDocRef = db.collection("follower_"+theOneID).document("nb_follower");

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                        DocumentSnapshot followDocNb = transaction.get(nbFollowDocRef);
                        DocumentSnapshot followerDocNb = transaction.get(nbFollowerDocRef);

                        double nb_follow = followDocNb.getDouble("follow_count") - 1;
                        double nb_follower = followerDocNb.getDouble("follower_count") - 1;

                        Map<String, Object> unFollowData = new HashMap<>();
                        unFollowData.put("follower_count",nb_follower);
                        unFollowData.put("unfollowID",theOneID);


                        transaction.update(nbFollowDocRef, "follow_count", nb_follow);
                        transaction.update(nbFollowerDocRef,unFollowData);

                        transaction.delete(followDocRef);
                        transaction.delete(followerDocRef);

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Transaction success!");
                        switchFollow(theOneID);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Transaction failure.", e);
                            }
                        });

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
            //updateImage(userID);
        } else {
            Log.e(TAG, "ERREUR REQUEST CODE");
        }
    }
}
