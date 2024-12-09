package com.example.taliSocialMedia.network;

public class PostResponse {
    private String message;
    private NewPostResponse post;

    public PostResponse() {
    }

    public String getMessage() {
        return message;
    }

    public NewPostResponse getPost() {
        return post;
    }
}
