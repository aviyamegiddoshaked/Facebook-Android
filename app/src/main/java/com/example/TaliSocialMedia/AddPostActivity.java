package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taliSocialMedia.network.Post;
import com.example.taliSocialMedia.network.ResponseResult;
import com.example.taliSocialMedia.signin.PostViewModel;

public class AddPostActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1234;

    // Uri for the selected photo to be attached to the post
    Uri photoForPost = null;

    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Declare UI elements
        Button backButton, submitPostBTN, addImageBtn;
        EditText userMessage;
        ImageView userImage;
        TextView userFirstName;

        // Initialize UI elements
        backButton = findViewById(R.id.goBackBTN);
        userMessage = findViewById(R.id.usermessageET);
        userMessage.setText("");
        userImage = findViewById(R.id.userImage);
        userFirstName = findViewById(R.id.username_TV);
        submitPostBTN = findViewById(R.id.finishBTN);
        addImageBtn = findViewById(R.id.postAddPicture);

        // Retrieve extras passed from previous activity
        Bundle extras = getIntent().getExtras();

        // Set user image and first name
        userImage.setImageURI(getIntent().getData());
        userFirstName.setText(extras.getString("firstname"));

        // Button click listener for submitting a post
        submitPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if a photo is selected
                if (photoForPost == null) {
                    Toast.makeText(AddPostActivity.this, "Must choose image", Toast.LENGTH_SHORT).show();
                } else {
                    Post post = new Post();
                    post.setMessage(userMessage.getText().toString());
                    post.setImage(photoForPost.toString());

                    viewModel.addPost(post).observe(AddPostActivity.this, new Observer<ResponseResult>() {
                        @Override
                        public void onChanged(ResponseResult result) {
                            if (result.isSuccess()) {
                                Intent myIntent = new Intent(AddPostActivity.this, PostActivity.class);
                                startActivity(myIntent);
                                finish();
                            } else {
                                Toast.makeText(AddPostActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // Button click listener for adding an image to the post
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery to select an image
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), GALLERY_REQUEST);
            }
        });

        // Button click listener for going back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Go back to previous screen
            }
        });
    }

    // Handle result after selecting an image from the gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is from selecting an image from the gallery
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            // Get the URI of the selected image
            Bitmap bitmap = Helper.decodeUri(getContentResolver(), data.getData());
            if (bitmap != null) {
                photoForPost = Helper.encodeImageIntoBase64(bitmap);
            } else {
                Toast.makeText(AddPostActivity.this, "Failed to read the image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
