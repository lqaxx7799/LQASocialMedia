package com.example.lqasocialmedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("profilePictureUrl")
    @Expose
    private String profilePictureUrl;
    @SerializedName("userFollowings1")
    @Expose
    private List<UserFollowing> userFollowings1;
    @SerializedName("userFollowings2")
    @Expose
    private List<UserFollowing> userFollowings2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<UserFollowing> getUserFollowings1() {
        return userFollowings1;
    }

    public void setUserFollowings1(List<UserFollowing> userFollowings1) {
        this.userFollowings1 = userFollowings1;
    }

    public List<UserFollowing> getUserFollowings2() {
        return userFollowings2;
    }

    public void setUserFollowings2(List<UserFollowing> userFollowings2) {
        this.userFollowings2 = userFollowings2;
    }
}
