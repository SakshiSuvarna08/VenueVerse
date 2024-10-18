package com.example.venueverse;

public class User {
    private String userId;   // Firebase user ID
    private String username;  // Username of the user
    private String email;     // Email of the user

    // Default constructor (required for Firebase)
    public User() {}

    // Constructor to initialize user details
    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    // Getter methods (optional, but useful for accessing fields)
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
