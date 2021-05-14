package com.example.lqasocialmedia.viewmodel;

public class Post {
    private int userId;
    private int postId;
    private String imgProfilePicture;
    private String userName;
    private String imgThumbnail;
    private boolean isLiked;
    private String caption;

    public Post(int userId, int postId, String imgProfilePicture, String userName, String imgThumbnail, boolean isLiked, String caption) {
        this.userId = userId;
        this.postId = postId;
        this.imgProfilePicture = imgProfilePicture;
        this.userName = userName;
        this.imgThumbnail = imgThumbnail;
        this.isLiked = isLiked;
        this.caption = caption;
    }

    public Post() {
    }

    public String getImgProfilePicture() {
        return imgProfilePicture;
    }

    public void setImgProfilePicture(String imgProfilePicture) {
        this.imgProfilePicture = imgProfilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgThumbnail() {
        return imgThumbnail;
    }

    public void setImgThumbnail(String imgThumbnail) {
        this.imgThumbnail = imgThumbnail;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
