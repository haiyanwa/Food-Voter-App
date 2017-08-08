package com.android.summer.csula.foodvoter;

import java.util.ArrayList;

/**
 * Created by Haiyan on 7/21/17.
 */

public class Restaurant {

    private int id;
    private String name;
    private boolean vote; //yes: true, no: false

    public Restaurant(int id, String name, boolean vote){
        this.id = id;
        this.name = name;
        this.vote =  vote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public static ArrayList<Restaurant> generateRestarantList(){
        ArrayList<Restaurant> restarantList = new ArrayList<Restaurant>();

        restarantList.add(new Restaurant(0, "Elysee Cafe", false));
        restarantList.add(new Restaurant(0, "Meet in Paris Cafe", false));
        restarantList.add(new Restaurant(0, "Northen Cafe", false));
        restarantList.add(new Restaurant(0, "The Wallace", false));
        restarantList.add(new Restaurant(0, "Cafe Vida", false));

        return restarantList;
    }
}

