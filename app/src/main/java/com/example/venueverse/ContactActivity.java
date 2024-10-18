package com.example.venueverse;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Optionally, remove the default title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable overflowIcon = ContextCompat.getDrawable(this, R.drawable.menu_icon);
        if (toolbar != null) {
            toolbar.setOverflowIcon(overflowIcon);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        boolean isLoggedIn = checkLoginState();

        // Hide or show menu items based on login state
        MenuItem loginItem = menu.findItem(R.id.menu_sign_in);
        loginItem.setVisible(!isLoggedIn);

        MenuItem registerItem = menu.findItem(R.id.menu_sign_up);
        registerItem.setVisible(!isLoggedIn);

        MenuItem logoutItem = menu.findItem(R.id.menu_sign_out);
        logoutItem.setVisible(isLoggedIn);

        MenuItem dashboardItem = menu.findItem(R.id.menu_dashboard);
        dashboardItem.setVisible(isLoggedIn);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Check login state
        boolean isLoggedIn = checkLoginState();

        // Show or hide items based on login state
        menu.findItem(R.id.menu_sign_in).setVisible(!isLoggedIn);
        menu.findItem(R.id.menu_sign_out).setVisible(isLoggedIn);
        menu.findItem(R.id.menu_dashboard).setVisible(isLoggedIn);

        return super.onPrepareOptionsMenu(menu);
    }

    // Method to check login state (you need to implement this)
    private boolean checkLoginState() {
        // Check whether the user is logged in
        // Return true if logged in, false otherwise
        return false; // Example placeholder
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            // Start HomeActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_welcome) {
            // Start WelcomeActivity
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_feedback) {
            // Start FeedbackActivity
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_contact) {
            // Start ContactUsActivity
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sign_in) {
            // Start LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sign_up) {
            // Start RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_dashboard) {
            // Start DashboardActivity
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sign_out) {
            // Start LogoutActivity (or handle logout process)
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}