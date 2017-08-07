package com.android.summer.csula.foodvoter.polls;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.android.summer.csula.foodvoter.HomeActivity;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Yelp;
import com.android.summer.csula.foodvoter.yelpApi.tasks.RequestYelpSearchTask;
import com.android.summer.csula.foodvoter.yelpApi.utils.BusinessUtilities;

import java.net.URL;
import java.util.List;


public class PollIntentService extends IntentService {

    private static final String TAG = PollIntentService.class.getSimpleName();
    private static final String EXTRA_POLL = "poll";


    public static Intent newIntent(Context context, Poll poll) {
        Intent intent = new Intent(context, PollIntentService.class);
        intent.putExtra(EXTRA_POLL, poll);
        return intent;
    }

    public PollIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Poll poll = (Poll) intent.getSerializableExtra(EXTRA_POLL);

        // Build the Yelp Search URL, make the call, get the result, randomize a subset of the result,
        // and insert into the poll object.  Write the result onto firebase
        try {
            URL url = PollUtilities.toURL(poll);
            Yelp yelp = RequestYelpSearchTask.execute(url);
            List<Business> businesses = yelp.getBusinesses();
            List<Business> selections = BusinessUtilities.getRandoms(businesses);
            poll.setBusinesses(selections);
            PollUtilities.writeToFirebase(poll);
            broadcastPollWriteStatus(HomeActivity.PollBroadcastReceiver.ACTION_SUCCESSFUL_WRITE);

        } catch (Exception e) {
            e.printStackTrace();
            broadcastPollWriteStatus(HomeActivity.PollBroadcastReceiver.ACTION_UNSUCCESSFUL_WRITE);
        }
    }

    private void broadcastPollWriteStatus(String action) {
        Intent updateBusinessIntent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateBusinessIntent);
    }
}
