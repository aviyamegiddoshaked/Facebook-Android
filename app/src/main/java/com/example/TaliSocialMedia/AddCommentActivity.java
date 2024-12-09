package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
       We come here from PostActivity to create a new comment.
*/
public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        // Declare UI elements
        Button backButton, addCommentBTN;
        EditText userMessage;
        ImageView userImage;
        TextView userFirstName;

        // Initialize UI elements
        backButton = findViewById(R.id.goBackBTN);
        userMessage = findViewById(R.id.usermessageET);
        userMessage.setText("");
        userImage = findViewById(R.id.userImage);
        userFirstName = findViewById(R.id.username_TV);
        addCommentBTN = findViewById(R.id.finishBTN);

        // Retrieve extras passed from previous activity
        Bundle extras = getIntent().getExtras();

        // Set user image and first name
        userImage.setImageURI(getIntent().getData());
        userFirstName.setText(extras.getString("firstname"));

        // Button click listener for adding comment
        addCommentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to navigate back to PostActivity
                Intent myIntent = new Intent(AddCommentActivity.this, PostActivity.class);

                // Get current date and format it
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                // Attach data to the intent
                myIntent.setData(getIntent().getData());
                myIntent.putExtra("firstname", extras.getString("firstname"));
                myIntent.putExtra("indicator", "afterComment");
                myIntent.putExtra("email", extras.getString("email"));
                myIntent.putExtra("message", userMessage.getText().toString());
                myIntent.putExtra("date", formattedDate);
                startActivity(myIntent); // Start PostActivity
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
}
