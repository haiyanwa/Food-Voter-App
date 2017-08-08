package com.android.summer.csula.foodvoter.yelpApi.models;


import java.io.Serializable;

public class Category implements Serializable {

    /**
     * Alias of a category, when searching for business in certain categories,
     * use alias rather than the title.
     */
    private String alias;

    /**
     * Title of a category for display purpose.
     */
    private String title;


    public Category() {}


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "{alias: " + alias + ", title: " + title + "}";
    }
}
