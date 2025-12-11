package com.toolcheck.model;

// Interface for User objects, providing access to ID, username, password, role, full name, email, and deleted
public interface UserInterface {
    // Gets the unique ID of the user.
    long getId();

    // Sets the unique ID of the user.
    void setId(long id);

    //Gets the username of the user.
    String getUsername();

    //Sets the username of the user.
    void setUsername(String username);

    //Gets the password of the user.
    String getPassword();

    // Sets the password of the user.
    void setPassword(String password);

    // Gets the role of the user (e.g., admin, user).
    String getRole();

    // Sets the role of the user.
    void setRole(String role);

    // Gets the full name of the user.
    String getFullName();

    // Sets the full name of the user.
    void setFullName(String fullName);

    // Gets the email address of the user.
    String getEmail();

    // Sets the email address of the user.
    void setEmail(String email);

    // Checks if the user is marked as deleted.
    boolean isDeleted();

    // Sets the deleted status of the user.
    void setDeleted(boolean deleted);
}