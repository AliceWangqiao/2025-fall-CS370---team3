package com.toolcheck.controller;

import com.toolcheck.dao.UserDAOInterface;
import com.toolcheck.model.User;

import java.util.List;

//  It acts as a bridge between the data layer (DAO) and the application logic.
public class UserController implements UserControllerInterface {

    // DAO interface to perform database or storage operations for users
    private final UserDAOInterface dao;

    // This allows the controller to use any DAO implementation.
    public UserController(UserDAOInterface dao) {
        this.dao = dao;
    }

    // Creates a new user.
    @Override
    public boolean createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) return false;
        return dao.addUser(user);
    }

    // Updates an existing user.
    @Override
    public boolean updateUser(User user) {
        return dao.updateUser(user);
    }

    // Deletes a user by ID.
    @Override
    public boolean deleteUser(long id) {
        return dao.deleteUser(id);
    }

    // Get a user by ID.
    @Override
    public User getUserById(long id) {
        return dao.getUserById(id);
    }

    // Get all users in the system.
    @Override
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    // Login a user by username and password.
    @Override
    public User login(String username, String password) {
        return dao.login(username, password);
    }
}