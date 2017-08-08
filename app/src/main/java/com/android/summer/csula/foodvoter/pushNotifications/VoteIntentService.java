package com.android.summer.csula.foodvoter.pushNotifications;


import android.app.IntentService;
import android.content.Intent;

public class VoteIntentService extends IntentService{

    private static final String  TAG = VoteIntentService.class.getSimpleName();


    public VoteIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        VoteTask.executeTask(this, action);
    }
}
