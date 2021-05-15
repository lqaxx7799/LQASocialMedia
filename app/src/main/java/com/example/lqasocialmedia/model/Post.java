package com.example.lqasocialmedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("id")
    private int id;
    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;
    @SerializedName("caption")
    private String caption;
    @SerializedName("isDeleted")
    private boolean isDeleted;
    @SerializedName("account")
    private Account account;
    @SerializedName("createdTime")
    private Date createdTime;

    public Post() {
    }

    public Post(int id, String thumbnailUrl, String caption, boolean isDeleted, Account account, Date createdTime) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.caption = caption;
        this.isDeleted = isDeleted;
        this.account = account;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccountId(Account account) {
        this.account = account;
    }

    public Date getCreatedAt() {
        return createdTime;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdTime = createdAt;
    }
}
