package com.toolcheck;

import com.toolcheck.controller.ToolController;
import com.toolcheck.controller.UserController;
import com.toolcheck.model.UserInterface;
import com.toolcheck.view.AdminView;
import com.toolcheck.view.UserView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


//Main JavaFX application launcher for Toolcheck
public class MainFX extends Application {

    // Controller for user-related operations such as login
    private final UserController userController = new UserController();

    // Controller for tool-related operations
    private final ToolController toolController = new ToolController();

    // Initializes the database and displays the login view.
    @Override
    public void start(Stage stage) {

        DBConnection.initDB();     // Initialize database connection before showing any UI

        showLoginView(stage);      // Show the login screen
    }

    // The screen allows the user to enter a username and password.
    private void showLoginView(Stage stage) {

        // VBox layout with vertical spacing of 12 pixels
        VBox loginPane = new VBox(12);
        loginPane.setPadding(new Insets(24));
        loginPane.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f9ff, #a0e3ff);");

        // Username label and input field
        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();

        // Password label and input field
        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();

        // Login button with custom styling
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-font-size: 13pt; -fx-padding: 8 14 8 14;");

        //status label to diaplay
        Label lblStatus = new Label("");

        // Add all controls to the VBox
        loginPane.getChildren().addAll(lblUser, txtUser, lblPass, txtPass, btnLogin, lblStatus);

        // set up the scene and stage
        Scene scene = new Scene(loginPane, 420, 270);
        stage.setScene(scene);
        stage.setTitle("ToolCheck - Login");
        stage.show();

        // Handle login button click
        btnLogin.setOnAction(e -> {
            String username = txtUser.getText().trim();
            String password = txtPass.getText().trim();

            // Check for empty username or password
            if (username.isEmpty() || password.isEmpty()) {
                lblStatus.setText("Enter username and password.");
                return;
            }

            // Attempt to login using the userController
            UserInterface loggedUser = userController.loginUser(username, password);
            if (loggedUser != null) {

                // Get correct dashboard based on user role
                if ("admin".equalsIgnoreCase(loggedUser.getRole())) {
                    new AdminView(stage, userController, toolController).show();
                } else {
                    new UserView(stage, userController, toolController, loggedUser).show();
                }
            } else {
                // Show error message if login fails
                lblStatus.setText("Invalid username or password.");
            }
        });
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}