package com.toolcheck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// DBConnection handles H2 database initialization.
public class DBConnection {

    private static final String DB_URL = "jdbc:h2:file:./data/ToolCheckSystem;AUTO_SERVER=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    // Static block runs once to create tables
    static {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Users table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id IDENTITY PRIMARY KEY," +
                            "username VARCHAR(50) UNIQUE NOT NULL," +
                            "password VARCHAR(255) NOT NULL," +
                            "role VARCHAR(10) DEFAULT 'user'," +
                            "full_name VARCHAR(100)," +
                            "email VARCHAR(100)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            // Tools table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tools (" +
                            "id IDENTITY PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL," +
                            "description CLOB," +
                            "category VARCHAR(100)," +
                            "tool_condition VARCHAR(10) DEFAULT 'good'," +
                            "status VARCHAR(15) DEFAULT 'available'," +
                            "location VARCHAR(100)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "CHECK (tool_condition IN ('excellent','good','fair','poor'))," +
                            "CHECK (status IN ('available','checked_out','maintenance'))" +
                            ")"
            );

            // Checkout Records table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS checkout_records (" +
                            "id IDENTITY PRIMARY KEY," +
                            "user_id BIGINT," +
                            "tool_id BIGINT," +
                            "checkout_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "return_date TIMESTAMP NULL," +
                            "condition_before VARCHAR(10)," +
                            "condition_after VARCHAR(10) NULL," +
                            "status VARCHAR(15) DEFAULT 'checked_out'," +
                            "FOREIGN KEY (user_id) REFERENCES users(id)," +
                            "FOREIGN KEY (tool_id) REFERENCES tools(id)," +
                            "CHECK (condition_before IN ('excellent','good','fair','poor'))," +
                            "CHECK (condition_after IN ('excellent','good','fair','poor'))," +
                            "CHECK (status IN ('checked_out','returned','overdue'))" +
                            ")"
            );

            // Reports table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS reports (" +
                            "id IDENTITY PRIMARY KEY," +
                            "user_id BIGINT," +
                            "tool_id BIGINT," +
                            "report_type VARCHAR(15)," +
                            "description CLOB," +
                            "reported_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "status VARCHAR(10) DEFAULT 'pending'," +
                            "FOREIGN KEY (user_id) REFERENCES users(id)," +
                            "FOREIGN KEY (tool_id) REFERENCES tools(id)," +
                            "CHECK (report_type IN ('damage','maintenance','lost'))," +
                            "CHECK (status IN ('pending','resolved'))" +
                            ")"
            );

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}