package com.example.lqasocialmedia.model;

import java.util.Date;

public class UserFollowing {
    private UserFollowingPK id;
    private Date createdTime;

    public UserFollowingPK getId() {
        return id;
    }

    public void setId(UserFollowingPK id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
