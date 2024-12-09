package com.example.taliSocialMedia.network;

public class FriendRequest {
    private UserInFriendRequest user;

    public FriendRequest() {
    }

    public FriendRequest(UserInFriendRequest user) {
        this.user = user;
    }

    public UserInFriendRequest getUser() {
        return user;
    }

    public static class UserInFriendRequest {
        private String _id;

        public UserInFriendRequest() {
        }

        public UserInFriendRequest(String _id) {
            this._id = _id;
        }

        public String get_id() {
            return _id;
        }
    }
}
