package com.android.summer.csula.foodvoter.yelpApi.tasks;


import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.summer.csula.foodvoter.yelpApi.models.Yelp;
import com.android.summer.csula.foodvoter.yelpApi.models.YelpAccessToken;
import com.android.summer.csula.foodvoter.yelpApi.utils.NetworkUtils;
import com.android.summer.csula.foodvoter.yelpApi.utils.YelpJsonUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Helper class use to retrieve a Yelp object (contains business info)
 */
public class RequestYelpSearchTask {

    /**
     * Make a call to Yelp Fashion API. Please use the SearchBuilder class to create the URL because
     * the HTTP requires a special Yelp Authorization that the SearchBuilder class handles.
     */
    @Nullable
    public static Yelp execute(URL searchUrl) {
        Yelp yelp = null;

        try {
            YelpAccessToken token = RequestYelpAccessTokenTask.execute();
            String jsonResponse = NetworkUtils.getJsonResponseFromHttpUrl(
                    searchUrl, NetworkUtils.GET_REQUEST, token.authorizationHttpHeader());
            yelp = YelpJsonUtil.parse(jsonResponse);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return yelp;
    }

    /**
     * This builder class is use to assist you in building a Yelp search URL object.
     */
    public static class SearchBuilder {

        private static final String YELP_SEARCH_BASE_URL = "https://api.yelp.com/v3/businesses/search";

        private static final String LOCATION_PARAM = "location";
        private static final String LATITUDE_PARAM = "latitude";
        private static final String LONGITUDE_PARAM = "longitude";

        private static final String LIMIT_PARAM = "limit";
        private static final String TERM_PARAM = "term";

        private static final String PRICE_PARAM = "price";
        private static final String OPEN_NOW_PARAM = "open_now";

        /* Returns up to 50 restaurants. */
        private static final int MAX_LIMIT = 50;
        private static final String DEFAULT_SEARCH_TERM = "food";

        /* required if lat and long not provided */
        private String location;

        /* required if location is not provided */
        private String latitude;
        private String longitude;

        private int limit = MAX_LIMIT;
        private String term = DEFAULT_SEARCH_TERM;

        private String price;

        private boolean openNow;

        /**
         * Build a Yelp Search URL. Required: location(zip code) OR longitude AND latitude
         */
        public URL build() throws Exception {
            Uri.Builder builder = Uri.parse(YELP_SEARCH_BASE_URL).buildUpon();

            /* Default fields */
            builder.appendQueryParameter(LIMIT_PARAM, Integer.toString(limit));
            builder.appendQueryParameter(TERM_PARAM, term);

            /* Mandatory fields  */
            if (location != null) {
                builder.appendQueryParameter(LOCATION_PARAM, location);
            } else if (latitude != null & longitude != null) {
                builder.appendQueryParameter(LATITUDE_PARAM, latitude);
                builder.appendQueryParameter(LONGITUDE_PARAM, longitude);
            } else {
                throw new Exception("Location(zipCode) or Longitude and Latitude is required!");
            }

            /* Optionals */
            if (price != null) {
                builder.appendQueryParameter(PRICE_PARAM, price);
            }
            builder.appendQueryParameter(OPEN_NOW_PARAM, Boolean.toString(openNow));

            Uri uri = builder.build();
            return new URL(uri.toString());
        }

        public SearchBuilder location(String zipCodeLocation) {
            location = zipCodeLocation;
            return this;
        }

        public SearchBuilder latitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public SearchBuilder longitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public SearchBuilder coordinates(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        /* TODO: handle case where we can include a range of prices: $-$$$$ */
        public SearchBuilder price(String price) {
            this.price = price;
            return this;
        }


        public SearchBuilder openNow(boolean openNow) {
            this.openNow = openNow;
            return this;
        }
    }
}
