package com.android.summer.csula.foodvoter.models;


import com.android.summer.csula.foodvoter.yelpApi.models.Business;

/**
 * Wrapper class used to wrap a business.class and store if its been selected or not
 */
public class BusinessVoteHelper {

    private Business business;
    private boolean selected;


    public BusinessVoteHelper(Business business) {
        this.business = business;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return business.getId();
    }
}
