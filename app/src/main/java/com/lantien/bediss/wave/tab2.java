package com.lantien.bediss.wave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class tab2 extends Fragment {

    private static final String TAG = "FROM TAB2 FRAG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "ON CREATE VIEW TAB2");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        MyAdapter adapter = new MyAdapter(new String[]{"test one", "test two", "test three", "test four", "test five" , "test six" , "test seven"});
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}
