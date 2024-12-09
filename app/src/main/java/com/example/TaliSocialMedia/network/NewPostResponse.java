package com.example.taliSocialMedia.network;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.taliSocialMedia.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NewPostResponse {
    @PrimaryKey
    @NonNull
    private String _id;
    @Ignore
    private String user;
    private String message;
    private String image;
    private String date;
    private List<String> likes;

    private String userName;
    private String userImage;
    private String userId;

    public NewPostResponse() { }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String get_id() {
        return _id;
    }

    public String getImage() {
        return image;
    }

    public List<String> getLikes() {
        return likes;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserDetails(String userName, String userImage, String userId) {
        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
    }
}

