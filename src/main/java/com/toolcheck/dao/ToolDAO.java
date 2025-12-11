package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.Tool;
import com.toolcheck.model.ToolInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//handles CRUD operations, as well as checkout and return of tools.
public class ToolDAO implements ToolDAOInterface {

    // Get  all tools from the database.
    @Override
    public List<ToolInterface> getAllTools() {
        List<ToolInterface> tools = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tools")) {

            // Loop through result set and map each row to a Tool object
            while (rs.next()) {
                Tool t = new Tool();
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setCondition(rs.getString("condition"));
                t.setStatus(rs.getString("status"));
                t.setUserId(rs.getLong("userId"));
                tools.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tools;
    }

    // Get  a tool by its ID.
    @Override
    public ToolInterface getTool(long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM tools WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tool t = new Tool();
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setCondition(rs.getString("condition"));
                t.setStatus(rs.getString("status"));
                t.setUserId(rs.getLong("userId"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Adds a new tool to the database.
    @Override
    public void addTool(ToolInterface tool) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tools(name, condition, status, userId) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, tool.getName());
            ps.setString(2, tool.getCondition());
            ps.setString(3, tool.getStatus());
            ps.setLong(4, tool.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Updates an existing tool's information in the database.
    @Override
    public void updateTool(ToolInterface tool) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE tools SET name=?, condition=?, status=?, userId=? WHERE id=?")) {
            ps.setString(1, tool.getName());
            ps.setString(2, tool.getCondition());
            ps.setString(3, tool.getStatus());
            ps.setLong(4, tool.getUserId());
            ps.setLong(5, tool.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Deletes a tool from the database by ID.
    @Override
    public void deleteTool(long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tools WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Checks out a tool to a specific user.
    //Updates the tool's status to 'Checked Out' and sets the userId.
    @Override
    public void checkoutTool(long toolId, long userId) throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE tools SET status='Checked Out', userId=? WHERE id=?")) {
            ps.setLong(1, userId);
            ps.setLong(2, toolId);
            ps.executeUpdate();
        }
    }

    //Returns a tool from a user.
    //Updates the tool's status to 'Available', sets its condition, and clears userId.
    @Override
    public void returnTool(long toolId, long userId, String condition) throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE tools SET status='Available', condition=?, userId=0 WHERE id=?")) {
            ps.setString(1, condition);
            ps.setLong(2, toolId);
            ps.executeUpdate();
        }
    }
}