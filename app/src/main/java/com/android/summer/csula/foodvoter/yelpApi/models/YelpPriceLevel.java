package com.android.summer.csula.foodvoter.yelpApi.models;


import java.security.InvalidParameterException;

/**
 * Yelp YelpPriceLevel level values for Business
 */
public enum YelpPriceLevel {

    $("1"), $$("2"), $$$("3"), $$$$("4");

    public String value;

    YelpPriceLevel(String value) {
        this.value = value;
    }

    /**
     * Convert a Yelp YelpPriceLevel String into a Integer String, $=1, $$=2, $$$=3, $$$$=4"
     */
    public static String fromYelpString(String yelpPriceLevel) {
        switch (yelpPriceLevel) {
            case "$":
                return "1";
            case "$$":
                return "2";
            case "$$$":
                return "3";
            case "$$$$":
                return "4";
            default:
                throw new InvalidParameterException("value should be a String $, $$, $$$, or $$$$ but instead it is: " + yelpPriceLevel);
        }
    }

    /**
     * Convert a string integer price value into "Yelp Form": 1=$, 2=$$, 3=$$$, 4=$$$.
     */
    public static String toYelpString(String priceLevel) {
        switch (priceLevel) {
            case "1":
                return "$";
            case "2":
                return "$$";
            case "3":
                return "$$$";
            case "4":
                return "$$$$";
            default:
                throw new InvalidParameterException("value should be a String 1,2,3, or 4");
        }
    }
}
