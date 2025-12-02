package com.toolcheck.controller;

import com.toolcheck.model.User;
import java.util.List;

// Defines the standard operations for managing users and handling login functionality.
public interface UserControllerInterface {
    boolean createUser(User user);  // Create a new user in the system.
    boolean updateUser(User user);  // Update an existing user's information.
    boolean deleteUser(long id);    //  Delete a user from the system by their ID.
    User getUserById(long id);      // Get a user by their ID.
    List<User> getAllUsers();       // Get a list of all users in the system.
    User login(String username, String password); // Login a user with username and password
}