package com.toolcheck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

// Uses H2 database with automatic server.
public class DBConnection {

    // JDBC URL for H2 database in user home directory with AUTO_SERVER mode
    private static final String DB_URL = "jdbc:h2:~/toolcheckDB;AUTO_SERVER=TRUE";

    // Database username
    private static final String USER = "sa";

    // Database password (empty for default H2)
    private static final String PASS = "";

    // establishes and returns a database connection.
    public static Connection getConnection() {

        try {
            // Attempt to connect to the H2 database
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void initDB() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "fullName VARCHAR(100), " +
                    "password VARCHAR(50) NOT NULL, " +
                    "role VARCHAR(20), " +
                    "email VARCHAR(100), " +
                    "deleted BOOLEAN DEFAULT FALSE" +
                    ")");

            // Insert default admin and user accounts if they do not exist.
            stmt.execute("MERGE INTO users (username, fullName, password, role, email) KEY(username) " +
                    "VALUES('admin','Admin Admin','admin123','admin','admin@example.com')");

            stmt.execute("MERGE INTO users (username, fullName, password, role, email) KEY(username) " +
                    "VALUES('user1','John Doe','user123','user','john@example.com')");

            // Tools table
            stmt.execute("CREATE TABLE IF NOT EXISTS tools (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "condition VARCHAR(50), " +
                    "status VARCHAR(50), " +
                    "userId BIGINT DEFAULT 0" +
                    ")");

            // Insert sample tools if they do not exist.
            stmt.execute("MERGE INTO tools (name, condition, status, userId) KEY(name) VALUES('Hammer','Good','Available',0)");
            stmt.execute("MERGE INTO tools (name, condition, status, userId) KEY(name) VALUES('Screwdriver','Fair','Available',0)");
            stmt.execute("MERGE INTO tools (name, condition, status, userId) KEY(name) VALUES('Wrench','Good','Available',0)");


            System.out.println("Database initialized successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}