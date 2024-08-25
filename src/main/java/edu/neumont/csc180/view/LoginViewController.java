/**
 * @author jsandland
 * @createdOn 8/25/2024 at 2:54 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.view;
 */
package edu.neumont.csc180.view;


import edu.neumont.csc180.controller.ChangeScene;
import edu.neumont.csc180.controller.SQLDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginViewController {


    @FXML
    private Label createAnAccountLinkText;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label invalidUsernameOrPasswordLabel;


    @FXML
    void createAnAccountLinkTextClicked(MouseEvent event) throws IOException {
        ChangeScene.changeSceneToCreateAccount(event);
    }


    @FXML
    void loginButtonPressed(ActionEvent event) throws IOException {
        if (checkLogin(usernameTextField.getText(), passwordTextField.getText())) {
            String username = SQLDatabase.getColumnValue("username", "password", passwordTextField.getText());
            ChangeScene.changeSceneToMenu(event, username);
        } else {
            loginFailed();
        }

    }

    private boolean checkLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return false;

        String sqlPassword = SQLDatabase.getColumnValue("password", "username", username);
        if (sqlPassword == null || !sqlPassword.equals(password)) return false;

        return true;
    }

    private void loginFailed() {
        invalidUsernameOrPasswordLabel.setVisible(true);
    }

}
