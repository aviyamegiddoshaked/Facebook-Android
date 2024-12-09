package com.example.taliSocialMedia.repository;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taliSocialMedia.Item;
import com.example.taliSocialMedia.R;
import com.example.taliSocialMedia.db.MyDatabase;
import com.example.taliSocialMedia.db.PostDao;
import com.example.taliSocialMedia.network.FeedResponse;
import com.example.taliSocialMedia.network.LikeRequest;
import com.example.taliSocialMedia.network.NewPostResponse;
import com.example.taliSocialMedia.network.Post;
import com.example.taliSocialMedia.network.PostResponse;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.network.RetrofitClient;
import com.example.taliSocialMedia.network.UnlikeRequest;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.network.WebApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private Application application;
    private WebApi webApi;
    private JSONObject jObject;

    private PostDao postDao;
    private MutableLiveData<List<Item>> allPosts;
    private Gson gson = new Gson();
    private User currentUser;

    private final Executor executor = Executors.newSingleThreadExecutor();;

    public PostRepository(Application application) {
        this.application = application;
        webApi = RetrofitClient.getInstance().create(WebApi.class);

        MyDatabase db = MyDatabase.getInstance(application);
        postDao = db.postDao();
        allPosts = new MutableLiveData<>();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    //    public LiveData<List<Item>> getPosts(String userEmail) {
//        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (jObject == null) {
//                    jObject = readJson();
//                }
//                List<Item> items = readUsers(userEmail);
//                liveData.postValue(items);
//            }
//        }).start();
//
//        return liveData;
//    }

//    public LiveData<List<Item>> getComments(Item item) {
//        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (jObject == null) {
//                    jObject = readJson();
//                }
//                List<Item> items = readComments(item);
//                liveData.postValue(items);
//            }
//        }).start();
//
//        return liveData;
//    }

    private JSONObject readJson() {
        Writer writer;
        JSONObject jObject;
        JSONArray jArray;
        List<Item> items = new ArrayList<>();

        // For JSON reading
        InputStream is = application.getResources().openRawResource(R.raw.users);
        writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Here we traverse through the JSON array (The array is called "Users" in the JSON file)
        // - getting the relevant information from each object in the JSON array
        // We put all the info together to create an "Item" object, and add it to the "items" array.
        // When we finish, the 'items' array should be full with items that represent different posts.
        String jsonString = writer.toString();

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Item> readUsers(String userEmail) {
        ArrayList<Item> items = new ArrayList<>();
        try {
            JSONArray jArray = jObject.getJSONArray("Users");

            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject Object = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String name = Object.getString("name");
                    String message = Object.getString("message");
                    String date = Object.getString("date");
                    String email = Object.getString("email");
                    Uri image = Uri.parse(Object.getString("image"));


                    if(i%4==0) {
                        items.add(new Item(name, message, R.drawable.firstpic, date,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==1) {
                        items.add(new Item(name, message, R.drawable.secondpic, date,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==2) {
                        items.add(new Item(name, message, R.drawable.thirdpic, date,false,email,userEmail.equals(email),false));
                    }
                    else if(i%4==3) {
                        items.add(new Item(name, message, R.drawable.fourthpic, date,false,email,userEmail.equals(email),false));
                    }
                } catch (JSONException e) {
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    private List<Item> readComments(Item item) {
        // Example of a JSON object in the "comments" array
//            "commentFor": "danielcohen@gmail.com",
//                "name" : "Nehorai Rani",
//                "message": "This is my message",
//                "image": "",
//                "date": "31/01/2024"
        ArrayList<Item> itemsForComments = new ArrayList<>();
        try {
            JSONArray jArray = jObject.getJSONArray("Comments");
            for (int i=0; i < jArray.length(); i++) {
                JSONObject Object = jArray.getJSONObject(i);

                // Pulling items from the array
                String commentFor = Object.getString("commentFor");
                String name = Object.getString("name");
                String message = Object.getString("message");
                String date = Object.getString("date");
                String email = Object.getString("email");
                Uri image = Uri.parse(Object.getString("image"));

                // Only if the comment belongs to the clicked user , load the comment
                if (item.getEmail().equals(commentFor)) {
                    if (i % 4 == 0) {
                        itemsForComments.add(new Item(name, message, R.drawable.firstpic, date, false, email,item.getEmail().equals(email),false));
                    } else if (i % 4 == 1) {
                        itemsForComments.add(new Item(name, message, R.drawable.secondpic, date, false, email,item.getEmail().equals(email),false));
                    } else if (i % 4 == 2) {
                        itemsForComments.add(new Item(name, message, R.drawable.thirdpic, date, false, email,item.getEmail().equals(email),false));
                    } else if (i % 4 == 3) {
                        itemsForComments.add(new Item(name, message, R.drawable.fourthpic, date, false, email,item.getEmail().equals(email),false));
                    }
                }
            }
        } catch (JSONException e) {
        }
        return itemsForComments;
    }




    public LiveData<List<Item>> getPosts(String ignored) {
        String email = UserRepository.getEmail(application);
        String userId = UserRepository.getUserId(application);
        executor.execute(() -> {
            List<Item> items = Post.convertPosts(postDao.getAllPosts(), email, userId);
            if (items.isEmpty()) {
                fetchPosts();
            } else {
                allPosts.postValue(items);
            }
        });
        return allPosts;
    }

    public MutableLiveData<ResponseResult> fetchPosts() {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String token = UserRepository.getToken(application);
        String email = UserRepository.getEmail(application);
        String userId = UserRepository.getUserId(application);
        webApi.getUserFeed(userId, token).enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body().getPosts();
                    if (posts != null) {
                        executor.execute(() -> {
                            for (Post post : posts) {
                                post.populateDbFields();
                                postDao.insert(post);
                            }
                        });
                        allPosts.postValue(Post.convertPosts(posts, email, userId));
                    }
                } else {
                    status.postValue(new ResponseResult(false, response.message()));
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public MutableLiveData<ResponseResult> addPost(Post post) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);
        webApi.createPost(userId, post, token).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    // Add the post to the local database upon successful addition on the server
                    NewPostResponse responsePost = response.body().getPost();
                    executor.execute(() -> {
                        String userImage = null;
                        String userName = null;
                        if (currentUser != null) {
                            userImage = currentUser.getImage();
                            userName = currentUser.getName();
                        }
                        responsePost.setUserDetails(userName, userImage, userId);
                        Post postToSave = new Post(responsePost);
                        postDao.insert(postToSave);
                    });
                    status.setValue(new ResponseResult(true, null));
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public MutableLiveData<ResponseResult> updatePost(Post post) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);

        webApi.updatePost(userId, post.get_id(), post, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Update the post in the local database upon successful update on the server
                    executor.execute(() -> {
                        postDao.update(post);
                    });
                    status.setValue(new ResponseResult(true, null));
                } else {
                    String errorMessage = parseErrorMessage(response.errorBody());
                    status.setValue(new ResponseResult(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                status.setValue(new ResponseResult(false, "Network error"));
            }
        });

        return status;
    }

    public MutableLiveData<ResponseResult> deletePost(String postId) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        String userId = UserRepository.getUserId(application);
        String token = UserRepository.getToken(application);
        webApi.deletePost(userId, postId, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Delete the post from the local database upon successful deletion on the server
                    executor.execute(() -> {
                        postDao.deletePostById(postId);
                        String email = UserRepository.getEmail(application);
                        allPosts.postValue(Post.convertPosts(postDao.getAllPosts(), email, userId));
                    });
                    status.setValue(new ResponseResult(true, null));
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

    public MutableLiveData<ResponseResult> toggleLike(Item item) {
        MutableLiveData<ResponseResult> status = new MutableLiveData<>();

        executor.execute(() -> {
            Post post = postDao.getPostById(item.getId());
            if (post == null) {
                status.postValue(new ResponseResult(false, "Post doesn't exist"));
                return;
            }

            String email = UserRepository.getEmail(application);
            String token = UserRepository.getToken(application);
            Call<ResponseBody> call;
            if (post.getLikes().contains(email)) {
                UnlikeRequest unlikeRequest = new UnlikeRequest(email);
                post.getLikes().remove(email);
                call = webApi.unlikePost(post.get_id(), unlikeRequest, token);
            } else {
                post.getLikes().add(email);
                LikeRequest likeRequest = new LikeRequest(post.getLikes());
                call = webApi.likePost(post.get_id(), likeRequest, token);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                                    postDao.update(post);
                        });
                        status.postValue(new ResponseResult(true, null));
                    } else {
                        String errorMessage = parseErrorMessage(response.errorBody());
                        status.postValue(new ResponseResult(false, errorMessage));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    status.postValue(new ResponseResult(false, "Network error"));
                }
            });
        });

        return status;
    }

    public LiveData<List<Item>> getComments(Item item) {
        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            liveData.postValue(new ArrayList<>());
        });
        return liveData;
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

    public LiveData<Post> getPost(String postId) {
        MutableLiveData<Post> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            Post post = postDao.getPostById(postId);
            liveData.postValue(post);
        });
        return liveData;
    }

    public void clear() {
        executor.execute(() -> {
            postDao.clear();
        });
    }
}
