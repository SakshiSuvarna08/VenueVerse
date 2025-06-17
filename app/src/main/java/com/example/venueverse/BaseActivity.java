package com.example.venueverse;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Ensure you have this layout with the Toolbar

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Set up the custom toolbar
        Drawable overflowIcon = ContextCompat.getDrawable(this, R.drawable.menu_icon); // Adjust according to your drawable
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setOverflowIcon(overflowIcon);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        updateMenuItems(menu); // Update visibility based on login state
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuItems(menu); // Update visibility before showing the menu
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuItems(Menu menu) {
        boolean isLoggedIn = checkLoginState();

        // Show or hide menu items based on login state
        menu.findItem(R.id.menu_sign_in).setVisible(!isLoggedIn);
        menu.findItem(R.id.menu_welcome).setVisible(isLoggedIn);
        menu.findItem(R.id.menu_sign_up).setVisible(!isLoggedIn);
        menu.findItem(R.id.menu_sign_out).setVisible(isLoggedIn);
        menu.findItem(R.id.menu_dashboard).setVisible(isLoggedIn);
        menu.findItem(R.id.menu_home).setVisible(!isLoggedIn); // Example for home
    }

    private boolean checkLoginState() {
        // Check whether the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    protected String getEmailId() {
        if (checkLoginState()) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            return currentUser.getEmail();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String email = getEmailId(); // Retrieve the user's email

        Intent intent = null; // Declare intent variable

        // Using if-else instead of switch statement
        if (id == R.id.menu_home) {
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.menu_welcome) {
            intent = new Intent(this, WelcomeActivity.class);
        } else if (id == R.id.menu_feedback) {
            intent = new Intent(this, FeedbackActivity.class);
        } else if (id == R.id.menu_contact) {
            intent = new Intent(this, ContactActivity.class);
        } else if (id == R.id.menu_sign_in) {
            intent = new Intent(this, LoginActivity.class);
        } else if (id == R.id.menu_sign_up) {
            intent = new Intent(this, RegisterActivity.class);
        } else if (id == R.id.menu_dashboard) {
            intent = new Intent(this, DashboardActivity.class);
        } else if (id == R.id.menu_sign_out) {
            intent = new Intent(this, LogoutActivity.class);
        } else {
            return super.onOptionsItemSelected(item);
        }

        // Add the email to the intent for all cases
        if (intent != null) {
            String Email = intent.getStringExtra("Email");
            intent.putExtra("Email", Email);
            intent.putExtra("Email", email);
            // Sending the email as an extra
            startActivity(intent);
        }

        return true; // Indicate that the event was handled
    }
}
