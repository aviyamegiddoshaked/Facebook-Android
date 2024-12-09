package com.example.taliSocialMedia.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taliSocialMedia.PostActivity;
import com.example.taliSocialMedia.R;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.repository.UserRepository;
import com.example.taliSocialMedia.signin.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersAdapter userListAdapter;
    private UserViewModel viewModel;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UsersAdapter(new ArrayList<>());
        recyclerView.setAdapter(userListAdapter);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(UsersActivity.this, PostActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        viewModel.fetchAllUsers().observe(UsersActivity.this, new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> users) {
                        if (users != null) {
                            User currentUser = getCurrentUser(users);
                            users.remove(currentUser);
                            userListAdapter.setUsers(users, currentUser);
                        } else {
                            Toast.makeText(UsersActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Set click listener for RecyclerView items
        userListAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isPrimary) {
                User user = userListAdapter.getUsers().get(position);
                User currentUser = userListAdapter.getCurrentUser();
                if (currentUser.getFriends().contains(user.getId())) {
                    unfriendUser(user);
                } else if (currentUser.getFriendRequestsReceived().contains(user.getId())) {
                    if (isPrimary) {
                        approveFriendRequest(user);
                    } else {
                        declineFriendRequest(user);
                    }
                } else if (currentUser.getFriendRequestsSent().contains(user.getId())) {
                    // nothing to do
                } else {
                    sendFriendRequest(user);
                }
            }
        });
    }

    private User getCurrentUser(List<User> allUsers) {
        String userId = UserRepository.getUserId(this);
        for (User user : allUsers) {
            if (userId.equals(user.getId())) {
                return user;
            }
        }
        throw new RuntimeException("User not found");
    }

    private void sendFriendRequest(User user) {
        viewModel.sendFriendRequest(user.getId()).observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(ResponseResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(UsersActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void approveFriendRequest(User user) {
        viewModel.approveFriendRequest(user.getId()).observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(ResponseResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(UsersActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void declineFriendRequest(User user) {
        viewModel.declineFriendRequest(user.getId()).observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(ResponseResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(UsersActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void unfriendUser(User user) {
        viewModel.unfriend(user.getId()).observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(ResponseResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(UsersActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
