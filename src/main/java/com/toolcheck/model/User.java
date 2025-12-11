package com.toolcheck.model;

//User class implementing UserInterface, holding basic user information.
public class User implements UserInterface {
    // Unique ID of the user
    private long id;

    // Username for login
    private String username;

    // Password for login
    private String password;

    // Role of the user (e.g., admin, user)
    private String role;

    // Full name of the user
    private String fullName;

    // Email address of the user
    private String email;

    // if the user is deleted
    private boolean deleted;

    // Default constructor
    public User() {}

    // Convenience constructor to initialize all user fields
    public User(long id, String username, String password, String role, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
    }

    // Gets the ID of the user
    @Override public long getId() { return id; }

    // Sets the ID of the user
    @Override public void setId(long id) { this.id = id; }

    // Gets the username of the user
    @Override public String getUsername() { return username; }

    // Sets the username of the user
    @Override public void setUsername(String username) { this.username = username; }

    // Gets the password of the user
    @Override public String getPassword() { return password; }

    // Sets the password of the user
    @Override public void setPassword(String password) { this.password = password; }

    // Gets the role of the user
    @Override public String getRole() { return role; }

    // Sets the role of the user
    @Override public void setRole(String role) { this.role = role; }

    // Gets the full name of the user
    @Override public String getFullName() { return fullName; }

    // Sets the full name of the user
    @Override public void setFullName(String fullName) { this.fullName = fullName; }

    // Gets the email of the user
    @Override public String getEmail() { return email; }

    // Sets the email of the user
    @Override public void setEmail(String email) { this.email = email; }

    // Checks if the user is marked as deleted
    @Override public boolean isDeleted() { return deleted; }

    // Sets the deleted flag of the user
    @Override public void setDeleted(boolean deleted) { this.deleted = deleted; }
}