package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView tvuser, tvemail, tvpass, tvconfirm;
    EditText username, email, password, cpassword;
    Button btnsignup, btnback;
    ImageView cpasswordEyeIcon;
    FirebaseAuth mAuth;
    boolean isCPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        btnsignup = findViewById(R.id.btnsignup);
        btnback = findViewById(R.id.btnback);
        cpasswordEyeIcon = findViewById(R.id.cpassword_eye_icon);

        cpasswordEyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCPasswordVisible) {
                    // Hide password
                    cpassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    cpasswordEyeIcon.setImageResource(R.drawable.eyec);
                } else {
                    // Show password
                    cpassword.setTransformationMethod(null);
                    cpasswordEyeIcon.setImageResource(R.drawable.eyeo);
                }
                isCPasswordVisible = !isCPasswordVisible;
                // Keep the cursor at the end of the text
                cpassword.setSelection(cpassword.getText().length());
            }
        });

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

                if (!Patterns.EMAIL_ADDRESS.matcher(vemail).matches()) {
                    email.setError("Enter valid email");
                    return;
                }

                if (TextUtils.isEmpty(vpassword) || vpassword.length() < 6) {
                    password.setError("Password is required and must be at least 6 characters");
                    return;
                }

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

                                                        // Navigate to LoginActivity
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish(); // Optionally close this activity
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Failed to save user details.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // If sign-up fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Back button click listener to navigate back to LoginActivity
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optionally close this activity
            }
        });
    }
}
