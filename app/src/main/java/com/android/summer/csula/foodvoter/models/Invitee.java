package com.android.summer.csula.foodvoter.models;


/**
 * This class is use to wrap the user class, it add "invited" field to determien
 * if the users have been invited to a poll
 */
public class Invitee {

    private User user;
    private boolean invited;

    public Invitee(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    @Override
    public boolean equals(Object obj) {
        return this.user.equals((User) obj);
    }

    public String getId() {
        return user.getId();
    }
}
