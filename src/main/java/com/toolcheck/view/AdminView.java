package com.toolcheck.view;

import com.toolcheck.controller.ToolControllerInterface;
import com.toolcheck.controller.UserControllerInterface;
import com.toolcheck.model.ToolInterface;
import com.toolcheck.model.UserInterface;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// Admin dashboard
public class AdminView extends View {

    // Controller for user-related operations
    private final UserControllerInterface userController;

    // Controller for tool-related operations
    private final ToolControllerInterface toolController;

    // Root layout container for the admin dashboard
    private VBox root;

    // Top buttons for switching screens
    private Button btnManageUsers, btnManageTools, btnCheckoutTools, btnReturnTools, btnLogout;

    public AdminView(Stage stage,
                     UserControllerInterface userController,
                     ToolControllerInterface toolController) {
        super(stage);
        this.userController = userController;
        this.toolController = toolController;
    }

    // Displays the admin dashboard with the top buttons.
    @Override
    public void show() {
        root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f8ff, #cfeaff);");

        initTopButtons();   // Initialize navigation buttons
        showDashboard();    // Display the dashboard layout

        Scene scene = new Scene(root, 980, 680);
        stage.setScene(scene);
        stage.setTitle("ToolCheck - Admin");
        stage.show();
    }

    // Initializes top menu buttons and styles.
    private void initTopButtons() {
        btnManageUsers = new Button("Manage Users");
        btnManageTools = new Button("Manage Tools");
        btnCheckoutTools = new Button("Checkout Tools");
        btnReturnTools = new Button("Return Tools");
        btnLogout = new Button("Logout");

        String style = "-fx-font-size: 13pt; -fx-padding: 8 12; -fx-background-color: #ffffffdd;";
        btnManageUsers.setStyle(style);
        btnManageTools.setStyle(style);
        btnCheckoutTools.setStyle(style);
        btnReturnTools.setStyle(style);
        btnLogout.setStyle(style);
    }

    // Display the main admin dashboard with navigation buttons
    private void showDashboard() {
        root.getChildren().clear();
        HBox bar = new HBox(10, btnManageUsers, btnManageTools, btnCheckoutTools, btnReturnTools, btnLogout);
        bar.setPadding(new Insets(8));
        root.getChildren().add(bar);

        //Button actions: navigate to respective views
        btnManageUsers.setOnAction(e -> showManageUsers());
        btnManageTools.setOnAction(e -> showManageTools());
        btnCheckoutTools.setOnAction(e -> showAdminCheckout());
        btnReturnTools.setOnAction(e -> showAdminReturn());
        btnLogout.setOnAction(e -> stage.close());
    }

    // displays the Manage Users screen for the admin
    private void showManageUsers() {
        // Keep only the top navigation bar
        root.getChildren().removeIf(n -> n != null && n != root.getChildren().get(0));

        // create table for users
        TableView<UserInterface> table = new TableView<>();

        // Column: User ID
        TableColumn<UserInterface, Long> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject());

