package com.example.venueverse;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends BaseActivity{

    private EditText nameInput;
    private EditText emailInput;
    private EditText messageInput;
    private Button submitButton;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initializing Views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        messageInput = findViewById(R.id.messageInput);
        submitButton = findViewById(R.id.submitButton);

        // Initialize Firebase Database
        reference = FirebaseDatabase.getInstance().getReference("Feedback");

        // Submit Button OnClickListener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String message = messageInput.getText().toString().trim();

                // Input validation (optional)
                if (name.isEmpty()) {
                    nameInput.setError("Name is required");
                    nameInput.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    emailInput.setError("Email is required");
                    emailInput.requestFocus();
                    return;
                }
                if (message.isEmpty()) {
                    messageInput.setError("Message is required");
                    messageInput.requestFocus();
                    return;
                }

                // Create a unique ID for each feedback
                String feedbackId = reference.push().getKey();

                // Create a Feedback object
                com.example.venueverse.Feedback feedback = new Feedback(name, email, message); // Ensure Feedback class is imported correctly

                // Save feedback to Firebase
                if (feedbackId != null) {
                    reference.child(feedbackId).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                                // Clear input fields after submission
                                nameInput.setText("");
                                emailInput.setText("");
                                messageInput.setText("");
                            } else {
                                Toast.makeText(FeedbackActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
