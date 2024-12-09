package com.example.taliSocialMedia.network;

import java.util.List;
import java.util.Objects;

public class User {
    private String _id;
    private String name;
    private String email;
    private String password;
    private String image;

    private List<String> friends;
    private List<String> friendRequestsSent;
    private List<String> friendRequestsReceived;

    public User() {
    }

    public User(String name, String email, String password, String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public String getId() {
        return _id;
    }

    // Getters and Setters
    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public List<String> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(_id, user._id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(image, user.image) && Objects.equals(friends, user.friends) && Objects.equals(friendRequestsSent, user.friendRequestsSent) && Objects.equals(friendRequestsReceived, user.friendRequestsReceived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name, email, password, image, friends, friendRequestsSent, friendRequestsReceived);
    }
}

