package com.android.summer.csula.foodvoter.yelpApi.utils;


import com.android.summer.csula.foodvoter.yelpApi.models.Business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusinessUtilities {
    // TODO: allow users to input the max number of selections

    public static final int MAX_SELECTIONS = 7;

    /**
     * Return a random selections of bossiness.
     */
    public static List<Business> getRandoms(List<Business> businesses) {
        List<Business> copyOfBusinesses = new ArrayList<>(businesses);
        Collections.shuffle(copyOfBusinesses);

        if (copyOfBusinesses.size() < MAX_SELECTIONS) {
            return copyOfBusinesses;
        }

        return copyOfBusinesses.subList(0, MAX_SELECTIONS);
    }
}
