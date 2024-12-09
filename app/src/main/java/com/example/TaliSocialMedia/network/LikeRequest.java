package com.example.taliSocialMedia.network;

import java.util.List;

public class LikeRequest {
    private List<String> likes;

    public LikeRequest() {
    }

    public LikeRequest(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getLikes() {
        return likes;
    }
}
