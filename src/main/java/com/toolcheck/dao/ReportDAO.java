package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// It implements CRUD operations (Create, Read, Update, Delete) using JDBC.
public class ReportDAO implements ReportDAOInterface {

    // Add a new report to the database.
    @Override
    public boolean addReport(Report report) {

        // SQL query with placeholders
        String sql = "INSERT INTO reports (user_id, tool_id, report_type, description, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set values for the placeholders in the SQL query
            ps.setLong(1, report.getUserId());
            ps.setLong(2, report.getToolId());
            ps.setString(3, report.getReportType());
            ps.setString(4, report.getDescription());
            ps.setString(5, report.getStatus());

            // Execute the insert statement
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) report.setId(keys.getLong(1));
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding report: " + e.getMessage());
        }
        return false;
    }

    // Mark a report as resolved in the database.
    @Override
    public boolean resolveReport(long id) {

        // SQL update statement to set status = 'resolved'
        String sql = "UPDATE reports SET status='resolved' WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);  // Set the ID placeholder
            return ps.executeUpdate() > 0;  // Returns true if at least one row was updated
        } catch (SQLException e) {
            System.out.println("Error resolving report: " + e.getMessage());
        }
        return false;
    }

    // Get all reports from the database.
    @Override
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports";       // SQL select all reports
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Loop through the result set and create Report objects
            while (rs.next()) {
                Report r = new Report();
                r.setId(rs.getLong("id"));
                r.setUserId(rs.getLong("user_id"));
                r.setToolId(rs.getLong("tool_id"));
                r.setReportType(rs.getString("report_type"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                reports.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching reports: " + e.getMessage());
        }
        return reports;
    }

    //  Retrieve a single report by its ID.
    @Override
    public Report getReportById(long id) {
        String sql = "SELECT * FROM reports WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);      // Set the ID placeholder
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {             // If a record is found
                Report r = new Report();
                r.setId(rs.getLong("id"));
                r.setUserId(rs.getLong("user_id"));
                r.setToolId(rs.getLong("tool_id"));
                r.setReportType(rs.getString("report_type"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                return r;
            }

        } catch (SQLException e) {
            System.out.println("Error fetching report: " + e.getMessage());
        }
        return null;
    }
}