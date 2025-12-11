package com.toolcheck.controller;

import com.toolcheck.dao.UserDAO;
import com.toolcheck.dao.UserDAOInterface;
import com.toolcheck.model.UserInterface;

import java.util.List;

// communicates with the data access layer (UserDAO) to
// perform login and CRUD operations
public class UserController implements UserControllerInterface {

    //DAO object for accessing user data
    private final UserDAOInterface userDAO;

    //Default constructor initializes the UserDAO instance.
    public UserController() {
        this.userDAO = new UserDAO();
    }

    // Attempts to log in a user by delegating to the DAO.
    @Override
    public UserInterface loginUser(String username, String password) {
        return userDAO.login(username, password);
    }

    // Get all users from the system by delegating to the DAO.
    @Override
    public List<UserInterface> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Get a user by their unique ID by delegating to the DAO.
    @Override
    public UserInterface getUser(long id) {
        return userDAO.getUser(id);
    }

    // Adds a new user by delegating to the DAO.
    @Override
    public void addUser(UserInterface user) {
        userDAO.addUser(user);
    }

    // Updates an existing user's information by delegating to the DAO.
    @Override
    public void updateUser(UserInterface user) {
        userDAO.updateUser(user);
    }

    // Deletes a user by their unique ID by delegating to the DAO.
    @Override
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }
}