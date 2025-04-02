package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {
    Button btnregister, btnforget, btnlogin;
    EditText loguser, logpass;
    ImageView eyeIcon;
    FirebaseAuth mAuth;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Find views by their IDs
        btnregister = findViewById(R.id.btnregister);
        btnforget = findViewById(R.id.btnforget);
        btnlogin = findViewById(R.id.btnlogin);
        loguser = findViewById(R.id.loguser);
        logpass = findViewById(R.id.logpass);
        eyeIcon = findViewById(R.id.eye_icon);

        // Set up 'Forget Password' button click
        btnforget.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity3.class);
            startActivity(intent);
        });

        // Set up 'Register' button click
        btnregister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up 'Login' button click
        btnlogin.setOnClickListener(view -> {
            String lemail = loguser.getText().toString().trim();
            String lpassword = logpass.getText().toString().trim();

            if (lemail.isEmpty()) {
                loguser.setError("Email is required");
                return;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(lemail).matches()) {
                loguser.setError("Enter a valid email address");
                return;
            }

            if (lpassword.isEmpty()) {
                logpass.setError("Password is required");
                return;
            }

            mAuth.signInWithEmailAndPassword(lemail, lpassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            String email = intent.getStringExtra("email") == null ? "vv" : intent.getStringExtra("email");
                            intent.putExtra("Email", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Toggle password visibility on eye icon click
        eyeIcon.setOnClickListener(view -> {
            if (isPasswordVisible) {
                logpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeIcon.setImageResource(R.drawable.eyec);
            } else {
                logpass.setInputType(InputType.TYPE_CLASS_TEXT);
                eyeIcon.setImageResource(R.drawable.eyeo);
            }
            isPasswordVisible = !isPasswordVisible;
            logpass.setSelection(logpass.length()); // Keeps cursor at end
        });
    }
}
