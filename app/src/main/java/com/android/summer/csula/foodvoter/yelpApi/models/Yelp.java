package com.android.summer.csula.foodvoter.yelpApi.models;


import java.util.List;

public class Yelp {

    private int total;
    private List<Business> businesses;

    public Yelp(int total, List<Business> businesses) {
        this.total = total;
        this.businesses = businesses;
    }

    public int getTotal() {
        return total;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }
}

