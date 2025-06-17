package com.example.venueverse;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class
MainActivity extends BaseActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set custom overflow icon
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d("Not null", "User authenticated!");

            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);

            // Check if email is null
            String email = currentUser.getEmail();
            if (email != null) {
                intent.putExtra("email", email);
            }

            startActivity(intent);
        }

        // Initialize buttons and set click listeners
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        Button contactButton = findViewById(R.id.contact_button);
        Button feedbackButton = findViewById(R.id.feedback_button);
        Intent intent=new Intent();
        String email;

        // Check if intent is null or the extra "email" is null
        if (intent == null || intent.getStringExtra("email") == null) {
            // If intent or email is null, set email to "nk"
            email = "vv";
        } else {
            // Otherwise, get the email from the intent
            email = intent.getStringExtra("email");
        }
        intent.putExtra("Email", email);

        // Set click listeners for buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });
    }


}
