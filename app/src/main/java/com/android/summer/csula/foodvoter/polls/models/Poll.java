package com.android.summer.csula.foodvoter.polls.models;


import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Poll implements Serializable {

    private static final String DEFAULT_PRICE = "1";

    // TODO: add a date_created field so it could by sorted by date.
    private User author;
    private String pollId;
    private String title;
    private String description;
    private Coordinate coordinate;
    private String zipCode;
    /**
     * Yelp Price Level 1,2,3,4 ($,$$,$$$,$$$$)
     */
    private String price = DEFAULT_PRICE;
    private boolean openNow;

    /**
     * Determine if the poll is completed(accepting votes) or completed(not accepting votes)
     */
    private boolean completed;


    /**
     * Number of millisecond since UNIX epoch when this Poll is activated, right before sending
     * it to a database;
     */
    private long activatedOn;

    private List<User> voters = new ArrayList<>();

    /**
     * Random selected bossiness for voting.
     */
    private List<Business> businesses = new ArrayList<>();

    /**
     * No argument constructor is for Firebase
     */
    public Poll() {}


    public Poll(User user) {
        this.author = user;
        // The author must participate in their own poll
        voters.add(author);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<User> getVoters() {
        return voters;
    }

    public void addVoters(User voter) {
        if (!voters.contains(voter)) {
            voters.add(voter);
        }
    }

    public void removeVoters(User voter) {
        voters.remove(voter);
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public User getAuthor() {
        return author;
    }

    public long getActivatedOn() {
        return activatedOn;
    }

    public void setActiveOn(long activatedOn) {
        this.activatedOn = activatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        return pollId.equals(poll.pollId);

    }

    @Override
    public int hashCode() {
        return pollId.hashCode();
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "authorId='" + author.getId() + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", coordinate=" + coordinate +
                ", zipCode='" + zipCode + '\'' +
                ", price='" + price + '\'' +
                ", openNow=" + openNow +
                ", completed=" + completed +
                ", voters=" + voters +
                '}';

    }
}
