package com.toolcheck;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties props = new Properties();

    static {
        // Load configuration
        File config = new File(CONFIG_FILE);
        if (config.exists()) {
            try (FileInputStream in = new FileInputStream(config)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("Failed to load config.properties, using defaults.");
            }
        }

        // Initialize database schema
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // --- Users table ---
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "username VARCHAR(50) UNIQUE NOT NULL," +
                            "password VARCHAR(255) NOT NULL," +
                            "role VARCHAR(10) DEFAULT 'user'," +
                            "full_name VARCHAR(100)," +
                            "email VARCHAR(100)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "CHECK (role IN ('admin','user'))" +
                            ")"
            );

            // --- Tools table ---
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tools (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL," +
                            "description CLOB," +
                            "category VARCHAR(50)," +
                            "tool_condition VARCHAR(10) DEFAULT 'good'," +
                            "status VARCHAR(15) DEFAULT 'available'," +
                            "location VARCHAR(100)," +
                            "CHECK (tool_condition IN ('excellent','good','fair','poor'))," +
                            "CHECK (status IN ('available','checked_out','maintenance'))" +
                            ")"
            );

            // --- Checkout Records table ---
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS checkout_records (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "user_id INT," +
                            "tool_id INT," +
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

            // --- Reports table ---
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS reports (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "user_id INT," +
                            "tool_id INT," +
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

            // --- Sample data for users ---
            stmt.executeUpdate(
                    "MERGE INTO users KEY(id) VALUES " +
                            "(1, 'admin', 'admin123', 'admin', 'Admin User', 'admin@example.com')," +
                            "(2, 'john_doe', 'password1', 'user', 'John Doe', 'john@example.com')," +
                            "(3, 'jane_smith', 'password2', 'user', 'Jane Smith', 'jane@example.com')"
            );

            // --- Sample data for tools ---
            stmt.executeUpdate(
                    "MERGE INTO tools KEY(id) VALUES " +
                            "(1, 'Hammer', 'Heavy-duty hammer', 'Hand Tool', 'excellent', 'available', 'Tool Room A')," +
                            "(2, 'Drill', 'Electric drill', 'Power Tool', 'good', 'available', 'Tool Room B')"
            );

            System.out.println("All tables initialized successfully in H2.");

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String h2Url = props.getProperty("DB_URL", "jdbc:h2:file:./data/ToolCheckSystem;AUTO_SERVER=FALSE");
        String h2User = props.getProperty("DB_USER", "sa");
        String h2Password = props.getProperty("DB_PASSWORD", "");
        return DriverManager.getConnection(h2Url, h2User, h2Password);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to H2 database successfully!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}