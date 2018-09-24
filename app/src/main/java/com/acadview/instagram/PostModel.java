package com.acadview.instagram;

public class PostModel {
    String username,userImgURL,userEmail,postImgURL,postTime;

    public PostModel(String username, String userImgURL, String userEmail, String postImgURL, String postTime) {
        this.username = username;
        this.userImgURL = userImgURL;
        this.userEmail = userEmail;
        this.postImgURL = postImgURL;
        this.postTime = postTime;
    }

    public PostModel(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImgURL() {
        return userImgURL;
    }

    public void setUserImgURL(String userImgURL) {
        this.userImgURL = userImgURL;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPostImgURL() {
        return postImgURL;
    }

    public void setPostImgURL(String postImgURL) {
        this.postImgURL = postImgURL;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
