package com.android.summer.csula.foodvoter.demos;


import android.util.Log;

import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class FirebasePollBusinesses {

    public static final String SELECTED_POLL_ID = "-KqjVxfO7TwT8Aqrv7_R";
    public static final String POLLS = "polls";


    public static void execute(final OnFirebaseResultListener listener) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(POLLS)
                .child(SELECTED_POLL_ID);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Poll poll = dataSnapshot.getValue(Poll.class);
                listener.onResult(poll.getBusinesses());

                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        ref.addValueEventListener(valueEventListener);

    }

    public interface OnFirebaseResultListener {
        void onResult(List<Business> businesses);
    }
}


