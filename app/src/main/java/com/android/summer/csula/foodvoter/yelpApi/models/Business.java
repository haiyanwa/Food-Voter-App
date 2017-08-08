package com.android.summer.csula.foodvoter.yelpApi.models;


import java.io.Serializable;
import java.util.List;

public class Business implements Serializable {

    /**
     * A list of category title and alias pairs associated with this business.
     */
    private List<Category> categories;

    /**
     * The coordinate of this business.
     */
    private Coordinate coordinate;

    /**
     * Phone number of the business formatted nicely to be displayed to users.
     * The format is the standard phone number format for the business's country.
     */
    private String displayPhone;

    /**
     * The distance in meters from the search location.
     * This returns meters regardless of the locale
     */
    private double distance;

    /**
     * Yelp id of this business.
     */
    private String id;

    /**
     * URL of photo for this business.
     */
    private String imageUrl;

    /**
     * The location of this business, including address, city, state, zip code and country.
     */
    private Location location;

    /**
     * Name of this business.
     */
    private String name;


    /**
     * YelpPriceLevel level of the business. Value is one of $, $$, $$$ and $$$$.
     */
    private String price;

    /**
     * Rating for this business (value ranges from 1, 1.5, ... 4.5, 5).
     */
    private double rating;

    /**
     * Number of reviews for this business.
     */
    private int reviewCount;

    /**
     * URL for business page on Yelp.
     */
    private String url;

    /**
     * A list of Yelp transactions that the business is registered for.
     * Current supported values are "pickup", "delivery", and "restaurant_reservation".
     * Refer to the Transactions inner class.
     */
    private List<String> transactions;


    public Business() {}


    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    public static final class Transactions {

        public static final String PICK_UP = "pickup";
        public static final String DELIVERY = "delivery";
        public static final String RESTAURANT_RESERVATION = "restaurant_reservation";

    }
}

