package com.android.summer.csula.foodvoter.data;

import com.android.summer.csula.foodvoter.models.Details;

import java.util.ArrayList;

/**
 * Created by cowboyuniverse on 7/23/17.
 */

public class RestaurantDataTest_DetailsActivity {

    private ArrayList<Details> restaurantList;
    public RestaurantDataTest_DetailsActivity(){
        restaurantList = new ArrayList<Details>();
        restaurantList.add(new Details("Batman Pizza", "32 Batman Ave, Sunbury VIC 3429, Australia", "+61-3-9744-1717"));
        restaurantList.add(new Details("Pizza Hut", "4351 S Central Ave, Los Angeles, CA 90011", "323-233-1444"));
    }

    public ArrayList<Details> getRestaurantList() {
        return restaurantList;
    }
}
