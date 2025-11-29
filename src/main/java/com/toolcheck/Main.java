package com.toolcheck;

import com.toolcheck.controller.*;
import com.toolcheck.dao.*;
import com.toolcheck.model.*;

import java.util.List;
import java.util.Scanner;

// It provides menus to manage users, tools, checkouts/returns, and reports.
public class Main {

    // Scanner for reading user input from console
    private static final Scanner sc = new Scanner(System.in);

    // DAO instances
    private static final UserDAOInterface userDAO = new UserDAO();
    private static final ToolDAOInterface toolDAO = new ToolDAO();
    private static final CheckoutRecordDAOInterface recordDAO = new CheckoutRecordDAO();
    private static final ReportDAOInterface reportDAO = new ReportDAO();

    // Controller instances
    private static final UserControllerInterface userCtrl = new UserController(userDAO);
    private static final ToolControllerInterface toolCtrl = new ToolController(toolDAO);
    private static final CheckoutRecordControllerInterface checkoutCtrl = new CheckoutRecordController(toolDAO, recordDAO);
    private static final ReportControllerInterface reportCtrl = new ReportController(reportDAO);

    public static void main(String[] args) {
        System.out.println("Welcome to ToolCheckSystem!");

        // Main loop of the program
        while (true) {
            showMainMenu();
            String input = sc.nextLine().trim();    // Read user input
            if (input.isEmpty()) continue;          // Ignore empty input

            // Main menu options
            switch (input) {
                case "1" -> userManagement();       // Manage users
                case "2" -> toolManagement();       // Manage tools
                case "3" -> checkoutMenu();         // Checkout/Return tools
                case "4" -> reportMenu();           // Manage reports
                case "0" -> {                       // Exit program
                    System.out.println("Goodbye.");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // -------------------- MAIN MENU --------------------
    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Tools");
        System.out.println("3. Checkout / Return Tools");
        System.out.println("4. Manage Reports");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // -------------------- USER MANAGEMENT --------------------
    // Handles adding, updating, deleting, and listing users
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

    // Add a new user
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
        if (user == null) { System.out.println("User not found."); return; }

        // Prompt for new values, leave unchanged if empty
        System.out.print("New username (" + user.getUsername() + "): "); String username = sc.nextLine(); if (!username.isEmpty()) user.setUsername(username);
        System.out.print("New password: "); String password = sc.nextLine(); if (!password.isEmpty()) user.setPassword(password);
        System.out.print("New role (" + user.getRole() + "): "); String role = sc.nextLine(); if (!role.isEmpty()) user.setRole(role);
        System.out.print("New full name (" + user.getFullName() + "): "); String fullName = sc.nextLine(); if (!fullName.isEmpty()) user.setFullName(fullName);
        System.out.print("New email (" + user.getEmail() + "): "); String email = sc.nextLine(); if (!email.isEmpty()) user.setEmail(email);

        if (userCtrl.updateUser(user)) System.out.println("User updated successfully!");
        else System.out.println("Failed to update user.");
    }

    // Delete user
    private static void deleteUser() {
        listAllUsers();
        System.out.print("Enter user ID to delete: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        if (userCtrl.deleteUser(id)) System.out.println("User deleted successfully!");
        else System.out.println("Failed to delete user.");
    }

    // List all users
    private static void listAllUsers() {
        List<User> users = userCtrl.getAllUsers();
        if (users.isEmpty()) { System.out.println("No users found."); return; }
        System.out.println("--- All Users ---");
        for (User u : users) System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getRole());
    }

    // -------------------- TOOL MANAGEMENT --------------------
    // Handles adding, updating, deleting, and listing tools
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

    // Add a new tool
    private static void addTool() {
        System.out.print("Tool Name: "); String name = sc.nextLine();
        System.out.print("Description: "); String desc = sc.nextLine();
        System.out.print("Category: "); String cat = sc.nextLine();
        System.out.print("Condition (excellent/good/fair/poor): "); String cond = sc.nextLine();
        System.out.print("Status (optional, default 'available'): "); String status = sc.nextLine();
        System.out.print("Location: "); String loc = sc.nextLine();

        Tool tool = new Tool(name, desc, cat, cond, status, loc);
        if (toolCtrl.addTool(tool)) System.out.println("Tool added successfully! ID=" + tool.getId());
        else System.out.println("Failed to add tool.");
    }

    // Update existing tool
    private static void updateTool() {
        listAllTools();
        System.out.print("Enter tool ID to update: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        Tool tool = toolCtrl.getToolById(id);
        if (tool == null) { System.out.println("Tool not found."); return; }

        // Prompt for new values, leave unchanged if empty
        System.out.print("New name (" + tool.getName() + "): "); String name = sc.nextLine(); if(!name.isEmpty()) tool.setName(name);
        System.out.print("New description (" + tool.getDescription() + "): "); String desc = sc.nextLine(); if(!desc.isEmpty()) tool.setDescription(desc);
        System.out.print("New category (" + tool.getCategory() + "): "); String cat = sc.nextLine(); if(!cat.isEmpty()) tool.setCategory(cat);
        System.out.print("New condition (" + tool.getToolCondition() + "): "); String cond = sc.nextLine(); if(!cond.isEmpty()) tool.setToolCondition(cond);
        System.out.print("New status (" + tool.getStatus() + "): "); String status = sc.nextLine(); if(!status.isEmpty()) tool.setStatus(status);
        System.out.print("New location (" + tool.getLocation() + "): "); String loc = sc.nextLine(); if(!loc.isEmpty()) tool.setLocation(loc);

        if (toolCtrl.updateTool(tool)) System.out.println("Tool updated successfully!");
        else System.out.println("Failed to update tool.");
    }

    // Delete tool
    private static void deleteTool() {
        listAllTools();
        System.out.print("Enter tool ID to delete: ");
        long id = parseLongInput(sc.nextLine());
        if (id == -1) return;

        if (toolCtrl.deleteTool(id)) System.out.println("Tool deleted successfully!");
        else System.out.println("Failed to delete tool.");
    }

    // List all tools
    private static void listAllTools() {
        List<Tool> tools = toolCtrl.getAllTools();
        if (tools.isEmpty()) { System.out.println("No tools found."); return; }
        System.out.println("--- All Tools ---");
        for (Tool t : tools) System.out.println(t.getId() + " | " + t.getName() + " | " + t.getToolCondition() + " | " + t.getStatus());
    }

    // -------------------- CHECKOUT / RETURN --------------------
    // Handles checking out and returning tools
    private static void checkoutMenu() {
        System.out.println("\n1. Checkout Tool");
        System.out.println("2. Return Tool");
        System.out.println("3. List All Checkout Records");
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

    // Checkout a tool for a user
    private static void checkoutTool() {
        listAllUsers();
        System.out.print("Enter User ID: ");
        long userId = parseLongInput(sc.nextLine());
        if (userId == -1 || userCtrl.getUserById(userId) == null) {
            System.out.println("Invalid user ID.");
            return;
        }

        // Show available tools only
        List<Tool> tools = toolCtrl.getAllTools();
        System.out.println("--- Available Tools ---");
        boolean anyAvailable = false;
        for (Tool t : tools) {
            if ("available".equalsIgnoreCase(t.getStatus())) {
                System.out.println(t.getId() + " | " + t.getName() + " | " + t.getToolCondition());
                anyAvailable = true;
            }
        }
        if (!anyAvailable) {
            System.out.println("No tools available for checkout.");
            return;
        }

        System.out.print("Enter Tool ID to checkout: ");
        long toolId = parseLongInput(sc.nextLine());
        if (toolId == -1) return;

        if (checkoutCtrl.checkoutTool(userId, toolId))
            System.out.println("Tool checked out successfully!");
        else
            System.out.println("Failed to checkout tool.");
    }

    // Return a tool
    private static void returnTool() {
        listAllCheckoutRecords();
        System.out.print("Enter Checkout Record ID to return: ");
        long recId = parseLongInput(sc.nextLine());
        if (recId == -1) return;

        System.out.print("Condition after return (excellent/good/fair/poor): ");
        String condAfter = sc.nextLine();

        if (checkoutCtrl.returnTool(recId, condAfter))
            System.out.println("Tool returned successfully!");
        else
            System.out.println("Failed to return tool.");
    }

    // List all checkout records
    private static void listAllCheckoutRecords() {
        List<CheckoutRecord> records = checkoutCtrl.listAllRecords();
        if (records.isEmpty()) {
            System.out.println("No checkout records.");
            return;
        }

        System.out.println("--- Checkout Records ---");
        for (CheckoutRecord r : records) {
            System.out.println(r.getId() + " | UserID:" + r.getUserId()
                    + " | ToolID:" + r.getToolId()
                    + " | Status:" + r.getStatus());
        }
    }

    // -------------------- REPORTS --------------------
    // Handles adding, resolving, and listing reports
    private static void reportMenu() {
        System.out.println("\n1. Add Report");
        System.out.println("2. Resolve Report");
        System.out.println("3. List All Reports");
        System.out.print("Select option: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        switch (input) {
            case "1" -> addReport();
            case "2" -> resolveReport();
            case "3" -> listAllReports();
            default -> System.out.println("Invalid option.");
        }
    }

    // Add a report
    private static void addReport() {
        listAllUsers();
        System.out.print("Enter User ID: ");
        long userId = parseLongInput(sc.nextLine());
        if (userId == -1 || userCtrl.getUserById(userId) == null) {
            System.out.println("Invalid user ID.");
            return;
        }

        listAllTools();
        System.out.print("Enter Tool ID: ");
        long toolId = parseLongInput(sc.nextLine());
        if (toolId == -1 || toolCtrl.getToolById(toolId) == null) {
            System.out.println("Invalid tool ID.");
            return;
        }

        System.out.print("Report Type (damage/maintenance/lost): ");
        String type = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();

        Report report = new Report(userId, toolId, type, desc);
        if (reportCtrl.addReport(report)) System.out.println("Report added successfully! ID=" + report.getId());
        else System.out.println("Failed to add report.");
    }

    // Resolve a report
    private static void resolveReport() {
        listAllReports();
        System.out.print("Enter Report ID to resolve: ");
        long reportId = parseLongInput(sc.nextLine());
        if (reportId == -1) return;

        if (reportCtrl.resolveReport(reportId)) System.out.println("Report resolved!");
        else System.out.println("Failed to resolve report.");
    }

    // List all reports
    private static void listAllReports() {
        List<Report> reports = reportCtrl.getAllReports();
        if (reports.isEmpty()) { System.out.println("No reports found."); return; }

        System.out.println("--- Reports ---");
        for (Report r : reports) System.out.println(r.getId() + " | UserID:" + r.getUserId() + " | ToolID:" + r.getToolId() + " | Status:" + r.getStatus());
    }

    // -------------------- UTILS --------------------
    // Parse a long number safely from string input
    private static long parseLongInput(String input) {
        try { return Long.parseLong(input.trim()); }
        catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return -1;
        }
    }
}