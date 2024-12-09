package com.example.taliSocialMedia.signin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taliSocialMedia.Item;
import com.example.taliSocialMedia.MyApplication;
import com.example.taliSocialMedia.network.Post;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.repository.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private PostRepository postRepository;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postRepository = ((MyApplication)application).getPostRepository();
    }

    public LiveData<List<Item>> getPosts(String userEmail) {
        return postRepository.getPosts(userEmail);
    }

    public LiveData<Post> getPost(String postId) {
        return postRepository.getPost(postId);
    }

    public LiveData<List<Item>> getComments(Item item) {
        return postRepository.getComments(item);
    }

    public LiveData<ResponseResult> addPost(Post post) {
        return postRepository.addPost(post);
    }

    public LiveData<ResponseResult> toggleLike(Item item) {
        return postRepository.toggleLike(item);
    }

    public LiveData<ResponseResult> updatePost(Post post) {
        return postRepository.updatePost(post);
    }

    public LiveData<ResponseResult> deletePost(String postId) {
        return postRepository.deletePost(postId);
    }

    public LiveData<ResponseResult> fetchPosts() {
        return postRepository.fetchPosts();
    }

    public void clearAll() {
        postRepository.clear();
    }
}
