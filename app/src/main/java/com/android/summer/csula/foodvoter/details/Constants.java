package com.android.summer.csula.foodvoter.details;

import com.android.summer.csula.foodvoter.BuildConfig;

/**
 * Created by cowboyuniverse on 8/1/17.
 */




//inside build.properties
//    YelpConsumerKey = "qaDwHX64g95xJe-0lGL1Mg"
//            YelpConsumerSecret = "kEX9HQTHon8niGkz8hWzOW28KNE"
//            YelpToken = "F1ex_Y8C4LDYIU_rf_kYiW7fEK3d7qC4"
//            YelpTokenSecret = "05Fs7pwP1XRjgB3vTvQUOnGn73w"



public class Constants {
    public static final String YELP_CONSUMER_KEY = BuildConfig.YELP_CONSUMER_KEY;
    public static final String YELP_CONSUMER_SECRET = BuildConfig.YELP_CONSUMER_SECRET;
    public static final String YELP_TOKEN = BuildConfig.YELP_TOKEN;
    public static final String YELP_TOKEN_SECRET = BuildConfig.YELP_TOKEN_SECRET;
    public static final String YELP_BASE_URL = "https://api.yelp.com/v2/search?term=food";
    public static final String YELP_LOCATION_QUERY_PARAMETER = "location";
}
