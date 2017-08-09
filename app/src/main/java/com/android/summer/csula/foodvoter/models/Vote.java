package com.android.summer.csula.foodvoter.models;

/**
 * Created by Haiyan on 8/5/17.
 */

public class Vote {
    private String userId;
    private String businessId;

    public Vote() {}

    public Vote(String id, String businessId) {
        this.userId = id;
        this.businessId = businessId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "userId='" + userId + '\'' +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}
