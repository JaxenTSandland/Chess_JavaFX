package edu.neumont.csc180;

import edu.neumont.csc180.controller.ChangeScene;
import edu.neumont.csc180.controller.ChessJavaFXManager;
import edu.neumont.csc180.controller.ChessManager;
import edu.neumont.csc180.view.ChessboardViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        ChangeScene.changeSceneToMenu(stage, "test");
    }

    public static void main(String[] args) {
        launch();
        //new ChessManager().startMenu();
    }
}