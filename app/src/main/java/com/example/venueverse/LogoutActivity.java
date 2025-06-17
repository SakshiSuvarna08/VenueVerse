package com.example.venueverse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutActivity extends BaseActivity {
    FirebaseAuth auth;
    Button btnlogout,feed;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);
        auth = FirebaseAuth.getInstance();
        btnlogout = findViewById(R.id.logoutButton);
        feed = findViewById(R.id.feedb);
        user = auth.getCurrentUser();
        if (user==null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
            startActivity(intent);
            finish();
        }
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
                feed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

}