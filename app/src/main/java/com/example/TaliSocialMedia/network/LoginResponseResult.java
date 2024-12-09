package com.example.taliSocialMedia.network;

public class LoginResponseResult extends ResponseResult {
    private String token;

    public LoginResponseResult(boolean success, String token, String errorMessage) {
        super(success, errorMessage);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
