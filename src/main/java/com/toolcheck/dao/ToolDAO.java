package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// It implements CRUD operations (Create, Read, Update, Delete) using JDBC.
public class ToolDAO implements ToolDAOInterface {

    // Add a new Tool to the database.
    @Override
    public boolean addTool(Tool tool) {

        // SQL query with placeholders
        String sql = "INSERT INTO tools (name, description, category, tool_condition, status, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set values in the SQL query from the Tool object
            ps.setString(1, tool.getName());
            ps.setString(2, tool.getDescription());
            ps.setString(3, tool.getCategory());
            ps.setString(4, tool.getToolCondition());
            ps.setString(5, tool.getStatus());
            ps.setString(6, tool.getLocation());

            // Execute the query
            int affected = ps.executeUpdate();

            // If insertion is successful, get the generated ID and set it in the Tool object
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) tool.setId(keys.getLong(1));
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding tool: " + e.getMessage());
        }
        return false;
    }

    // Update an existing Tool in the database.
    @Override
    public boolean updateTool(Tool tool) {
        String sql = "UPDATE tools SET name=?, description=?, category=?, tool_condition=?, status=?, location=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tool.getName());
            ps.setString(2, tool.getDescription());
            ps.setString(3, tool.getCategory());
            ps.setString(4, tool.getToolCondition());
            ps.setString(5, tool.getStatus());
            ps.setString(6, tool.getLocation());
            ps.setLong(7, tool.getId());

            // Execute the update; returns true if at least one row is updated
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating tool: " + e.getMessage());
        }
        return false;
    }

    // Delete a Tool from the database using its ID.
    @Override
    public boolean deleteTool(long id) {
        String sql = "DELETE FROM tools WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            // Execute the delete; returns true if at least one row is deleted
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting tool: " + e.getMessage());
        }
        return false;
    }

    // Get a Tool from the database using its ID.
    @Override
    public Tool getToolById(long id) {
        String sql = "SELECT * FROM tools WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Create a Tool object and set all fields from the result set
                Tool t = new Tool();
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setCategory(rs.getString("category"));
                t.setToolCondition(rs.getString("tool_condition"));
                t.setStatus(rs.getString("status"));
                t.setLocation(rs.getString("location"));
                return t;
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tool: " + e.getMessage());
        }
        return null;    // Tool not found
    }

    // Get all tools from the database.
    @Override
    public List<Tool> getAllTools() {
        List<Tool> tools = new ArrayList<>();
        String sql = "SELECT * FROM tools";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // // Loop through all results and create Tool objects
            while (rs.next()) {
                Tool t = new Tool();
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setCategory(rs.getString("category"));
                t.setToolCondition(rs.getString("tool_condition"));
                t.setStatus(rs.getString("status"));
                t.setLocation(rs.getString("location"));
                tools.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tools: " + e.getMessage());
        }
        return tools;
    }
}