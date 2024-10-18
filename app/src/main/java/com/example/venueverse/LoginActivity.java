package com.example.venueverse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnregister, btnforget, btnlogin;
    EditText loguser, logpass;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        btnregister = findViewById(R.id.btnregister);
        btnforget = findViewById(R.id.btnforget);
        btnlogin = findViewById(R.id.btnlogin);
        loguser = findViewById(R.id.loguser);
        logpass = findViewById(R.id.logpass);

        btnforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lemail, lpassword;
                lemail = loguser.getText().toString().trim();
                lpassword = logpass.getText().toString().trim();

                if (lemail.isEmpty()) {
                    loguser.setError("Email is required");
                    return;
                }

                if (lpassword.isEmpty()) {
                    logpass.setError("Password is required");
                    return;
                }

                // Sign in the user
                mAuth.signInWithEmailAndPassword(lemail, lpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Sign in fails
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}
