package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// It implements CRUD operations (Create, Read, Update, Delete) using JDBC.
public class UserDAO implements UserDAOInterface {

    // Add a new user to the database.
    @Override
    public boolean addUser(User user) {

        // SQL query with placeholders
        String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();        // Get database connection
             // Prepare statement to prevent SQL injection and allow generated keys
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set values in the query from the User object
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());

            // Execute the query
            int affected = ps.executeUpdate();

            // If insertion successful, get the auto-generated ID
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) user.setId(keys.getLong(1));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
        return false;
    }

    // Update an existing user's information in the database.
    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username=?, password=?, role=?, full_name=?, email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set new values for the user
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setLong(6, user.getId());        // identify which user to update


            return ps.executeUpdate() > 0;       // return true if update affected at least 1 row
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    // Delete a user from the database by ID.
    @Override
    public boolean deleteUser(long id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);  // set the user ID to delete
            return ps.executeUpdate() > 0;      // true if deletion affected a row
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    // Get a single user by ID.
    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);      // set ID parameter
            ResultSet rs = ps.executeQuery();    // // execute query
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
        return null;
    }

    // Get a list of all users from the database.
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                users.add(u);       // add each user to the list
            }

        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }
}