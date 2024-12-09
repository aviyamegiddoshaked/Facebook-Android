package com.example.taliSocialMedia;

import android.net.Uri;

import com.example.taliSocialMedia.network.Post;
import com.example.taliSocialMedia.network.User;

import java.util.List;
import java.util.Objects;

public class Item {
    String id;
    String name;
    String email;
    String message;
    String date;
    int image=0;
    Uri uriImage=null,postPhoto=null;
    boolean isitliked,isPostByCurrentUser,isHeadlineInComments;


    /*
    Here we use different constructors for different items.
    Example of different items:
    - Some items use stock photos from the app.
    - Some items use new photos from the camera/gallery of the user.
    - Some items have a picture in them (post items have pictures, comments do not).
    */

    public Item(String name, String message, int image, String date, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.message = message;
        this.date = date;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
    }

    // Constructor for an item with an image resource and a post photo
    public Item(String name, String message, int image, String date, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments,Uri postPhoto) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.message = message;
        this.date = date;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
        this.postPhoto=postPhoto;
    }


    // Constructor for an item with a URI image and a post photo
    public Item(String name, String message, Uri image, String date, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments, Uri postPhoto) {
        this.name = name;
        this.email = email;
        this.uriImage = image;
        this.message = message;
        this.date = date;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
        this.postPhoto=postPhoto;
    }

    public Item(Post post, String email, String currentUserId) {
        post.populateDbFields();
        this.id = post.get_id();
        this.name = post.getUserName();
        this.uriImage = Uri.parse(post.getUserImage());
        this.postPhoto = Helper.parseToUri(post.getImage());
        this.message = post.getMessage();
        this.date = Helper.formatDateString(post.getDate());
        this.isitliked = post.getLikes().contains(email);
        this.isPostByCurrentUser = post.getUserId().equals(currentUserId);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", image=" + image +
                ", uriImage=" + uriImage +
                ", isitliked=" + isitliked +
                ", isPostByCurrentUser=" + isPostByCurrentUser +
                '}';
    }

    // Constructor for an item with an image resource
    public Item(String name, String message, Uri image, String date, boolean isitliked, String email, boolean isPostByCurrentUser,boolean isHeadlineInComments) {
        this.name = name;
        this.email = email;
        this.uriImage = image;
        this.message = message;
        this.date = date;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;

    }


    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIsitliked() {
        return isitliked;
    }

    public void setIsitliked(boolean isitliked) {
        this.isitliked = isitliked;
    }

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPostByCurrentUser() {
        return isPostByCurrentUser;
    }

    public void setPostByCurrentUser(boolean postByCurrentUser) {
        isPostByCurrentUser = postByCurrentUser;
    }

    public boolean isHeadlineInComments() {
        return isHeadlineInComments;
    }

    public void setHeadlineInComments(boolean headlineInComments) {
        isHeadlineInComments = headlineInComments;
    }

    public Uri getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(Uri postPhoto) {
        this.postPhoto = postPhoto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return image == item.image && isitliked == item.isitliked && isPostByCurrentUser == item.isPostByCurrentUser && isHeadlineInComments == item.isHeadlineInComments && Objects.equals(name, item.name) && Objects.equals(email, item.email) && Objects.equals(message, item.message) && Objects.equals(date, item.date) && Objects.equals(uriImage, item.uriImage) && Objects.equals(postPhoto, item.postPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, message, date, image, uriImage, postPhoto, isitliked, isPostByCurrentUser, isHeadlineInComments);
    }
}