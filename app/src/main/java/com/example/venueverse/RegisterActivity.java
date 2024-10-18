package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView tvuser,tvemail,tvpass,tvconfirm;
    EditText username,email,password,cpassword;
    Button btnsignup,btnback;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(RegisterActivity.this, "User already exists with this email",
                    Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(RegisterActivity.this, LogoutActivity.class);
            startActivity(intent2);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        btnsignup = findViewById(R.id.btnsignup);
        btnback = findViewById(R.id.btnback);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vusername, vemail, vpassword, vcpassword;
                vusername = username.getText().toString().trim();
                vemail = email.getText().toString().trim();
                vpassword = password.getText().toString().trim();
                vcpassword = cpassword.getText().toString().trim();

                if (TextUtils.isEmpty(vusername)) {
                    username.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(vemail)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(vpassword)) {
                    password.setError("Password is required");
                    return;
                }




                if (TextUtils.isEmpty(vcpassword)) {
                    cpassword.setError("Confirm Password is required");
                    return;
                }

                // Check if password and confirm password match
                if (!vpassword.equals(vcpassword)) {
                    cpassword.setError("Passwords do not match");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(vemail, vpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    String userId = currentUser.getUid();

                                    // Create User object
                                    User user = new User(userId, vusername, vemail);

                                    // Get a reference to the database
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

                                    // Store user details in the Realtime Database
                                    database.child(userId).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Account created and details saved.", Toast.LENGTH_SHORT).show();
                                                        username.setText("");
                                                        email.setText("");
                                                        password.setText("");
                                                        cpassword.setText("");

                                                        // Navigate to MainActivity
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish(); // Optionally close this activity
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Failed to save user details.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
