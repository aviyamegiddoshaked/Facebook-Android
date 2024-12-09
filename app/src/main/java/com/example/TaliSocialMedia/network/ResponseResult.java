package com.example.taliSocialMedia.network;

public class ResponseResult {
    private boolean success;
    private String errorMessage;

    public ResponseResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
