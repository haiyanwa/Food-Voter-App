package com.android.summer.csula.foodvoter.database;


import android.content.Context;
import android.util.Log;

import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.pushNotifications.MyFirebasePreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserUpdater {

    public static final DatabaseReference USERS_REFERENCE = getUserRef();

    private static final String TAG = UserUpdater.class.getSimpleName();


    private UserUpdater() {}

    public static void updateOnlineStatus(final String userId, final boolean isOnline) {
        USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If you don't check that the user exist, you could insert a new user with
                // a single field, "online => false"
                if (dataSnapshot.hasChild(userId)) {
                    USERS_REFERENCE
                            .child(userId)
                            .child("online")
                            .setValue(isOnline);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    public static void logUserOnline(final Context context, final User user) {
        USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userId = user.getId();

                if (dataSnapshot.hasChild(userId)) {
                    Log.d(TAG, "existing_user: " + user.toString());
                } else {
                    Log.d(TAG, "new_user: " + user.toString());
                    USERS_REFERENCE
                            .child(userId)
                            .setValue(user);
                }

                updateOnlineStatus(userId, true);
                updateToken(userId, MyFirebasePreference.getToken(context));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public static void updateToken(final String userId, final String token) {
        USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    USERS_REFERENCE.child(userId)
                            .child("token")
                            .setValue(token);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    public static void updateFriendToken(final String hostId, final String friendId, final String friendToken) {
        final DatabaseReference myFriendshipRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("friendship")
                .child(hostId);

        myFriendshipRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(friendId)) {
                    Log.d(TAG, "updating user: " + hostId + " 's friend: " + friendId + " 's token to " + friendToken);
                    myFriendshipRef
                            .child(friendId)
                            .child("token")
                            .setValue(friendToken);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private static DatabaseReference getUserRef() {
        return FirebaseDatabase.getInstance().getReference().child("users");
    }
}
