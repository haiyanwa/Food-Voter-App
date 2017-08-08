package com.android.summer.csula.foodvoter.pushNotifications;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * A service that extends FirebaseInstanceIdService to handle the creation, rotation, and updating
 * of registration tokens. This is required for sending to specific devices or for creating
 * device groups.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "onTokenRefresh => token: " + refreshedToken);

        MyFirebasePreference.setToken(this, refreshedToken);
    }
}
