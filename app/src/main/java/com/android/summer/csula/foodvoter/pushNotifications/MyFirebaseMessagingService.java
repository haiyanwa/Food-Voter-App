package com.android.summer.csula.foodvoter.pushNotifications;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/*
 * A service that extends FirebaseMessagingService. This is required if you want to
 * do any message handling beyond receiving notifications on apps in the background.
 * To receive notifications in foregrounded apps, to receive data payload,
 * to send upstream messages, and so on, you must extend this service.
 *
 * https://firebase.google.com/docs/cloud-messaging/android/client
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "onMessageReceived => data: " + data);

        PollNotificationUtils.notifyUserOfPollInvites(this, data);
    }
}
