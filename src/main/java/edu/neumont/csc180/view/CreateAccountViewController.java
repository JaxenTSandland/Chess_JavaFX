/**
 * @author jsandland
 * @createdOn 8/25/2024 at 2:54 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.view;
 */
package edu.neumont.csc180.view;

import edu.neumont.csc180.controller.ChangeScene;
import edu.neumont.csc180.controller.SQLDatabase;
import edu.neumont.csc180.controller.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountViewController {

    final int PASSWORD_MIN_LENGTH = 8;
    final int PASSWORD_MIN_LOWERCASE = 1;
    final int PASSWORD_MIN_UPPERCASE = 1;
    final int PASSWORD_MIN_DIGITS = 1;
    final int PASSWORD_MIN_SYMBOLS = 1;

    //int minLength, int minUpper, int minLower, int minNumeric, int minSymbols

    @FXML
    private Button cancelButton;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label passwordRequirementsLabel;

    @FXML
    void cancelButtonPressed(ActionEvent event) throws IOException {
        SoundManager soundManager = new SoundManager();
        soundManager.playSound(SoundManager.Sounds.BUTTON_CLICK);
        ChangeScene.changeSceneToLogin(event);
    }

    @FXML
    void createAccountButtonPressed(ActionEvent event) throws IOException {
        SoundManager soundManager = new SoundManager();
        soundManager.playSound(SoundManager.Sounds.BUTTON_CLICK);

        if (usernameTextField.getText().isEmpty() || confirmPasswordTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            createAccountFailed("All fields must be filled in");
            return;
        }

        boolean confirmedPasswordMatches = confirmPasswordTextField.getText().equals(passwordTextField.getText());

        if (!confirmedPasswordMatches) {
            createAccountFailed("Passwords do not match");
            return;
        }

        if (!validatePasswordComplexity(passwordTextField.getText())) {
            createAccountFailed("Password requirements not met");
            return;
        }

        if (SQLDatabase.usernameExists(usernameTextField.getText())) {
            createAccountFailed("Username is taken");
            return;
        }

        boolean accountCreated = SQLDatabase.createAccount(usernameTextField.getText(), passwordTextField.getText());


        if (accountCreated) {
            ChangeScene.changeSceneToLogin(event);
        } else {
            createAccountFailed("Invalid account details");
        }
    }

    private void createAccountFailed(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private static boolean validateRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) return true;
        else return false;
    }

    private Matcher match(String line, String regex) {
        Pattern p = null;
        Matcher m = null;

        p = Pattern.compile(regex);
        m = p.matcher(line);
        return m;
    }

    public boolean validatePasswordComplexity(String password) {
        Matcher matcher = null;
        String regex = null;

        regex = "[A-Z]"; //Min uppercase
        matcher = match(password, regex);
        if (countMatcherGroups(matcher) < PASSWORD_MIN_UPPERCASE) return false;

        regex = "[a-z]"; //Min lowercase
        matcher = match(password, regex);
        if (countMatcherGroups(matcher) < PASSWORD_MIN_LOWERCASE) return false;

        regex = "[^ ]"; //Min length
        matcher = match(password, regex);
        if (countMatcherGroups(matcher) < PASSWORD_MIN_LENGTH) return false;

        regex = "[\\d]"; //Min numberic
        matcher = match(password, regex);
        if (countMatcherGroups(matcher) < PASSWORD_MIN_DIGITS) return false;

        regex = "[\\W]"; //Min symbols
        matcher = match(password, regex);
        if (countMatcherGroups(matcher) < PASSWORD_MIN_SYMBOLS) return false;

        return true;
    }

    private static int countMatcherGroups(Matcher matcher) {
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
