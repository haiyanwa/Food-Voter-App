package com.android.summer.csula.foodvoter.models;

/**
 * Created by Haiyan on 8/8/17.
 */

public class VoteResult {
    private String businessId;
    private Integer count;

    public VoteResult(String businessId, Integer count){
        this.businessId = businessId;
        this.count = count;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessName(String businessName) {
        this.businessId = businessName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
