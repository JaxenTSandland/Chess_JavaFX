package edu.neumont.csc180;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExampleController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}