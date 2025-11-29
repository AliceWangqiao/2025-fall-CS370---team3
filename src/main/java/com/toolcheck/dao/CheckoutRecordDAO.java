package com.toolcheck.dao;

import com.toolcheck.DBConnection;
import com.toolcheck.model.CheckoutRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// It implements CRUD operations (Create, Read, Update, Delete) using JDBC.
public class CheckoutRecordDAO implements CheckoutRecordDAOInterface {

    // Adds a new checkout record to the database.
    @Override
    public boolean addRecord(CheckoutRecord record) {
        // Check if record already exists (update) or add new
        if (record.getId() > 0) return updateRecord(record);

        // SQL query with placeholders
        String sql = "INSERT INTO checkout_records (user_id, tool_id, checkout_date, return_date, condition_after, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set values for each placeholder in the SQL statement
            ps.setLong(1, record.getUserId());
            ps.setLong(2, record.getToolId());
            ps.setTimestamp(3, Timestamp.valueOf(record.getCheckoutDate()));
            ps.setTimestamp(4, record.getReturnDate() != null ? Timestamp.valueOf(record.getReturnDate()) : null);
            ps.setString(5, record.getConditionAfter());
            ps.setString(6, record.getStatus());

            // Execute the statement
            int affected = ps.executeUpdate();

            // If a row was inserted, get the generated ID and assign it to the record
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) record.setId(keys.getLong(1));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding checkout record: " + e.getMessage());
        }
        return false;
    }

    // Updates an existing checkout record in the database.
    @Override
    public boolean updateRecord(CheckoutRecord record) {
        String sql = "UPDATE checkout_records SET user_id=?, tool_id=?, checkout_date=?, return_date=?, condition_after=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set values for each placeholder in the SQL statement
            ps.setLong(1, record.getUserId());
            ps.setLong(2, record.getToolId());
            ps.setTimestamp(3, Timestamp.valueOf(record.getCheckoutDate()));
            ps.setTimestamp(4, record.getReturnDate() != null ? Timestamp.valueOf(record.getReturnDate()) : null);
            ps.setString(5, record.getConditionAfter());
            ps.setString(6, record.getStatus());
            ps.setLong(7, record.getId());

            // Execute the statement and return true if at least one row was updated
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating checkout record: " + e.getMessage());
        }
        return false;
    }

    // Get all checkout records from the database.
    @Override
    public List<CheckoutRecord> getAllRecords() {
        List<CheckoutRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM checkout_records";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Loop through each row in the result set
            while (rs.next()) {
                CheckoutRecord r = new CheckoutRecord();
                r.setId(rs.getLong("id"));
                r.setUserId(rs.getLong("user_id"));
                r.setToolId(rs.getLong("tool_id"));
                r.setCheckoutDate(rs.getTimestamp("checkout_date").toLocalDateTime());

                // Handle nullable return_date
                Timestamp returnTs = rs.getTimestamp("return_date");
                if (returnTs != null) r.setReturnDate(returnTs.toLocalDateTime());
                r.setConditionAfter(rs.getString("condition_after"));
                r.setStatus(rs.getString("status"));
                records.add(r);     // Add record to the list
            }
        } catch (SQLException e) {
            System.out.println("Error fetching checkout records: " + e.getMessage());
        }
        return records;
    }

    // Retrieves a single checkout record by its ID.
    @Override
    public CheckoutRecord getRecordById(long id) {
        String sql = "SELECT * FROM checkout_records WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CheckoutRecord r = new CheckoutRecord();
                r.setId(rs.getLong("id"));
                r.setUserId(rs.getLong("user_id"));
                r.setToolId(rs.getLong("tool_id"));
                r.setCheckoutDate(rs.getTimestamp("checkout_date").toLocalDateTime());
                Timestamp returnTs = rs.getTimestamp("return_date");
                if (returnTs != null) r.setReturnDate(returnTs.toLocalDateTime());
                r.setConditionAfter(rs.getString("condition_after"));
                r.setStatus(rs.getString("status"));
                return r;   // Return the found record
            }
        } catch (SQLException e) {
            System.out.println("Error fetching checkout record: " + e.getMessage());
        }
        return null;    // No record found
    }
}