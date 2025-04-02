package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.venueverse.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmationActivity extends BaseActivity {
    ImageView confirmationImage;
    Button dashboardButton;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set custom overflow icon
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

        confirmationImage = findViewById(R.id.confirmation_image);
        dashboardButton = findViewById(R.id.dashboard_button);

        // Get data from intent


        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(Email);
            }
        });
    }

    // Method to navigate to DashboardActivity and pass data
    public void goToDashboard(String username) {
        Intent dintent = new Intent(ConfirmationActivity.this, DashboardActivity.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Email = currentUser.getEmail();
        }
        dintent.putExtra("Email", Email);

        startActivity(dintent);
    }
}
