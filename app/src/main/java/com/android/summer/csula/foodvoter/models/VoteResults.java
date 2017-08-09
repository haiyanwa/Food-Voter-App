package com.android.summer.csula.foodvoter.models;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This object records all the users vote. It could be use to retrive what a user vote or record (add/upate)
 * a user vote
 */
public class VoteResults {

    private Map<String, String> map = new HashMap<>();


    public void recordVote(Vote vote) {
        map.put(vote.getUserId(), vote.getBusinessId());
    }

    @Nullable
    public Vote voteOf(String userId) {
        if (map.containsKey(userId)) {
            return new Vote(userId, map.get(userId));
        }

        return null;
    }
}
