package com.android.summer.csula.foodvoter.models;

import java.io.Serializable;

public class User implements Serializable{
    private String username;
    private String id;
    private String token;
    private boolean online;

    public User() {
    }

    public User(String username, String id) {
        this.username = username;
        this.id = id;
    }

    public User(String username, String id, boolean online) {
        this(username, id);
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!User.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final User other = (User) obj;

        return this.id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
