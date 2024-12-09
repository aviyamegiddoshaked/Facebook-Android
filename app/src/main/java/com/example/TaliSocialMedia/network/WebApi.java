package com.example.taliSocialMedia.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebApi {
    // Register a new user
    @POST("/api/register") // /api/users
    Call<ResponseBody> registerUser(@Body User user);

    // Login
    @POST("/api/login") // "/api/tokens"
    Call<TokenResponse> loginUser(@Body User request);

    @GET("/api/search")
    Call<UsersResponse> searchUsers(@Query("name") String nameQuery, @Header("Authorization") String token);

    @GET("/api/users/{id}")
    Call<User> getCurrentUser(@Path("id") String userId, @Header("Authorization") String token);

    // Get user's feed
    @GET("/api/posts/{id}")
    Call<FeedResponse> getUserFeed(@Path("id") String userId, @Header("Authorization") String token);

//    @GET("/api/users/{id}/posts")
//    Call<List<Post>> getUserFeed(@Path("id") String userId, @Header("Authorization") String token);

    // Create a new post for a user
    @POST("/api/users/{id}/posts")
    Call<PostResponse> createPost(@Path("id") String userId, @Body Post request, @Header("Authorization") String token);

    // Update a post for a user
    @PUT("/api/users/{id}/posts/{pid}")
    Call<Void> updatePost(@Path("id") String userId, @Path("pid") String postId, @Body Post request, @Header("Authorization") String token);

    // Delete a post for a user
    @DELETE("/api/users/{id}/posts/{pid}")
    Call<ResponseBody> deletePost(@Path("id") String userId, @Path("pid") String postId, @Header("Authorization") String token);

    @POST("/api/posts/{id}/like")
    Call<ResponseBody> likePost(@Path("id") String postId, @Body LikeRequest request, @Header("Authorization") String token);

    @POST("/api/posts/{id}/unlike")
    Call<ResponseBody> unlikePost(@Path("id") String postId, @Body UnlikeRequest request, @Header("Authorization") String token);

    @POST("/api/users/{recipient}/friend-request")
    Call<ResponseBody> sendFriendRequest(@Path("recipient") String recipient, @Body FriendRequest request, @Header("Authorization") String token);

    @PATCH("/api/users/{recipient}/approve-friend-request")
    Call<ResponseBody> approveFriendRequest(@Path("recipient") String recipient, @Body FriendRequest request, @Header("Authorization") String token);

    @PATCH("/api/users/{id}/decline-friend-request")
    Call<ResponseBody> declineFriendRequest(@Path("id") String userId, @Body FriendRequest request, @Header("Authorization") String token);

    @DELETE("/api/users/{id}/friends/{friend_id}")
    Call<ResponseBody> unfriend(@Path("id") String userId, @Path("friend_id") String friendId, @Header("Authorization") String token);
}