package com.android.summer.csula.foodvoter.yelpApi.utils;


import android.util.Log;

import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Category;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.android.summer.csula.foodvoter.yelpApi.models.Location;
import com.android.summer.csula.foodvoter.yelpApi.models.Yelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handle the tedious conversion from yelp json response into an Java Object
 */
public class YelpJsonUtil {

    /* Yelp JSON fields */
    private static final String TOTAL = "total";
    private static final String BUSINESSES_ARRAY = "businesses";

    /* Business JSON fields */
    private static final String DISPLAY_PHONE = "display_phone";
    private static final String DISTANCE = "distance";
    private static final String ID = "id";
    private static final String IMAGE_URL = "image_url";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String REVIEW_COUNT = "review_count";
    private static final String URL = "url";
    private static final String TRANSACTIONS_ARRAY = "transactions";
    private static final String CATEGORIES_ARRAY = "categories";
    private static final String LOCATION = "location";
    private static final String COORDINATES = "coordinates";

    /* Categories JSON fields */
    private static final String ALIAS = "alias";
    private static final String TITLE = "title";

    /* Location JSON fields */
    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String ADDRESS3 = "address3";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String ZIP_CODE = "zip_code";
    private static final String STATE = "state";
    private static final String DISPLAY_ADDRESS = "display_address";

    /* Coordinates JSON fields */
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    /**
     * Convert Yelp Api JSON String into a Java Object
     */
    public static Yelp parse(String yelpJsonResponse) {

        try {
            JSONObject yelpJson = new JSONObject(yelpJsonResponse);
            JSONArray yelpBusinessesJsonArray = yelpJson.getJSONArray(BUSINESSES_ARRAY);
            int total = yelpJson.getInt(TOTAL);
            List<Business> businesses = toBusinesses(yelpBusinessesJsonArray);
            return new Yelp(total, businesses);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Business> toBusinesses(JSONArray yelpBusinessJsonArray) {
        List<Business> businesses = new ArrayList<>();

        for (int i = 0; i < yelpBusinessJsonArray.length(); i++) {
            try {

                Business business = toBusiness(yelpBusinessJsonArray.getJSONObject(i));
                businesses.add(business);
            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    Log.d("xxx", yelpBusinessJsonArray.getJSONObject(i).getString(ID));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return businesses;
    }

    private static Business toBusiness(JSONObject yelpBusinessJson) throws JSONException {

        String displayPhone = yelpBusinessJson.getString(DISPLAY_PHONE);
        double distance = yelpBusinessJson.getDouble(DISTANCE);
        String id = yelpBusinessJson.getString(ID);
        String imageUrl = yelpBusinessJson.getString(IMAGE_URL);
        String name = yelpBusinessJson.getString(NAME);
        String price = yelpBusinessJson.getString(PRICE);
        double rating = yelpBusinessJson.getDouble(RATING);
        int reviewCount = yelpBusinessJson.getInt(REVIEW_COUNT);
        String url = yelpBusinessJson.getString(URL);

        JSONArray transactionsJsonArray = yelpBusinessJson.getJSONArray(TRANSACTIONS_ARRAY);
        List<String> transactions = toStringList(transactionsJsonArray);

        JSONArray categoriesJsonArray = yelpBusinessJson.getJSONArray(CATEGORIES_ARRAY);
        List<Category> categories = toCategoryList(categoriesJsonArray);

        JSONObject locationJsonObject = yelpBusinessJson.getJSONObject(LOCATION);
        Location location = toLocation(locationJsonObject);

        JSONObject coordinatesJsonObject = yelpBusinessJson.getJSONObject(COORDINATES);
        Coordinate coordinate = toCoordinate(coordinatesJsonObject);

        Business business = new Business();
        business.setDisplayPhone(displayPhone);
        business.setDistance(distance);
        business.setId(id);
        business.setImageUrl(imageUrl);
        business.setName(name);
        business.setPrice(price);
        business.setRating(rating);
        business.setReviewCount(reviewCount);
        business.setUrl(url);
        business.setTransactions(transactions);
        business.setCategories(categories);
        business.setLocation(location);
        business.setCoordinate(coordinate);

        return business;
    }

    private static Coordinate toCoordinate(JSONObject coordinatesJsonObject) throws JSONException {
        Coordinate coordinate = new Coordinate();

        double latitude = coordinatesJsonObject.getDouble(LATITUDE);
        double longitude = coordinatesJsonObject.getDouble(LONGITUDE);

        coordinate.setLatitude(latitude);
        coordinate.setLongitude(longitude);

        return coordinate;
    }

    private static List<Category> toCategoryList(JSONArray categoriesJsonArray) {
        List<Category> categories = new ArrayList<>();

        for (int i = 0; i < categoriesJsonArray.length(); i++) {
            try {
                Category category = toCategory(categoriesJsonArray.getJSONObject(i));
                categories.add(category);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return categories;
    }

    private static Category toCategory(JSONObject jsonObject) throws JSONException {
        String alias = jsonObject.getString(ALIAS);
        String title = jsonObject.getString(TITLE);

        Category category = new Category();
        category.setAlias(alias);
        category.setTitle(title);

        return category;
    }


    private static List<String> toStringList(JSONArray yelpBusinessTransactionsArray) {
        List<String> stringList = new ArrayList<>();

        if (yelpBusinessTransactionsArray.length() == 0) {
            return stringList;
        } else {
            for (int i = 0; i < yelpBusinessTransactionsArray.length(); i++) {

                try {
                    stringList.add(yelpBusinessTransactionsArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return stringList;
        }
    }


    private static Location toLocation(JSONObject yelpBusinessJson) throws JSONException {
        Location location = new Location();

        String address1 = yelpBusinessJson.getString(ADDRESS1);
        String address2 = yelpBusinessJson.getString(ADDRESS2);
        String address3 = yelpBusinessJson.getString(ADDRESS3);
        String city = yelpBusinessJson.getString(CITY);
        String country = yelpBusinessJson.getString(COUNTRY);
        String state = yelpBusinessJson.getString(STATE);
        String zipCode = yelpBusinessJson.getString(ZIP_CODE);

        JSONArray displayAddressJsonArray = yelpBusinessJson.getJSONArray(DISPLAY_ADDRESS);
        List<String> displayAddress = toStringList(displayAddressJsonArray);

        location.setAddress1(address1);
        location.setAddress2(address2);
        location.setAddress3(address3);
        location.setCity(city);
        location.setCountry(country);
        location.setState(state);
        location.setZipCode(zipCode);
        location.setDisplayAddress(displayAddress);

        return location;
    }
}
