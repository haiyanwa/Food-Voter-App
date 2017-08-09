package com.android.summer.csula.foodvoter.models;

/**
 * Created by Haiyan on 8/8/17.
 */

public class VoteResult {
    private String businessName;
    private Integer count;

    public VoteResult(String businessName, Integer count){
        this.businessName = businessName;
        this.count = count;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
