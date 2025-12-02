package com.toolcheck;

import com.toolcheck.controller.*;
import com.toolcheck.dao.*;
import com.toolcheck.model.*;

import java.util.List;
import java.util.Scanner;

 // managing users, tools, checkouts/returns, and reports.
 //Supports admin and normal user roles, with password reset for all users.
public class Main {

    // Scanner for reading user input
    private static final Scanner sc = new Scanner(System.in);

    // DAO interfaces for database operations
    private static final UserDAOInterface userDAO = new UserDAO();
    private static final ToolDAOInterface toolDAO = new ToolDAO();
    private static final CheckoutRecordDAOInterface recordDAO = new CheckoutRecordDAO();
    private static final ReportDAOInterface reportDAO = new ReportDAO();

    // Controller interfaces for handling business logic
    private static final UserControllerInterface userCtrl = new UserController(userDAO);
    private static final ToolControllerInterface toolCtrl = new ToolController(toolDAO);
    private static final CheckoutRecordControllerInterface checkoutCtrl = new CheckoutRecordController(toolDAO, recordDAO);
    private static final ReportControllerInterface reportCtrl = new ReportController(reportDAO);

    // Currently logged-in user
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("Welcome to ToolCheckSystem!");

        // Outer Loop: allows returning to login page after Logout
        while (true) {
            // Show login menu until someone logs in
            showLoginMenu();

            // Main menu for logged-in user
            boolean logout = false;
            while (!logout) {
                logout = showMainMenu();
            }

            // Reset current user after Logout
            currentUser = null;
        }
    }

    // ---------------- LOGIN ----------------
     // Display the login menu until a user successfully logs in.
    private static void showLoginMenu() {
        while (currentUser == null) {
            System.out.println("\n--- Login ---");
            System.out.println("1. Login");
            System.out.println("2. Forgot Admin Password / First Admin Setup");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> loginUser();
                case "2" -> resetOrCreateAdmin();
                default -> System.out.println("Invalid option.");
            }
        }
    }


    // Handles user login
    private static void loginUser() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        currentUser = userCtrl.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, "
                    + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        } else {
            System.out.println("Invalid username or password. Try again.");
        }
    }

    // Reset admin password if admin exists.
    private static void resetOrCreateAdmin() {
        List<User> users = userCtrl.getAllUsers();
        boolean adminExists = users.stream().anyMatch(u -> "admin".equalsIgnoreCase(u.getRole()));

        if (!adminExists) {
            System.out.println("No admin found. Please create the first admin account.");
            addAdminUser();
            return;
        }

        System.out.println("Reset Admin Password");
        System.out.print("Enter Admin username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter Admin email: ");
        String email = sc.nextLine().trim();

        // Find admin user by username and email
        User admin = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getEmail().equalsIgnoreCase(email) && "admin".equalsIgnoreCase(u.getRole()))
                .findFirst().orElse(null);

        if (admin == null) {
            System.out.println("Admin not found with provided information.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine().trim();
        admin.setPassword(newPassword);

        if (userCtrl.updateUser(admin)) System.out.println("Admin password updated successfully!");
        else System.out.println("Failed to update password.");
    }

    // Adds a new admin user.
     // Used for first admin setup when no admin exists.
    private static void addAdminUser() {
        System.out.print("Username: "); String username = sc.nextLine().trim();
        System.out.print("Password: "); String password = sc.nextLine().trim();
        System.out.print("Full Name: "); String fullName = sc.nextLine().trim();
        System.out.print("Email: "); String email = sc.nextLine().trim();

        User admin = new User(username, password, "admin", fullName, email);
        if (userCtrl.createUser(admin)) {
            System.out.println("First admin account created successfully! ID=" + admin.getId());
            currentUser = admin;
        } else {
            System.out.println("Failed to create admin account.");
        }
    }

    // Checks if the current user is an admin.
    private static boolean isAdmin() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    // ---------------- MAIN MENU ----------------
     // Admins see user management, tool management, checkout/return, and reports.
     // Normal users see tool management, checkout/return, and reports.
    private static boolean showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        if (isAdmin()) System.out.println("1. Manage Users");
        System.out.println((isAdmin() ? "2" : "1") + ". Manage Tools");
        System.out.println((isAdmin() ? "3" : "2") + ". Checkout / Return Tools");
        System.out.println((isAdmin() ? "4" : "3") + ". Manage Reports");
        System.out.println("0. Logout");
        System.out.print("Choose an option: ");

        String input = sc.nextLine().trim();
        if (input.isEmpty()) return false;

        switch (input) {
            case "1" -> { if (isAdmin()) userManagement(); else toolManagement(); }
            case "2" -> { if (isAdmin()) toolManagement(); else checkoutMenu(); }
            case "3" -> { if (isAdmin()) checkoutMenu(); else reportMenu(); }
            case "4" -> { if (isAdmin()) reportMenu(); else System.out.println("Invalid option!"); }
            case "0" -> {
                System.out.println("Logging out...");
                return true; // triggers logout and returns to login
            }
            default -> System.out.println("Invalid option.");
        }
        return false;
    }

    // -------------------- USER MANAGEMENT --------------------
    private static void userManagement() {
        System.out.println("\n--- User Management ---");
        System.out.println("1. Add User");
        System.out.println("2. Update User");
        System.out.println("3. Delete User");
        System.out.println("4. List All Users");
        System.out.print("Select option: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        switch (input) {
            case "1" -> addUser();
            case "2" -> updateUser();
            case "3" -> deleteUser();
            case "4" -> listAllUsers();
            default -> System.out.println("Invalid option.");
        }
    }

    // Add a user
    private static void addUser() {
        System.out.print("Username: "); String username = sc.nextLine();
        System.out.print("Password: "); String password = sc.nextLine();
        System.out.print("Role (admin/user): "); String role = sc.nextLine();
        System.out.print("Full Name: "); String fullName = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();

        User u = new User(username, password, role, fullName, email);
        if (userCtrl.createUser(u)) System.out.println("User added successfully! ID=" + u.getId());
        else System.out.println("Failed to add user.");
    }

    // Update existing user
    private static void updateUser() {
        listAllUsers();
        System.out.print("Enter user ID to update: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        User user = userCtrl.getUserById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("New username (" + user.getUsername() + "): ");
        String username = sc.nextLine();
        if (!username.isEmpty()) {
            user.setUsername(username);
        }
        System.out.print("New password: ");
        String password = sc.nextLine();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        System.out.print("New role (" + user.getRole() + "): ");
        String role = sc.nextLine();
        if (!role.isEmpty()) {
            user.setRole(role);
        }
        System.out.print("New full name (" + user.getFullName() + "): ");
        String fullName = sc.nextLine();
        if (!fullName.isEmpty()) {
            user.setFullName(fullName);
        }
        System.out.print("New email (" + user.getEmail() + "): ");
        String email = sc.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        if (userCtrl.updateUser(user)) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    // delete existing user
    private static void deleteUser() {
        listAllUsers();
        System.out.print("Enter user ID to delete: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) {
            return;
        }

        if (userCtrl.deleteUser(id)) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    // Get all users
    private static void listAllUsers() {
        List<User> users = userCtrl.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.println("--- All Users ---");
        for (User u : users) {
            System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getRole());
        }
    }

    // ---------------- TOOL MANAGEMENT ----------------
    private static void toolManagement() {
        System.out.println("\n--- Tool Management ---");
        System.out.println("1. Add Tool");
        System.out.println("2. Update Tool");
        System.out.println("3. Delete Tool");
        System.out.println("4. List All Tools");
        System.out.print("Select option: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        switch (input) {
            case "1" -> addTool();
            case "2" -> updateTool();
            case "3" -> deleteTool();
            case "4" -> listAllTools();
            default -> System.out.println("Invalid option.");
        }
    }

    // Add a tool
    private static void addTool() {
        System.out.print("Tool Name: "); String name = sc.nextLine();
        System.out.print("Description: "); String desc = sc.nextLine();
        System.out.print("Category: "); String cat = sc.nextLine();
        System.out.print("Condition (excellent/good/fair/poor): "); String cond = sc.nextLine();
        System.out.print("Status (optional, default 'available'): "); String status = sc.nextLine();
        System.out.print("Location: "); String loc = sc.nextLine();

        Tool tool = new Tool(name, desc, cat, cond, status, loc);
        if (toolCtrl.addTool(tool)) {
            System.out.println("Tool added successfully! ID=" + tool.getId());
        } else {
            System.out.println("Failed to add tool.");
        }
    }

    // Update existing tool
    private static void updateTool() {
        listAllTools();
        System.out.print("Enter tool ID to update: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        Tool tool = toolCtrl.getToolById(id);
        if (tool == null) { System.out.println("Tool not found."); return; }

        System.out.print("New name (" + tool.getName() + "): "); String name = sc.nextLine(); if(!name.isEmpty()) tool.setName(name);
        System.out.print("New description (" + tool.getDescription() + "): "); String desc = sc.nextLine(); if(!desc.isEmpty()) tool.setDescription(desc);
        System.out.print("New category (" + tool.getCategory() + "): "); String cat = sc.nextLine(); if(!cat.isEmpty()) tool.setCategory(cat);
        System.out.print("New condition (" + tool.getToolCondition() + "): "); String cond = sc.nextLine(); if(!cond.isEmpty()) tool.setToolCondition(cond);
        System.out.print("New status (" + tool.getStatus() + "): "); String status = sc.nextLine(); if(!status.isEmpty()) tool.setStatus(status);
        System.out.print("New location (" + tool.getLocation() + "): "); String loc = sc.nextLine(); if(!loc.isEmpty()) tool.setLocation(loc);

        if (toolCtrl.updateTool(tool)) {
            System.out.println("Tool updated successfully!");
        } else {
            System.out.println("Failed to update tool.");
        }
    }

    // Delete a tool
    private static void deleteTool() {
        listAllTools();
        System.out.print("Enter tool ID to delete: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        if (toolCtrl.deleteTool(id)) {
            System.out.println("Tool deleted successfully!");
        }
        else {
            System.out.println("Failed to delete tool.");
        }
    }

    // Get all tools
    private static void listAllTools() {
        List<Tool> tools = toolCtrl.getAllTools();
        if (tools.isEmpty()) { System.out.println("No tools found."); return; }
        System.out.println("--- All Tools ---");
        for (Tool t : tools) {
            System.out.println(t.getId() + " | " + t.getName() + " | " + t.getToolCondition() + " | " + t.getStatus());
        }
    }


    // ---------------- CHECKOUT / RETURN ----------------
    private static void checkoutMenu() {
        System.out.println("\n--- Checkout / Return Tools ---");
        System.out.println("1. Checkout Tool");
        System.out.println("2. Return Tool");
        System.out.println("3. List Checkout Records");
        System.out.print("Select option: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        switch (input) {
            case "1" -> checkoutTool();
            case "2" -> returnTool();
            case "3" -> listAllCheckoutRecords();
            default -> System.out.println("Invalid option.");
        }
    }

    // Users check out tools
    private static void checkoutTool() {
        long userId;
        if (isAdmin()) {
            listAllUsers();
            System.out.print("Enter User ID to checkout tool for: ");
            userId = parseLongInput(sc.nextLine());
            if (userId == -1 || userCtrl.getUserById(userId) == null) {
                System.out.println("Invalid user ID.");
                return; }
        } else {
            userId = currentUser.getId();
        }

        List<Tool> tools = toolCtrl.getAllTools();
        boolean anyAvailable = false;
        System.out.println("--- Available Tools ---");
        for (Tool t : tools) {
            if ("available".equalsIgnoreCase(t.getStatus())) {
                System.out.println(t.getId() + " | " + t.getName() + " | " + t.getToolCondition());
                anyAvailable = true;
            }
        }
        if (!anyAvailable) {
            System.out.println("No tools available.");
            return;
        }

        System.out.print("Enter Tool ID to checkout: ");
        long toolId = parseLongInput(sc.nextLine());
        if (toolId == -1) return;

        if (checkoutCtrl.checkoutTool(userId, toolId)) {
            System.out.println("Tool checked out successfully!");
        } else {
            System.out.println("Failed to checkout tool.");
        }
    }

    // Users return tools
    private static void returnTool() {
        listAllCheckoutRecords();
        System.out.print("Enter Checkout Record ID to return: ");
        long recId = parseLongInput(sc.nextLine());
        if (recId == -1) {
            return;
        }

        System.out.print("Condition after return (excellent/good/fair/poor): ");
        String condAfter = sc.nextLine();

        if (checkoutCtrl.returnTool(recId, condAfter)) {
            System.out.println("Tool returned successfully!");
        } else{
            System.out.println("Failed to return tool.");
        }
    }

    // Get all check out records
    private static void listAllCheckoutRecords() {
        List<CheckoutRecord> records = checkoutCtrl.listAllRecords();
        if (records.isEmpty()) {
            System.out.println("No checkout records.");
            return;
        }

        System.out.println("--- Checkout Records ---");
        for (CheckoutRecord r : records) {
            if (!isAdmin() && r.getUserId() != currentUser.getId()) {
                continue;
            }

            // Fetch the related User and Tool objects
            User user = userCtrl.getUserById(r.getUserId());
            Tool tool = toolCtrl.getToolById(r.getToolId());

            String fullName = (user != null) ? user.getFullName() : "Unknown User";
            String toolName = (tool != null) ? tool.getName() : "Unknown Tool";

            System.out.println(r.getId() + " | UserID:" + r.getUserId()  + " (" + fullName
                    + ")" +  " | ToolID:" + r.getToolId() + " (" + toolName  + ")" + " | Status:" + r.getStatus());
        }
    }

    // ---------------- REPORTS ----------------
    private static void reportMenu() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Add Report");
        if (isAdmin()) System.out.println("2. Resolve Report");
        System.out.println((isAdmin() ? "3" : "2") + ". List Reports");
        System.out.print("Select option: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            return;
        }

        switch (input) {
            case "1" -> addReport();
            case "2" -> { if (isAdmin()) resolveReport(); else listAllReports(); }
            case "3" -> { if (isAdmin()) listAllReports(); else System.out.println("Invalid option."); }
            default -> System.out.println("Invalid option.");
        }
    }

    // Add a report
    private static void addReport() {
        long userId = isAdmin() ? selectUserForReport() : currentUser.getId();
        if (userId == -1) return;

        long toolId = selectToolForReport();
        if (toolId == -1) return;

        System.out.print("Report Type (damage/maintenance/lost): ");
        String type = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();

        Report report = new Report(userId, toolId, type, desc);
        if (reportCtrl.addReport(report)) System.out.println("Report added successfully! ID=" + report.getId());
        else System.out.println("Failed to add report.");
    }

    // Displays a list of all users and asks for a user ID input.
     // Validates that the selected user exists.
    private static long selectUserForReport() {
        // List all users for reference
        listAllUsers();
        // Ask admin to input the user ID for whom the report is being created
        System.out.print("Enter User ID for the report: ");
        long userId = parseLongInput(sc.nextLine());
        // Validate user exists.
        if (userCtrl.getUserById(userId) == null) {
            System.out.println("Invalid user ID."); return -1;
        } else {
            return userId;
        }


    }

    // Displays all tools and asks for a Tool ID input.
     // Validates that the selected tool exists.
    private static long selectToolForReport() {
        // List all tools for reference
        listAllTools();
        // Ask user to input the tool ID for the report
        System.out.print("Enter Tool ID for the report: ");
        long toolId = parseLongInput(sc.nextLine());

        // Validate tool exists
        if (toolCtrl.getToolById(toolId) == null) {
            System.out.println("Invalid tool ID.");
            return -1;
        } else {
            return toolId;
        }

    }

    // Display all reports and prompts for a report ID.
     // Updates the report status to resolved.
    private static void resolveReport() {
        // Show all reports for reference
        listAllReports();
        // Ask for Report ID to resolve.
        System.out.print("Enter Report ID to resolve: ");
        long reportId = parseLongInput(sc.nextLine());
        if (reportId == -1) {
            return; // exit if invalid input
        }

        // Attempt to resolve report and show success/failure message
        if (reportCtrl.resolveReport(reportId)) {
            System.out.println("Report resolved!");
        } else {
            System.out.println("Failed to resolve report.");
        }
    }

    // Admin sees all reports.
     // Normal users see only their own reports.
    private static void listAllReports() {
        List<Report> reports = reportCtrl.getAllReports();
        if (reports.isEmpty()) {
            System.out.println("No reports found.");
            return;
        } else {
            System.out.println("--- Reports ---");
        }

        for (Report r : reports) {
            // Normal users only see their own reports
            if (!isAdmin() && r.getUserId() != currentUser.getId()) {
                continue;
            } else {
                // Display report details
                System.out.println(r.getId() + " | UserID:" + r.getUserId()
                        + " | ToolID:" + r.getToolId() + " | Type:" + r.getReportType()
                        + " | Status:" + r.getStatus());
            }

        }
    }

    // Safely parses a string into a long value
    private static long parseLongInput(String input) {
        try { return Long.parseLong(input); }
        catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return -1;
        }
    }
}