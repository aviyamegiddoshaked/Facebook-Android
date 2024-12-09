package com.example.taliSocialMedia.network;

public class UnlikeRequest {
    private String email;

    public UnlikeRequest(String email) {
        this.email = email;
    }

    public UnlikeRequest() {
    }

    public String getEmail() {
        return email;
    }
}
