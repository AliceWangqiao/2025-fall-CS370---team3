package com.toolcheck.view;

import javafx.stage.Stage;

// UI window
public abstract class View {

    // The primary JavaFX Stage (window)
    protected Stage stage;

    //Default constructor, allows subclass to set the stage later
    public View() {}

    //Constructor to initialize the view with a specific JavaFX Stage.
    public View(Stage stage) { this.stage = stage; }

    // Displays the view. this method sets up the scene and shows the stage.
    public abstract void show();
}
