/**
 * @author jsandland
 * @createdOn 8/25/2024 at 2:15 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.view;
 */
package edu.neumont.csc180.view;
import edu.neumont.csc180.controller.ChangeScene;
import edu.neumont.csc180.controller.ChessLoader;
import edu.neumont.csc180.controller.SQLDatabase;
import edu.neumont.csc180.controller.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuViewController {

    @FXML
    private Button continueButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label usernameLabel;

    private String username;

    @FXML
    void continueButtonClicked(ActionEvent event) {
        playButtonSound();
        try {
            ChangeScene.changeSceneToGame(event, username, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void newGameButtonClicked(ActionEvent event) {
        playButtonSound();
        try {
            ChangeScene.changeSceneToGame(event, username, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) {
        playButtonSound();
        try {
            ChangeScene.changeSceneToLogin(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initData(String username) {
        this.username = username;
        usernameLabel.setText(username);

        continueButton.setDisable(ChessLoader.userHasEmptySave(username));
    }

    private void playButtonSound() {
        SoundManager soundManager = new SoundManager();
        soundManager.playSound(SoundManager.Sounds.BUTTON_CLICK);
    }

}