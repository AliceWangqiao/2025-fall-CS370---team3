package com.toolcheck.controller;

import com.toolcheck.model.UserInterface;
import java.util.List;

//acts as an intermediary between the view
//(e.g., JavaFX UI) and the data access layer (UserDAO).
public interface UserControllerInterface {

    //Attempts to log in a user with the provided username and password.
    UserInterface loginUser(String username, String password);

    // Get all users in the system.
    List<UserInterface> getAllUsers();

    // Get a specific user by their unique ID.
    UserInterface getUser(long id);

    //Adds a new user to the system.
    void addUser(UserInterface user);

    //Updates an existing user's information.
    void updateUser(UserInterface user);

    //Deletes a user by their unique ID.
    void deleteUser(long id);
}
