package com.example.taliSocialMedia.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taliSocialMedia.network.FriendRequest;
import com.example.taliSocialMedia.network.LoginResponseResult;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.network.RetrofitClient;
import com.example.taliSocialMedia.network.TokenResponse;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.network.UsersResponse;
import com.example.taliSocialMedia.network.WebApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final Application application;
    private WebApi apiInterface;
    private Gson gson;
    MutableLiveData<List<User>> allUsersStatus;

    public UserRepository(Application application) {
        this.application = application;
        apiInterface = RetrofitClient.getInstance().create(WebApi.class);
        gson = new Gson();
        allUsersStatus = new MutableLiveData<>();
    }

    public LiveData<ResponseResult> register(String firstName, String email, String password, Uri imageUri) {
        MutableLiveData<ResponseResult> registrationStatus = new MutableLiveData<>();

        User user = new User(firstName, email, password, imageUri.toString());

        // Call the registerUser API endpoint using Retrofit
        Call<ResponseBody> call = apiInterface.registerUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("MY_APP", "Registration successful");
                    registrationStatus.setValue(new ResponseResult(true, null));
                } else {
                    String errorMessage = "Registration failed. Please try again.";
                    try {
                        errorMessage = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("MY_APP", "failed with code " + response.code());
                    registrationStatus.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("MY_APP", "failed with error ", t);
                registrationStatus.setValue(new ResponseResult(false, "Network error"));
            }
        });
        return registrationStatus;
    }

    public LiveData<LoginResponseResult> loginUser(String email, String password) {
        MutableLiveData<LoginResponseResult> loginStatus = new MutableLiveData<>();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Call<TokenResponse> call = apiInterface.loginUser(user);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                boolean successful = response.isSuccessful();
                TokenResponse body = response.body();
                if (successful && body != null) {
                    saveLoggedUser(application, email, body.getId(), body.getToken());
                    loginStatus.setValue(new LoginResponseResult(true, body.getToken(), null));
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    loginStatus.setValue(new LoginResponseResult(false, null, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                loginStatus.setValue(new LoginResponseResult(false, null, "Network error"));
            }
        });

        return loginStatus;
    }

    public MutableLiveData<User> getCurrentUser() {
        MutableLiveData<User> status = new MutableLiveData<>();
        String userId = getUserId(application);
        String token = UserRepository.getToken(application);
        apiInterface.getCurrentUser(userId, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    status.setValue(response.body());
                } else {
                    status.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                status.setValue(null);
            }
        });

        return status;
    }

    public MutableLiveData<List<User>> fetchAllUsers() {
        String token = UserRepository.getToken(application);
        apiInterface.searchUsers("", token).enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    allUsersStatus.setValue(response.body().getUsers());
                } else {
                    allUsersStatus.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                allUsersStatus.setValue(null);
            }
        });

        return allUsersStatus;
    }

    public MutableLiveData<ResponseResult> sendFriendRequest(String targetUserId) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);

        FriendRequest.UserInFriendRequest ur = new FriendRequest.UserInFriendRequest(userId);
        FriendRequest request = new FriendRequest(ur);

        apiInterface.sendFriendRequest(targetUserId, request, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    status.setValue(new ResponseResult(true, null));
                    fetchAllUsers();
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public LiveData<ResponseResult> approveFriendRequest(String targetUserId) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);

        FriendRequest.UserInFriendRequest ur = new FriendRequest.UserInFriendRequest(userId);
        FriendRequest request = new FriendRequest(ur);

        apiInterface.approveFriendRequest(targetUserId, request, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    status.setValue(new ResponseResult(true, null));
                    fetchAllUsers();
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public LiveData<ResponseResult> declineFriendRequest(String targetUserId) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);

        FriendRequest.UserInFriendRequest ur = new FriendRequest.UserInFriendRequest(userId);
        FriendRequest request = new FriendRequest(ur);

        apiInterface.declineFriendRequest(targetUserId, request, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    status.setValue(new ResponseResult(true, null));
                    fetchAllUsers();
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public LiveData<ResponseResult> unfriend(String friendId) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);

        apiInterface.unfriend(userId, friendId, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    status.setValue(new ResponseResult(true, null));
                    fetchAllUsers();
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    private String parseErrorMessage(ResponseBody response) {
        try {
            JsonObject jsonObject = gson.fromJson(response.string(), JsonObject.class);
            return jsonObject.get("error").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error";
        }
    }

    private static void saveLoggedUser(Context context, String email, String id, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_id", id);
        editor.putString("auth_token", token);
        editor.apply();
    }

    public static void clearLoggedUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_email");
        editor.remove("user_id");
        editor.remove("auth_token");
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_email", null);
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null);
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);
    }
}
