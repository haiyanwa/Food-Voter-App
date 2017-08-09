package com.android.summer.csula.foodvoter.models;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This object records all the users vote. It could be use to retrive what a user vote or record (add/upate)
 * a user vote
 */
public class VoteRecord {

    /**
     * A Map of userId=>businessId . The values indicated which business the key voted for.
     */
    public Map<String, String> map = new HashMap<>();


    public void recordVote(Vote vote) {
        map.put(vote.getUserId(), vote.getBusinessId());
    }

    public void removeVote(Vote vote) {
        map.remove(vote.getUserId());
    }

    /**
     * Return all recorded votes
     */
    public Set<Map.Entry<String, String>> getEntries() {
        return map.entrySet();
    }

    @Nullable
    public Vote voteOf(String userId) {
        if (map.containsKey(userId)) {
            return new Vote(userId, map.get(userId));
        }

        return null;
    }
}
