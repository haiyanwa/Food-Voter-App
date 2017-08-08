package com.android.summer.csula.foodvoter.polls;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v7.widget.RecyclerView.*;

public class AllPollsFragment extends PollFragment {

    private static final String FIREBASE_NODE_POLLS = "polls";
    private static final String TAG = AllPollsFragment.class.getSimpleName();

    private PollsAdapter pollsAdapter;
    private DatabaseReference pollsRef;
    private ChildEventListener childEventListener;

    private View view;


    public static AllPollsFragment newInstance() {
        return new AllPollsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        pollsRef = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE_POLLS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_all_polls, container, false);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        initializeRecyclerView(view);
        attachValueEventListener();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        detachValueEventListener();
    }

    private void initializeRecyclerView(View view) {
        // You inherited  OnPollClickListener from the abstract class PollFragment
        pollsAdapter = new PollsAdapter(this, false);

        LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_all_polls);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pollsAdapter);
    }

    private void attachValueEventListener() {
        Log.d(TAG, "Attaching childEventListener to Firebase Database References...");
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Poll poll = dataSnapshot.getValue(Poll.class);
                pollsAdapter.addPoll(poll);
                Log.d(TAG, "adding a new poll!: " + poll.getTitle());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        pollsRef.addChildEventListener(childEventListener);
    }

    private void detachValueEventListener() {
        if (childEventListener != null) {
            Log.d(TAG, "Detaching childEventListener to Firebase Database References...");
            pollsRef.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
}