        // Column: User username
        TableColumn<UserInterface, String> unameC = new TableColumn<>("Username");
        unameC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getUsername()));

        // Column: User full name
        TableColumn<UserInterface, String> fullC = new TableColumn<>("Full Name");
        fullC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getFullName()));

        // Column: user Email
        TableColumn<UserInterface, String> emailC = new TableColumn<>("Email");
        emailC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getEmail()));

        // Column: user role
        TableColumn<UserInterface, String> roleC = new TableColumn<>("Role");
        roleC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getRole()));

        // Add columns to table
        table.getColumns().addAll(idC, unameC, fullC, emailC, roleC);
        table.setItems(FXCollections.observableArrayList(userController.getAllUsers()));

        // Action buttons
        Button add = new Button("Add User");
        Button edit = new Button("Edit Selected");
        Button del = new Button("Delete Selected");
        Button back = new Button("Back");
        HBox actions = new HBox(8, add, edit, del, back);
        actions.setPadding(new Insets(8));

        // Layout for action buttons
        root.getChildren().addAll(new Label("Manage Users"), table, actions);

        // Go back to dashboard
        back.setOnAction(e -> showDashboard());

        // Add user dialog
        add.setOnAction(e -> {
            Dialog<UserInterface> dlg = new Dialog<>();
            dlg.setTitle("Add User");

            // Form layout inside dialog
            GridPane gp = new GridPane();
            gp.setHgap(8); gp.setVgap(8); gp.setPadding(new Insets(10));
            TextField tfUsername = new TextField();
            TextField tfFull = new TextField();
            TextField tfEmail = new TextField();
            PasswordField pf = new PasswordField();
            ChoiceBox<String> role = new ChoiceBox<>(FXCollections.observableArrayList("user", "admin"));
            role.setValue("user");

            // User status drop-down
            gp.add(new Label("Username:"), 0, 0); gp.add(tfUsername, 1, 0);
            gp.add(new Label("Password:"), 0, 1); gp.add(pf, 1, 1);
            gp.add(new Label("Full name:"), 0, 2); gp.add(tfFull, 1, 2);
            gp.add(new Label("Email:"), 0, 3); gp.add(tfEmail, 1, 3);
            gp.add(new Label("Role:"), 0, 4); gp.add(role, 1, 4);

            dlg.getDialogPane().setContent(gp);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Process dialog result
            dlg.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    UserInterface u = new com.toolcheck.model.User();
                    u.setUsername(tfUsername.getText().trim());
                    u.setPassword(pf.getText());
                    u.setFullName(tfFull.getText().trim());
                    u.setEmail(tfEmail.getText().trim());
                    u.setRole(role.getValue());
                    return u;
                }
                return null;
            });

            dlg.showAndWait().ifPresent(u -> {
                userController.addUser(u);
                table.setItems(FXCollections.observableArrayList(userController.getAllUsers()));
            });
        });


        // Edit user dialog
        edit.setOnAction(e -> {
            UserInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Edit User", "Select a user to edit."); return; }

            Dialog<UserInterface> dlg = new Dialog<>();
            dlg.setTitle("Edit User");
            GridPane gp = new GridPane();
            gp.setHgap(8); gp.setVgap(8); gp.setPadding(new Insets(10));
            TextField tfUsername = new TextField(sel.getUsername());
            TextField tfFull = new TextField(sel.getFullName());
            TextField tfEmail = new TextField(sel.getEmail());
            PasswordField pf = new PasswordField();
            pf.setPromptText("leave blank to keep existing password");
            ChoiceBox<String> role = new ChoiceBox<>(FXCollections.observableArrayList("user", "admin"));
            role.setValue(sel.getRole() == null ? "user" : sel.getRole());

            gp.add(new Label("Username:"), 0, 0); gp.add(tfUsername, 1, 0);
            gp.add(new Label("New Password:"), 0, 1); gp.add(pf, 1, 1);
            gp.add(new Label("Full name:"), 0, 2); gp.add(tfFull, 1, 2);
            gp.add(new Label("Email:"), 0, 3); gp.add(tfEmail, 1, 3);
            gp.add(new Label("Role:"), 0, 4); gp.add(role, 1, 4);

            dlg.getDialogPane().setContent(gp);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dlg.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    sel.setUsername(tfUsername.getText().trim());
                    if (!pf.getText().isBlank()) sel.setPassword(pf.getText());
                    sel.setFullName(tfFull.getText().trim());
                    sel.setEmail(tfEmail.getText().trim());
                    sel.setRole(role.getValue());
                    return sel;
                }
                return null;
            });

            dlg.showAndWait().ifPresent(u -> {
                userController.updateUser(u);
                table.setItems(FXCollections.observableArrayList(userController.getAllUsers()));
            });
        });

        // Delete user
        del.setOnAction(e -> {
            UserInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Delete User", "Select a user to delete."); return; }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete user " + sel.getUsername() + "?", ButtonType.YES, ButtonType.NO);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    userController.deleteUser(sel.getId());
                    table.setItems(FXCollections.observableArrayList(userController.getAllUsers()));
                }
            });
        });
    }

    // Displays the Manage Tools screen for the admin
    private void showManageTools() {

        // Keep only the top navigation bar
        root.getChildren().removeIf(n -> n != null && n != root.getChildren().get(0));

        // set up table for tools
        TableView<ToolInterface> table = new TableView<>();

        // Column: Tool ID
        TableColumn<ToolInterface, Long> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(
                p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject()
        );

        // Column: Tool Name
        TableColumn<ToolInterface, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(
                p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName())
        );

        // Column: Condition (e.g., Good, Fair, Broken)
        TableColumn<ToolInterface, String> condC = new TableColumn<>("Condition");
        condC.setCellValueFactory(
                p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getCondition())
        );

        // Column: Status (Available, Checked Out, Maintenance)
        TableColumn<ToolInterface, String> statusC = new TableColumn<>("Status");
        statusC.setCellValueFactory(
                p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getStatus())
        );

        // Column: User ID (0 = nobody has it)
        TableColumn<ToolInterface, Long> uidC = new TableColumn<>("User ID");
        uidC.setCellValueFactory(
                p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getUserId()).asObject()
        );


        // Add columns to table
        table.getColumns().addAll(idC, nameC, condC, statusC, uidC);
        // Load all tools from database into table
        table.setItems(FXCollections.observableArrayList(toolController.getAllTools()));

        // Action buttons
        Button add = new Button("Add Tool");
        Button edit = new Button("Edit Selected");
        Button del = new Button("Delete Selected");
        Button back = new Button("Back");

        // Layout for action buttons
        HBox actions = new HBox(8, add, edit, del, back);
        actions.setPadding(new Insets(8));

        // Add UI components into root layout
        root.getChildren().addAll(new Label("Manage Tools"), table, actions);

        // Back button returns to dashboard
        back.setOnAction(e -> showDashboard());

        // Add tool logic
        add.setOnAction(e -> {
            Dialog<ToolInterface> dlg = new Dialog<>();
            dlg.setTitle("Add Tool");

            // Form layout inisde dialog
            GridPane gp = new GridPane(); gp.setHgap(8); gp.setVgap(8); gp.setPadding(new Insets(10));
            TextField tfName = new TextField();
            TextField tfCond = new TextField();

            // Tool status drop-down
            ChoiceBox<String> status = new ChoiceBox<>(FXCollections.observableArrayList("Available", "Checked Out", "Maintenance"));
            status.setValue("Available");
            gp.add(new Label("Name:"), 0, 0); gp.add(tfName, 1, 0);
            gp.add(new Label("Condition:"), 0, 1); gp.add(tfCond, 1, 1);
            gp.add(new Label("Status:"), 0, 2); gp.add(status, 1, 2);
            dlg.getDialogPane().setContent(gp);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            // Process dialog result
            dlg.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    ToolInterface t = new com.toolcheck.model.Tool();
                    t.setName(tfName.getText().trim());
                    t.setCondition(tfCond.getText().trim());
                    t.setStatus(status.getValue());
                    t.setUserId(0);
                    return t;
                }
                return null;
            });
            dlg.showAndWait().ifPresent(t -> {
                toolController.addTool(t);
                table.setItems(FXCollections.observableArrayList(toolController.getAllTools()));
            });
        });

        // Edit tool logic
        edit.setOnAction(e -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Edit Tool", "Select a tool to edit."); return; }
            Dialog<ToolInterface> dlg = new Dialog<>();
            dlg.setTitle("Edit Tool");
            GridPane gp = new GridPane(); gp.setHgap(8); gp.setVgap(8); gp.setPadding(new Insets(10));
            TextField tfName = new TextField(sel.getName());
            TextField tfCond = new TextField(sel.getCondition());
            ChoiceBox<String> status = new ChoiceBox<>(FXCollections.observableArrayList("Available", "Checked Out", "Maintenance"));
            status.setValue(sel.getStatus());
            gp.add(new Label("Name:"), 0, 0); gp.add(tfName, 1, 0);
            gp.add(new Label("Condition:"), 0, 1); gp.add(tfCond, 1, 1);
            gp.add(new Label("Status:"), 0, 2); gp.add(status, 1, 2);
            dlg.getDialogPane().setContent(gp);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    sel.setName(tfName.getText().trim());
                    sel.setCondition(tfCond.getText().trim());
                    sel.setStatus(status.getValue());
                    return sel;
                }
                return null;
            });
            dlg.showAndWait().ifPresent(t -> {
                toolController.updateTool(t);
                table.setItems(FXCollections.observableArrayList(toolController.getAllTools()));
            });
        });


        // Delete tool logic
        del.setOnAction(e -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Delete Tool", "Select a tool to delete."); return; }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete tool " + sel.getName() + "?", ButtonType.YES, ButtonType.NO);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    toolController.deleteTool(sel.getId());
                    table.setItems(FXCollections.observableArrayList(toolController.getAllTools()));
                }
            });
        });
    }

    // Displays the Admin Checkout view
    private void showAdminCheckout() {
        // keep only the top navigation bar
        root.getChildren().removeIf(n -> n != null && n != root.getChildren().get(0));

        // setup TableView
        TableView<ToolInterface> table = new TableView<>();

        // column: tool ID
        TableColumn<ToolInterface, Long> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject());

        // column: tool name
        TableColumn<ToolInterface, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName()));

        // Column: tool status
        TableColumn<ToolInterface, String> statusC = new TableColumn<>("Status");
        statusC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getStatus()));

        // Add columns to table
        table.getColumns().addAll(idC, nameC, statusC);
        // show only tools that are available
        table.setItems(FXCollections.observableArrayList(toolController.getAllTools().stream()
                .filter(t -> "Available".equalsIgnoreCase(t.getStatus()) || t.getUserId() == 0).toList()));

        TextField tfUserId = new TextField(); tfUserId.setPromptText("User ID to checkout to");

        // Action buttons
        Button checkout = new Button("Checkout");
        Button back = new Button("Back");
        HBox actions = new HBox(8, tfUserId, checkout, back);
        actions.setPadding(new Insets(8));
        root.getChildren().addAll(new Label("Admin Checkout Tools"), table, actions);

        back.setOnAction(e -> showDashboard());

        checkout.setOnAction(e -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Checkout", "Select a tool."); return; }
            try {
                long uid = Long.parseLong(tfUserId.getText().trim());
                boolean ok = toolController.checkoutTool(sel.getId(), uid);
                if (ok) {
                    showAlert("Checkout", "Checked out successfully.");
                    table.setItems(FXCollections.observableArrayList(toolController.getAllTools().stream()
                            .filter(t -> "Available".equalsIgnoreCase(t.getStatus()) || t.getUserId() == 0).toList()));
                } else showAlert("Checkout", "Failed to checkout.");
            } catch (NumberFormatException ex) {
                showAlert("Checkout", "Invalid User ID.");
            }
        });
    }

    // Admin Return Screen
    private void showAdminReturn() {
        root.getChildren().removeIf(n -> n != null && n != root.getChildren().get(0));

        // Setup table view
        TableView<ToolInterface> table = new TableView<>();

        // Column: tool ID
        TableColumn<ToolInterface, Long> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject());

        // Column: tool name
        TableColumn<ToolInterface, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName()));

        // Column: user ID
        TableColumn<ToolInterface, Long> uidC = new TableColumn<>("User ID");
        uidC.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getUserId()).asObject());

        // Add all columns to table
        table.getColumns().addAll(idC, nameC, uidC);

        // Only show tools that are currently checked out
        table.setItems(FXCollections.observableArrayList(toolController.getAllTools().stream()
                .filter(t -> t.getUserId() != 0).toList()));

        TextField tfCondition = new TextField(); tfCondition.setPromptText("Condition after return");
        Button btnReturn = new Button("Return");
        Button back = new Button("Back");
        HBox actions = new HBox(8, tfCondition, btnReturn, back);
        actions.setPadding(new Insets(8));
        root.getChildren().addAll(new Label("Admin Return Tools"), table, actions);

        back.setOnAction(e -> showDashboard());
        btnReturn.setOnAction(e -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showAlert("Return", "Select a tool."); return; }
            String cond = tfCondition.getText().trim();
            if (cond.isEmpty()) { showAlert("Return", "Enter condition after return."); return; }

            boolean ok = toolController.returnTool(sel.getId(), sel.getUserId(), cond);
            if (ok) {
                showAlert("Return", "Tool returned.");
                table.setItems(FXCollections.observableArrayList(toolController.getAllTools().stream()
                        .filter(t -> t.getUserId() != 0).toList()));
            } else showAlert("Return", "Failed to return tool.");
        });
    }

    // Utility method for displaying informational alerts
    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}