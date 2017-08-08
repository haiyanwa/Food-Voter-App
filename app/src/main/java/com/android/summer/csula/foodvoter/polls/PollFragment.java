package com.android.summer.csula.foodvoter.polls;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.summer.csula.foodvoter.ListActivity;
import com.android.summer.csula.foodvoter.polls.models.Poll;

/**
 * This abstract fragment will implement the PollsAdaper.OnClickListener.
 */
public abstract class PollFragment extends Fragment implements PollsAdapter.OnPollClickListener {

    @Override
    public void onPollClick(Poll poll) {
        Intent intent = ListActivity.newIntent(getContext(), poll);
        startActivity(intent);
    }
}
