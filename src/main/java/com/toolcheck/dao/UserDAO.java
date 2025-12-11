package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.User;
import com.toolcheck.model.UserInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// handles login, CRUD operations, and uses a helper
// method to map database rows to User objects.
public class UserDAO implements UserDAOInterface {

    // Attempts to log in a user by checking the username and password.
    // Only users not marked as deleted can log in.
    @Override
    public UserInterface login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username=? AND password=? AND deleted=FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the username and password parameters
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Map the row to a User object
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //Gets all users that are not marked as deleted.
    @Override
    public List<UserInterface> getAllUsers() {
        List<UserInterface> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE deleted=FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Loop through the result set and map each row to a User object
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get a specific user by their unique ID, only if not deleted.
    @Override
    public UserInterface getUser(long id) {
        String sql = "SELECT * FROM users WHERE id=? AND deleted=FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Adds a new user to the database.
    @Override
    public void addUser(UserInterface user) {
        String sql = "INSERT INTO users (username, fullName, password, role, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getEmail());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Updates an existing user's information in the database.
    @Override
    public void updateUser(UserInterface user) {
        String sql = "UPDATE users SET username=?, fullName=?, password=?, role=?, email=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getEmail());
            ps.setLong(6, user.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Marks a user as deleted in the database
    @Override
    public void deleteUser(long id) {
        String sql = "UPDATE users SET deleted=TRUE WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Maps a ResultSet row to a User object.
    private UserInterface mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("fullName"));
        u.setRole(rs.getString("role"));
        u.setEmail(rs.getString("email"));
        u.setDeleted(rs.getBoolean("deleted"));
        return u;
    }
}