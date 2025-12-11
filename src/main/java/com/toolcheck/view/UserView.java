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

import java.util.List;

 //User dashboard: 2 main functions (Checkout tools, Return tools).
public class UserView extends View {

    // Controller for user-related operations such as login
    private final UserControllerInterface userController;

    //Controller for tool-related operations (CRUD, checkout/return)
    private final ToolControllerInterface toolController;

    // The currently logged-in user
    private final UserInterface currentUser;

    // Main vertical layout for the dashboard
    private VBox mainLayout;

    // Buttons for dashboard action
    private Button btnCheckoutTools;
    private Button btnReturnTools;
    private Button btnLogout;

    // Constructor initializes controllers and current user
    public UserView(Stage stage,
                    UserControllerInterface userController,
                    ToolControllerInterface toolController,
                    UserInterface currentUser) {

        super(stage);
        this.userController = userController;
        this.toolController = toolController;
        this.currentUser = currentUser;
    }

    // Displays the user dashboard.
    @Override
    public void show() {
        mainLayout = new VBox(14);
        mainLayout.setPadding(new Insets(18));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f7f7b7, #d6ffd6);");

        Label title = new Label("ToolCheck - User Dashboard (" + currentUser.getUsername() + ")");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        initButtons();
        showDashboard(title);

        Scene scene = new Scene(mainLayout, 850, 520);
        stage.setScene(scene);
        stage.setTitle("ToolCheck - User");
        stage.show();
    }

    // Initializes the dashboard buttons and their styles.
    private void initButtons() {
        btnCheckoutTools = new Button("Checkout Tools");
        btnReturnTools = new Button("Return Tools");
        btnLogout = new Button("Logout");

        String style = "-fx-font-size: 13pt; -fx-padding: 8 14; -fx-background-color: #ffffffcc;";
        btnCheckoutTools.setStyle(style);
        btnReturnTools.setStyle(style);
        btnLogout.setStyle(style);
    }

    // Sets up the main dashboard view with title and action buttons.
    private void showDashboard(Label title) {
        mainLayout.getChildren().clear();
        HBox bar = new HBox(10, btnCheckoutTools, btnReturnTools, btnLogout);
        bar.setPadding(new Insets(8));

        mainLayout.getChildren().addAll(title, bar);

        btnCheckoutTools.setOnAction(e -> showCheckoutTools());
        btnReturnTools.setOnAction(e -> showReturnTools());
        btnLogout.setOnAction(e -> stage.close());
    }

    // Displays the list of available tools for checkout.
    // Allows user to select a tool and checkout with optional condition input.
    private void showCheckoutTools() {
        mainLayout.getChildren().clear();

        Label header = new Label("Available Tools (select one and checkout)");
        header.setStyle("-fx-font-weight: bold;");

        TableView<ToolInterface> table = new TableView<>();
        TableColumn<ToolInterface, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject());
        TableColumn<ToolInterface, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName()));
        TableColumn<ToolInterface, String> condCol = new TableColumn<>("Condition");
        condCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getCondition()));
        TableColumn<ToolInterface, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getStatus()));

        table.getColumns().addAll(idCol, nameCol, condCol, statusCol);

        //Filter tools to show only available ones
        List<ToolInterface> tools = toolController.getAllTools();

        // show only available tools
        List<ToolInterface> available = tools.stream()
                .filter(t -> t.getUserId() == 0 || t.getUserId() == -1 || "available".equalsIgnoreCase(t.getStatus()))
                .toList();
        table.setItems(FXCollections.observableArrayList(available));

        Button btnCheckout = new Button("Checkout Selected");
        Button btnBack = new Button("Back");

        HBox actions = new HBox(10, btnCheckout, btnBack);
        actions.setPadding(new Insets(8));

        mainLayout.getChildren().addAll(header, table, actions);

        btnBack.setOnAction(ev -> show());

        btnCheckout.setOnAction(ev -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showAlert("Checkout", "Please select a tool to checkout.");
                return;
            }

            // // Optional input for tool condition at checkout
            TextInputDialog condDialog = new TextInputDialog(sel.getCondition() == null ? "GOOD" : sel.getCondition());
            condDialog.setHeaderText("Tool condition at checkout (optional)");
            condDialog.setContentText("Condition:");
            condDialog.showAndWait().ifPresent(condition -> {
                boolean ok = toolController.checkoutTool(sel.getId(), currentUser.getId());
                if (ok) {
                    showAlert("Checkout", "Tool checked out successfully.");
                    // refresh list
                    table.setItems(FXCollections.observableArrayList(
                            toolController.getAllTools().stream()
                                    .filter(t -> t.getUserId() == 0 || "available".equalsIgnoreCase(t.getStatus()))
                                    .toList()
                    ));
                } else {
                    showAlert("Checkout", "Failed to checkout tool.");
                }
            });
        });
    }


    //Displays the list of tools currently checked out by the user.
    private void showReturnTools() {
        mainLayout.getChildren().clear();

        Label header = new Label("My Checked-out Tools (select one and return)");
        header.setStyle("-fx-font-weight: bold;");

        TableView<ToolInterface> table = new TableView<>();
        // Column: tool ID
        TableColumn<ToolInterface, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleLongProperty(p.getValue().getId()).asObject());

        // Column: tool name
        TableColumn<ToolInterface, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName()));

        // Column: tool condition
        TableColumn<ToolInterface, String> condCol = new TableColumn<>("Condition (when checked out)");
        condCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getCondition()));

        // Column: tool status
        TableColumn<ToolInterface, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getStatus()));

        table.getColumns().addAll(idCol, nameCol, condCol, statusCol);

        // tools assigned to current user
        List<ToolInterface> userTools = toolController.getAllTools().stream()
                .filter(t -> t.getUserId() == currentUser.getId())
                .toList();
        table.setItems(FXCollections.observableArrayList(userTools));

        // Return inputs
        TextField txtCondition = new TextField();
        txtCondition.setPromptText("Condition after return (e.g. GOOD)");


        Button btnReturn = new Button("Return Selected");
        Button btnBack = new Button("Back");

        HBox inputs = new HBox(8, txtCondition);
        HBox actions = new HBox(10, btnReturn, btnBack);
        inputs.setPadding(new Insets(6));
        actions.setPadding(new Insets(6));

        mainLayout.getChildren().addAll(header, table, inputs, actions);

        btnBack.setOnAction(e -> show());

        btnReturn.setOnAction(e -> {
            ToolInterface sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showAlert("Return", "Please select a tool to return.");
                return;
            }
            String condition = txtCondition.getText().trim();
            if (condition.isEmpty()) {
                showAlert("Return", "Please enter the condition after return.");
                return;
            }

            boolean ok = toolController.returnTool(sel.getId(), currentUser.getId(), condition);
            if (ok) {
                showAlert("Return", "Tool returned successfully.");
                // Refresh table to show remaining checked-out tools
                table.setItems(FXCollections.observableArrayList(
                        toolController.getAllTools().stream()
                                .filter(t -> t.getUserId() == currentUser.getId())
                                .toList()
                ));
            } else {
                showAlert("Return", "Failed to return tool.");
            }
        });
    }
    //Helper method to show an information alert dialog.
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}