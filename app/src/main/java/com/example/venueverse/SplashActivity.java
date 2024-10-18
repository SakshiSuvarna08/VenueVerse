package com.example.venueverse;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.View.OnApplyWindowInsetsListener;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    // Handle window insets here if needed
                    return insets;
                }
            });
        }

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // Adjust the delay as needed
    }
}

