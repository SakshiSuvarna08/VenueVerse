package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.venueverse.DashboardActivity;
public class ConfirmationActivity extends AppCompatActivity {
    ImageView confirmationImage;
    Button dashboardButton;
    String username;
    boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);


        confirmationImage = findViewById(R.id.confirmation_image);
        dashboardButton = findViewById(R.id.dashboard_button);

        // Get data from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "nk";  // Use a default value or handle the error
        }


        // Set onClick listener for the dashboard button
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(username);
            }
        });
    }

    // Method to navigate to DashboardActivity and pass data
    public void goToDashboard(String username) {
        Intent intent = new Intent(ConfirmationActivity.this, DashboardActivity.class);
        // Pass the data to DashboardActivity
        intent.putExtra("username", username);

        startActivity(intent);
    }
}
