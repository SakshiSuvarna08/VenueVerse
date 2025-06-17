package com.example.venueverse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity3 extends AppCompatActivity {
    Button btnsend;
    EditText forgotmail;
    FirebaseAuth mAuth; // Declare mAuth

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent=new Intent();
        String email;

        email = intent.getStringExtra("Email");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        forgotmail = findViewById(R.id.forgotmail);
        btnsend = findViewById(R.id.btnsend);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = forgotmail.getText().toString().trim();

                if (!TextUtils.isEmpty(strEmail)) {
                    ResetPassword(strEmail); // Pass the email as an argument
                } else {
                    forgotmail.setError("Email is required");
                }
            }
        });
    }

    private void ResetPassword(String strEmail) { // Accept email as a parameter
        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity3.this, "Reset password link is sent to your email",
                                Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(MainActivity3.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity3.this, "Something went wrong! Try again later...",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
