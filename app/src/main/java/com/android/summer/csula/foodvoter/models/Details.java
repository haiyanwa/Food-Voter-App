package com.android.summer.csula.foodvoter.models;

/**
 * Created by cowboyuniverse on 7/24/17.
 */

/**
 * Created by cowboyuniverse on 7/20/17.
 */

public class Details {
    private String restaurantName;
    private String address;
    private String phoneNumber;
    private String foodType;
    private String price;
    private String description;
    private String imgURL;
    private String url;

    //not sure if to use longitude for maps coordintates
    private String location;
    private float longititude;
    private float latitude;

    public Details(){
    }

    private  String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }





    public Details(String restaurantName, String address, String phoneNumber, String foodType, String price, String description, String location) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.foodType = foodType;
        this.price = price;
        this.description = description;
        this.location = location;
    }

    public Details(String Restaurant, String address, String phoneNumber, String foodType, String price, String description, String location, float longititude, float latitude) {

        this.restaurantName = Restaurant;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.foodType = foodType;
        this.price = price;
        this.description = description;
        this.location = location;
        this.longititude = longititude;
        this.latitude = latitude;
    }

    public Details(String restaurantName, String address, String phoneNumber, String foodType, String price, String description, float longititude, float latitude) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.foodType = foodType;
        this.price = price;
        this.description = description;
        this.longititude = longititude;
        this.latitude = latitude;
    }

    public Details(String restaurantName, String address, String phoneNumber, String foodType, String price, String description) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.foodType = foodType;
        this.price = price;
        this.description = description;
    }

    public Details(String restaurantName, String address, String phoneNumber, String price, String description) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.description = description;
    }

    public Details(String restaurantName, String address, String phoneNumber) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLongititude() {
        return longititude;
    }

    public void setLongititude(float longititude) {
        this.longititude = longititude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}




