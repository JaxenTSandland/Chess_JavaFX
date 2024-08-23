package edu.neumont.csc180;

import edu.neumont.csc180.controller.ChessJavaFXManager;
import edu.neumont.csc180.controller.ChessManager;
import edu.neumont.csc180.view.ChessboardViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static ChessboardViewController fxController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/chessboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 900);

        fxController = fxmlLoader.getController();

        stage.setTitle("JaxenS_ChessJavaFX");
        stage.setScene(scene);
        stage.show();
        ChessJavaFXManager chessManager = new ChessJavaFXManager(fxController);
        fxController.setChessJavaFXManager(chessManager);
        chessManager.startNewGame();
    }

    public static void main(String[] args) {
        launch();
        //new ChessManager().startMenu();
    }
}