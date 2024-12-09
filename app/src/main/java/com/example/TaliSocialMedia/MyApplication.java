package com.example.taliSocialMedia;

import android.app.Application;

import com.example.taliSocialMedia.repository.PostRepository;

public class MyApplication extends Application {
    private PostRepository postRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        postRepository = new PostRepository(this);
    }

    public PostRepository getPostRepository() {
        return postRepository;
    }
}
