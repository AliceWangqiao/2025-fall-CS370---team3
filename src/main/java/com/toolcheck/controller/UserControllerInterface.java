package com.toolcheck.controller;

import com.toolcheck.model.User;
import java.util.List;

// It is used by the controller layer to manage users in the system.
public interface UserControllerInterface {
    boolean createUser(User user);  // Create a new user in the system.
    boolean updateUser(User user);  // Update an existing user's information.
    boolean deleteUser(long id);    //  Delete a user from the system by their ID.
    User getUserById(long id);      // Get a user by their ID.
    List<User> getAllUsers();       // Get a list of all users in the system.
}