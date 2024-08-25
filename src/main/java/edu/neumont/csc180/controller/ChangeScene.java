/**
 * @author jsandland
 * @createdOn 8/23/2024 at 9:51 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;

import edu.neumont.csc180.view.ChessboardViewController;
import edu.neumont.csc180.view.MenuViewController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ChangeScene {


    private static FXMLLoader changeScene(Event event, String fxmlFileName) throws IOException {
        return changeScene(convertEventToStage(event), fxmlFileName);
    }


    private static FXMLLoader changeScene(Stage stage, String fxmlFileName) throws IOException {
        URL url = new File("src/main/resources/edu/neumont/csc180/fxml/" + fxmlFileName).toURI().toURL();
        if (stage != null) {
            stage.close();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(url);

        float width = 900;
        float height = 900;
        if (fxmlFileName.equals("login-view.fxml")) {
            width = 400;
            height = 500;
        } else if (fxmlFileName.equals("createAccount-view.fxml")) {
            width = 500;
            height = 500;
        } else if (fxmlFileName.equals("menu-view.fxml")) {
            width = 600;
            height = 400;
        }

        Scene scene = new Scene(fxmlLoader.load(), width, height);


        stage.setTitle("JaxenS_ChessJavaFX");
        stage.setScene(scene);
        stage.show();

        return fxmlLoader;
    }

    public static void changeSceneToGame(Event event, String username, boolean loadPreviousSave) throws IOException {
        FXMLLoader loader = changeScene(event, "chessboard-view.fxml");
        if (username == null || username.isEmpty()) throw new IllegalAccessError("Invalid username");

        ChessboardViewController fxController = loader.getController();
        ChessJavaFXManager chessManager = new ChessJavaFXManager(fxController);
        fxController.setChessJavaFXManager(chessManager, username);

        if (loadPreviousSave) {
            chessManager.loadContinuedGame(username);
        } else {
            chessManager.startNewGame(username);
        }
    }

    public static void changeSceneToMenu(Event event, String username) throws IOException {
        changeSceneToMenu(convertEventToStage(event), username);
    }

    public static void changeSceneToMenu(Stage stage, String username) throws IOException {
        FXMLLoader loader = changeScene(stage, "menu-view.fxml");
        if (username == null || username.isEmpty()) throw new IllegalAccessError("Invalid username");

        MenuViewController menuController = loader.getController();
        menuController.initData(username);
    }

    public static void changeSceneToLogin(Event event) throws IOException {
        changeSceneToLogin(convertEventToStage(event));
    }

    public static void changeSceneToLogin(Stage stage) throws IOException {
        changeScene(stage, "login-view.fxml");
    }

    public static void changeSceneToCreateAccount(Event event) throws IOException {
        changeSceneToCreateAccount(convertEventToStage(event));
    }

    public static void changeSceneToCreateAccount(Stage stage) throws IOException {
        changeScene(stage, "createAccount-view.fxml");
    }

    private static Stage convertEventToStage(Event event) {
        return (Stage)((Node)event.getSource()).getScene().getWindow();
    }

}
