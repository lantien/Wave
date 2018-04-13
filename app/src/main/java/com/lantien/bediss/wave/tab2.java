package com.lantien.bediss.wave;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class tab2 extends Fragment {

    private static final String TAG = "FROM TAB2 FRAG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Post> myPost = new ArrayList<Post>();
    private StorageReference mStorageRef;
    Bitmap bmp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "ON CREATE VIEW TAB2");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        getFeedGenerique(rv);

        return rootView;
    }

    public void getFeedGenerique(final RecyclerView rv) {

        CollectionReference docRef = db.collection("generiqueFeed");

        //final Bitmap bmp =


                docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int i = 0;


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.e(TAG, document.getId() + " => " + document.getData());

                                final QueryDocumentSnapshot docu = document;

                                mStorageRef = FirebaseStorage.getInstance().getReference();

                                StorageReference imageRef = mStorageRef.child(document.getString("posterID") + "/1.jpg");

                                final long ONE_MEGABYTE = 512 * 512;
                                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        // Data for "images/island.jpg" is returns, use this as needed

                                        Log.e(TAG, "Succes image FOR POST");
                                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        myPost.add(new Post(docu.getString("Title"),bmp));
                                        MyAdapter adapter = new MyAdapter(myPost);

                                        rv.setAdapter(adapter);

                                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                                        rv.setLayoutManager(llm);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Log.e(TAG, "fail img FOR POST");
                                    }
                                });



                            }





                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
