package com.example.taliSocialMedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.taliSocialMedia.friends.UsersActivity;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.repository.UserRepository;
import com.example.taliSocialMedia.signin.PostViewModel;
import com.example.taliSocialMedia.signin.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewInterface {

    // Global variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView searchView;

    List<Item> itemsForComments = new ArrayList<>();
    List<Item> itemsClickedComment = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView,recyclerViewForComments;
    MyAdapter myAdapter;
    MyAdapterForComments myAdapterForComments;

    RadioButton nightModeBtn;
    Item testCaseAddComment=null;

    String userEmail;

    boolean viewingComments=false;

    private PostViewModel postViewModel;
    private UserViewModel userViewModel;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        ImageView userImage;
        Button backBTN,addPostBTN;

        // View attachments
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.username_TV);
        backBTN = findViewById(R.id.signOutBTN);
        addPostBTN = findViewById(R.id.addPostBTN);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewForComments = findViewById(R.id.recyclerViewForComments);
        drawerLayout= findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        searchView=findViewById(R.id.searchView);
        searchView.clearFocus();
        nightModeBtn = findViewById(R.id.nightModeBTN);

        userViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User currentUser) {
                if (currentUser == null) {
                    Toast.makeText(PostActivity.this, "Failed to get user details", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri userImageUri = Uri.parse(currentUser.getImage());
                String scheme = userImageUri.getScheme();
                if (scheme != null && scheme.equals("data")) {
                    Bitmap bitmap = Helper.decodeImageFromBase64(userImageUri);
                    userImage.setImageBitmap(bitmap);
                } else {
                    userImage.setImageURI(userImageUri);
                }

                userName.setText(currentUser.getName());
                ((MyApplication)getApplication()).getPostRepository().setCurrentUser(currentUser);
            }
        });

        // Setup two adapters, one for posts and one for comments
        myAdapter = new MyAdapter(getApplicationContext(),new ArrayList<>(),this);
        myAdapterForComments = new MyAdapterForComments(getApplicationContext(),itemsForComments,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Implement the refresh action here, such as fetching new data
                postViewModel.fetchPosts().observe(PostActivity.this, new Observer<ResponseResult>() {
                    @Override
                    public void onChanged(ResponseResult result) {
                        if (!result.isSuccess()) {
                            Toast.makeText(PostActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
                        }
                    }
                });
            }
        });


        // Simple search logic (when text change, filter list by input-text and refresh list)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        // Night mode button MODE_NIGHT_YES / MODE_NIGHT_NO
        nightModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
                    nightModeBtn.setChecked(true);
                    nightModeBtn.setSelected(true);

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    nightModeBtn.setChecked(false);
                    nightModeBtn.setSelected(false);

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        // Hamburger menu
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        userEmail = UserRepository.getEmail(this);

        // Get info from the activity that launched us
        // Four Scenarios
        // 1) We got called from MainActivity
        // 2) We got called from AddPostActivity
        // 3) We got called from AddCommentActivity
        // 4) We got called from EditMessageActivity (edit post/comment activity)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get name, email, image from the activity that launched us
            // indicator = to know where we came from and act accordingly (after post? after comment? after edit?)
            Uri imageUri = getIntent().getData();
            String indicator = extras.getString("indicator");

            // Here we take care of the different scenarios.
            // For example, if we came after creating new post, Then what we want to do is add the new "post" to the items array and notify the adapter so we will see the new post on our screen
            // Or , if we came after 'editing comment' we change the comment.
            switch (indicator) {
                case "afterComment":
                case "afterEditComment": {
                    testCaseAddComment = new Item(extras.getString("firstname"), extras.getString("message"), imageUri, extras.getString("date"), false, userEmail, true, false);
                    break;
                }
            }
        }

        postViewModel.getPosts(userEmail).observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                myAdapter.setItems(items);
                swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
            }
        });

        // A back btn that simply calls "onBackPressed" when clicked
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // When we want to add post, we call the AddPostActivity.
        // That activity shows the userImage + his name (and uses his email behind the scenes)
        // So we put the data into the intent for AddPostActivity to use
        addPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras != null) {

                    Intent myIntent = new Intent(PostActivity.this, AddPostActivity.class);
                    // Get Uri from Intent
                    Uri userImage=getIntent().getData();
                    myIntent.setData(userImage);
                    myIntent.putExtra("firstname", extras.getString("firstname"));
                    myIntent.putExtra("email", extras.getString("email"));
                    startActivity(myIntent);

                }
            }
        });

    }

    // Used for the search bar, whenever we type something into the search bar, this function will run
    // This function gets the current string that's in the search bar
    // And compares it to the names of all the post-creators (for Item item:items)
    // If the name contains the string in the search bar, it will be added to the filteredList
    // Finally we feed the adapter the filteredList, and we only see posts that match our search.
    private void filterList(String text) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : myAdapter.items){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }


            myAdapter.setFilteredList(filteredList);

    }

    // When the back button is pressed , we check if the hamburger menu is open , if it is , we close it
    // If its not, only then we check if were viewing comments of a post , if we are, change the appearance to normal (as if were not viewing comments anymore)
    // And finally if the hamburger menu is not open AND were not viewing comments, we go back to the main menu.
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(viewingComments)
        {
            recyclerView.setAdapter(myAdapter);
            recyclerViewForComments.setVisibility(View.GONE);
            viewingComments = false;
            myAdapter.notifyDataSetChanged();
        }
        else {
            Intent myIntent = new Intent(PostActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
        }
    }

    // Not implemented yet - hamburger menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            UserRepository.clearLoggedUser(this);
            postViewModel.clearAll();
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            finish();
        } else if (item.getItemId() == R.id.nav_users_list) {
            Intent myIntent = new Intent(this, UsersActivity.class);
            startActivity(myIntent);
            finish();
        }
        return true;
    }


    @Override
    public void onItemClick(int position) {/*
    OnItemClick - whenever we click on an item, if were not viewing comments, that means we want to
    load the comments of the post we just clicked.

    The screen will now show 2 recyclerViews (instead of 1 for all the posts)
    # one for the post we clicked (with the picture and all that)
    # and one for the comments (below the post, those without pictures)

    itemsClickedComment - is the itemArray for the post we clicked (an array with 1 item only! - the clicked post..)
    itemsForComments - is the itemArray for all the comments

    We make the "recyclerViewForComments" visible (because we want to see comments now, we set it to not be visible in the XML file
    set the recyclerview layout to linearlayout and set its adapter to "myAdapterForComments"
    the adapter was already set (not here) to use the "recyclerViewForComments" array.

    we then fill the "recyclerViewForComments" array with elements we load from the JSON Array called "Comments" (in the JSON file)
    and then we're done.
     */
        if(!viewingComments)
        {
            itemsClickedComment.clear();

            Item item = myAdapter.items.get(position);
            if(item.getUriImage() == null)
                itemsClickedComment.add(new Item(item.getName(), item.getMessage(), item.getImage(), item.getDate(), item.isitliked, item.getEmail(), item.isPostByCurrentUser,true, item.getPostPhoto()));
            else
                itemsClickedComment.add(new Item(item.getName(), item.getMessage(), item.getUriImage(), item.getDate(), item.isitliked, item.getEmail(), item.isPostByCurrentUser,true, item.getPostPhoto()));


            MyAdapter mytempAdapter = new MyAdapter(getApplicationContext(), itemsClickedComment, this);
            recyclerView.setAdapter(mytempAdapter);
            mytempAdapter.notifyDataSetChanged();
            recyclerViewForComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewForComments.setAdapter(myAdapterForComments);
            recyclerViewForComments.setVisibility(View.VISIBLE);
            viewingComments = true;
            itemsForComments.clear();

            if(testCaseAddComment!=null){
                itemsForComments.add(testCaseAddComment);
            }

            postViewModel.getComments(item).observe(this, new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    itemsForComments.addAll(items);
                    mytempAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // When we click like, check if the post is already liked. if it is liked -> do unlike , if its not liked -> do like.
    @Override
    public boolean onLikeClick(int position) {
        Item item = myAdapter.items.get(position);
        postViewModel.toggleLike(item).observe(this, new Observer<ResponseResult>() {
            @Override
            public void onChanged(ResponseResult result) {
                if (result.isSuccess()) {
                    item.setIsitliked(!item.isIsitliked());
                } else {
                    Toast.makeText(PostActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return !item.isIsitliked();
    }


    // We use this method to know if the post is made by the current user
    // If yes, we show the "Edit" and "Delete" button
    @Override
    public boolean checkIfPostIsMadeByCurrentUser(int position) {
        if(viewingComments) {
            if (position == 0){
                // Don't show delete icon if viewing comments of your own post, you can only delete post from outside.
                if (userEmail.equals(itemsClickedComment.get(0).getEmail())) {
                return false;
                }
            }
            else{
                if (userEmail.equals(itemsForComments.get(position-1).getEmail())) {
                    return true;
                }
            }
        }
        else{
            return myAdapter.items.get(position).isPostByCurrentUser();
        }
        return false;
    }



    // Delete post OR comment, and because we have fake comment (can't write to JSON) we deal with that also.
    @Override
    public void onDeleteClick(int position) {
        if(viewingComments){
            if (position==0)
                return;
            else{
                if(testCaseAddComment!= null && itemsForComments.get(position-1).getEmail()== testCaseAddComment.getEmail())
                    testCaseAddComment=null;
                itemsForComments.remove(position-1);
                myAdapterForComments.notifyItemRemoved(position-1);
            }
        }
        else {
            postViewModel.deletePost(myAdapter.items.get(position).getId()).observe(this, new Observer<ResponseResult>() {
                @Override
                public void onChanged(ResponseResult result) {
                    if (result.isSuccess()) {
                        // do nothing
                    } else {
                        Toast.makeText(PostActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // When we click the "add comment" button, very similar to "addPostBTN.setOnClickListener"
    @Override
    public void onAddCommentClick(int position) {
        Bundle extras = getIntent().getExtras();
        Intent myIntent = new Intent(PostActivity.this, AddCommentActivity.class);
        // Get Uri from Intent
        Uri userImage=getIntent().getData();
        myIntent.setData(userImage);
        myIntent.putExtra("firstname", extras.getString("firstname"));
        myIntent.putExtra("email", extras.getString("email"));
        startActivity(myIntent);
    }

    // When we click the "edit" button (the pencil) , also very similar to "addPostBTN.setOnClickListener"
    @Override
    public void onEditPostClick(int position) {
        if(viewingComments)
        {
            Bundle extras = getIntent().getExtras();
            Intent myIntent = new Intent(PostActivity.this, EditMessageActivity.class);
            // Get Uri from Intent
            Uri userImage = getIntent().getData();
            myIntent.setData(userImage);
            myIntent.putExtra("firstname", extras.getString("firstname"));
            myIntent.putExtra("email", extras.getString("email"));
            myIntent.putExtra("message", itemsForComments.get(position-1).getMessage());
            myIntent.putExtra("position", position-1);
            myIntent.putExtra("title","Edit comment");
            myIntent.putExtra("indicate","comment");
            startActivity(myIntent);
        }
        else {
            Bundle extras = getIntent().getExtras();
            Intent myIntent = new Intent(PostActivity.this, EditMessageActivity.class);
            // Get Uri from Intent
            Uri userImage = getIntent().getData();
            myIntent.setData(userImage);
            myIntent.putExtra("firstname", userName.getText().toString());
            myIntent.putExtra("title","Edit post");
            myIntent.putExtra("indicate","post");
            myIntent.putExtra("post_id", myAdapter.items.get(position).getId());
            startActivity(myIntent);
        }
    }

    // When click share - open sharing menu , after user chooses where to share, we also send a message
    // If for example user will decide to share via Whatsapp, it will automatically write "Share from tali social media app"
    // To the whatsapp user he chose to share with. this can later be changed to something more meaningful like the message in the post that the user clicked share on.
    @Override
    public void onShareClick(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Share from tali social media app");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}