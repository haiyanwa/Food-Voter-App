package com.android.summer.csula.foodvoter.yelpApi.models;


public class YelpAccessToken {

    /**
     * The client id for you app with Yelp.
     */
    private String accessToken;

    /**
     * The access token type. Always returns Bearer.
     */
    private String tokenType;


    /**
     * Represents the number of seconds after which this access token will expire.
     * Right now it's always 15552000, which is 180 days.
     * Time start on INITIAL creation.
     */
    private int expiresIn;

    public YelpAccessToken(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String authorizationHttpHeader() {
        return "Bearer" + " " + accessToken;
    }

    @Override
    public String toString() {
        return "{access_token: " + accessToken + ", token_type: " + tokenType + ", expires_in: " + expiresIn + "}";
    }
}


