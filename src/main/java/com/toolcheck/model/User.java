package com.toolcheck.model;

// It extends the Model class to inherit common fields like id and createdAt.
public class User extends Model {
    private String username;        // Username of the user (must be unique)
    private String password;        // Password for login
    private String role = "user";   // Role of the user, default is "user" (can be "admin")
    private String fullName;        // Full name of the user
    private String email;           // Email address of the user

    // No-argument constructor.
    // Useful for creating an empty Tool object and setting fields later.
    public User() {}

    // Constructor to create a new User with all details.
    public User(String username, String password, String role, String fullName, String email) {
        this.username = username;
        this.password = password;
        if(role != null && !role.isBlank()) this.role = role;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and setters
    public String getUsername() {
        return username;        // Get the username of the user
    }
    public void setUsername(String username) {
        this.username = username; // Set the username of the user
    }
    public String getPassword() {
        return password;            // Get the password of the user
    }
    public void setPassword(String password) {
        this.password = password;  // Set the password of the user
    }
    public String getRole() {
        return role;                // Get the role of the user (admin/user)
    }
    public void setRole(String role) {
        this.role = role;           // Set the role of the user
    }
    public String getFullName() {
        return fullName;           // Get the full name of the user
    }
    public void setFullName(String fullName) {
        this.fullName = fullName; // Set the full name of the user
    }
    public String getEmail() {
        return email;               // Get the email address of the user
    }
    public void setEmail(String email) {
        this.email = email;         // Set the email address of the user
    }
}