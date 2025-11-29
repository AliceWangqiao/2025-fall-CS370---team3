package com.toolcheck.dao;

import com.toolcheck.model.User;
import java.util.List;

//  UserDAOInterface defines the standard operations for managing User data.
public interface UserDAOInterface {
    boolean addUser(User user);     // Add a new user to the system
    boolean updateUser(User user);  // Update an existing user's information
    boolean deleteUser(long id);    // Delete a user by their ID
    User getUserById(long id);      // Get a user by their ID.
    List<User> getAllUsers();       // Get all users in the system.
}
