package com.example.taliSocialMedia.signin;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taliSocialMedia.network.LoginResponseResult;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<ResponseResult> register(String firstName, String email, String password, Uri imageUri) {
        return userRepository.register(firstName, email, password, imageUri);
    }

    public LiveData<LoginResponseResult> login(String email, String password) {
        return userRepository.loginUser(email, password);
    }

    public LiveData<List<User>> fetchAllUsers() {
        return userRepository.fetchAllUsers();
    }

    public LiveData<User> getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public LiveData<ResponseResult> sendFriendRequest(String targetUserId) {
        return userRepository.sendFriendRequest(targetUserId);
    }

    public LiveData<ResponseResult> approveFriendRequest(String targetUserId) {
        return userRepository.approveFriendRequest(targetUserId);
    }

    public LiveData<ResponseResult> unfriend(String friendId) {
        return userRepository.unfriend(friendId);
    }

    public LiveData<ResponseResult> declineFriendRequest(String targetUserId) {
        return userRepository.declineFriendRequest(targetUserId);
    }
}
