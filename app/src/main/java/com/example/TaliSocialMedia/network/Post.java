package com.example.taliSocialMedia.network;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.taliSocialMedia.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    private String _id;
    @Ignore
    private User user;
    private String message;
    private String image;
    private String date;
    private List<String> likes;

    private String userName;
    private String userImage;
    private String userId;

    public Post() { }

    public Post(@NonNull NewPostResponse newPost) {
        this._id = newPost.get_id();
        this.message = newPost.getMessage();
        this.image = newPost.getImage();
        this.date = newPost.getDate();
        this.likes = newPost.getLikes();
        this.userName = newPost.getUserName();
        this.userImage = newPost.getUserImage();
        this.userId = newPost.getUserId();
    }

    public Post(Item item) {
        this.message = item.getMessage();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void populateDbFields() {
        if (this.user == null) {
            return;
        }
        this.userName = this.user.getName();
        this.userImage = this.user.getImage();
        this.userId = this.user.getId();
    }

    public static List<Item> convertPosts(List<Post> posts, String email, String userId) {
        ArrayList<Item> items = new ArrayList<>();
        for (Post post : posts) {
            items.add(new Item(post, email, userId));
        }
        return items;
    }

    public static List<Post> convertItems(List<Item> items) {
        ArrayList<Post> posts = new ArrayList<>();
        for (Item item : items) {
            posts.add(new Post(item));
        }
        return posts;
    }
}

