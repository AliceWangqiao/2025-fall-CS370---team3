package com.toolcheck.dao;

import com.toolcheck.model.UserInterface;
import java.util.List;

// Interface for user database operations (login, add, update, delete).
public interface UserDAOInterface {

    // Attempts to log in a user with the given username and password
    UserInterface login(String username, String password);

    // Retrieves a list of all users in the system
    List<UserInterface> getAllUsers();

    // Retrieves a specific user by their unique ID
    UserInterface getUser(long id);

    // Adds a new user to the system
    void addUser(UserInterface user);

    // Updates the information of an existing user
    void updateUser(UserInterface user);

    // Deletes a user by their unique ID (soft delete or permanent depending on implementation)
    void deleteUser(long id);
}