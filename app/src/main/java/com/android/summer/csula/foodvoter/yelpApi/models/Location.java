package com.android.summer.csula.foodvoter.yelpApi.models;


import java.io.Serializable;
import java.util.List;

public class Location implements Serializable{

    /**
     * Street address of this business.
     */
    private String address1;

    /**
     * Street address of this business, continued.
     */
    private String address2;

    /**
     * Street address of this business, continued.
     */
    private String address3;

    /**
     * City of this business.
     */
    private String city;

    /**
     * ISO 3166-1 alpha-2 country code of this business.
     */
    private String country;

    /**
     * ISO 3166-2 (with a few exceptions) state code of this business.
     */
    private String state;

    /**
     * Zip code of this business.
     */
    private String zipCode;

    /**
     * Array of strings that if organized vertically give an address that is in the
     * standard address format for the business's country.
     */
    private List<String> displayAddress;


    public Location() {}


    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<String> getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(List<String> displayAddress) {
        this.displayAddress = displayAddress;
    }
}

