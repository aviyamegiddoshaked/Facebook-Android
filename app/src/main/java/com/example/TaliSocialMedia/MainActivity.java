package com.example.taliSocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taliSocialMedia.network.LoginResponseResult;
import com.example.taliSocialMedia.network.User;
import com.example.taliSocialMedia.repository.UserRepository;
import com.example.taliSocialMedia.signin.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        String userId = UserRepository.getUserId(this);
        if (userId != null) {
            Intent myIntent = new Intent(MainActivity.this, PostActivity.class);

            // Get Uri from Intent
            Uri userImage = getIntent().getData();
            myIntent.setData(userImage);

            // Add flags to the intent
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

            String email = UserRepository.getEmail(MainActivity.this);
            // Put extras to the intent
            myIntent.putExtra("firstname", email);
            myIntent.putExtra("email", email);
            myIntent.putExtra("indicator", "main");

            // Start the activity
            startActivity(myIntent);
            finish();
        }

        // Initialize UI elements
        TextView Title, gotoRegister;
        EditText emailET, passwordET;
        Button enterBTN;

        // Find UI elements by their IDs
        Title = findViewById(R.id.title_TV);
        enterBTN = findViewById(R.id.enterBTN);
        emailET = findViewById(R.id.useremailET);
        passwordET = findViewById(R.id.passwordET);
        gotoRegister = findViewById(R.id.registerTV);

        // Click listener for "Register" TextView
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        // Click listener for "Enter" Button
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve stored email, password, and firstname
                String userInputEmail, userInputPassword;

                // Get user input email and password
                userInputEmail = emailET.getText().toString();
                userInputPassword = passwordET.getText().toString();

                viewModel.login(userInputEmail, userInputPassword).observe(MainActivity.this, new Observer<LoginResponseResult>() {
                    @Override
                    public void onChanged(LoginResponseResult result) {
                        if (result.isSuccess()) {
                            // If matched, create intent to start PostActivity
                            Intent myIntent = new Intent(MainActivity.this, PostActivity.class);

                            // Get Uri from Intent
                            Uri userImage = getIntent().getData();
                            myIntent.setData(userImage);

                            // Add flags to the intent
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                            // Put extras to the intent
                            myIntent.putExtra("firstname", emailET.getText().toString());
                            myIntent.putExtra("email", emailET.getText().toString());
                            myIntent.putExtra("indicator", "main");

                            // Start the activity
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(MainActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
