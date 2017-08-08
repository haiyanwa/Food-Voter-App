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
import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InvitedToPollFragment extends PollFragment {

    private static final String KEY_USER_ID = "user_id";

    private RecyclerView recyclerView;
    private PollsAdapter pollsAdapter;
    private DatabaseReference pollsRef;
    private ChildEventListener childEventListener;
    private String userId;

    private View view;

    public static InvitedToPollFragment newInstance(String userId) {
        InvitedToPollFragment fragment = new InvitedToPollFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(KEY_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_polls, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeRecyclerView();
        initializeDatabase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanupDatabase();
    }

    private void initializeRecyclerView() {
        // You inherited OnClickListner from PollFragment. True to show a invited icon instead
        pollsAdapter = new PollsAdapter(this, true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_all_polls);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pollsAdapter);
    }

    private void initializeDatabase() {
        childEventListener = getChildEventListener();
        pollsRef = FirebaseDatabase.getInstance().getReference().child("polls");
        pollsRef.addChildEventListener(childEventListener);
    }

    private void cleanupDatabase() {
        if (childEventListener != null) {
            pollsRef.removeEventListener(childEventListener);
        }
    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Poll poll = dataSnapshot.getValue(Poll.class);

                if (isUserInvited(poll)) {
                    pollsAdapter.addPoll(poll);
                }
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
    }

    private boolean isUserInvited(Poll poll) {
        return isUserTheAuthor(poll) || isUserAVoter(poll);
    }


    private boolean isCurrentUser(User voter) {
        return voter.getId().equals(userId);
    }

    private boolean isUserTheAuthor(Poll poll) {
        return isCurrentUser(poll.getAuthor());
    }

    private boolean isUserAVoter(Poll poll) {
        for (User voter : poll.getVoters()) {
            if (isCurrentUser(voter)) {
                return true;
            }
        }
        return false;
    }
}
