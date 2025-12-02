package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 // All operations use JDBC to connect and interact with the database.
public class UserDAO implements UserDAOInterface {

    // Login method: checks if a user exists with the given username and password.
    @Override
    public User login(String username, String password) {
        // SQL query to find a matching user
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set query parameters
            ps.setString(1, username);
            ps.setString(2, password);

            // Execute query
            ResultSet rs = ps.executeQuery();

            // If user found, create and return User object
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                return user; // login successful
            }

        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return null; // login failed
    }


     // Add a new user to the database.
    @Override
    public boolean addUser(User user) {
        // SQL insert query
        String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set values for query placeholders
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());

            // Execute insert query
            int affected = ps.executeUpdate();

            // If insert succeeded, get the auto-generated ID
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) user.setId(keys.getLong(1));
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }

        return false; // insert failed
    }


     // Update an existing user's information in the database.
    @Override
    public boolean updateUser(User user) {
        // SQL update query
        String sql = "UPDATE users SET username=?, password=?, role=?, full_name=?, email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set new values in the query
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setLong(6, user.getId()); // identify which user to update

            // Return true if at least 1 row was updated
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }

        return false; // update failed
    }

    //Delete a user by their ID.
    @Override
    public boolean deleteUser(long id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id); // set the ID to delete
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }

        return false; // deletion failed
    }

    // Get a single user by their ID.
    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            // If user exists, populate and return User object
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                return u;
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }

        return null; // user not found
    }

    // Get a list of all users in the system.
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Loop through result set and populate list
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                users.add(u); // add user to list
            }

        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }

        return users;
    }
}