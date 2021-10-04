package ch.zhaw.theluckyseven.frantic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * JoinGameController handles all the functionalities for players that need to join an existing server.
 * A player needs to input his name and the server code.
 */
public class JoinGameController extends PreGameController {
    private static boolean nameFieldEmpty = true;
    private static boolean codeFieldEmpty = true;

    //FXML Button
    @FXML
    Button joinButton;
    //FXML Texfield
    @FXML
    TextField gameCodeField;

    /**
     * Sets the first stage when creating UI with all its functionalities.
     *
     * @param primaryStage stage on which the Join GUI is displayed
     */
    public void startJoinGameController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        joinButton.setDisable(true);
        createTextListener();
    }

    /**
     * Adds the changeListener to the Textfields.
     */
    void createTextListener() {
        logger.finest("creation of listeners for nameField and codeField");
        nameField.textProperty().addListener((observableValue, s, t1) -> {
            nameFieldEmpty = t1.trim().equals("");
            setButtonClickable();
        });

        gameCodeField.textProperty().addListener((observableValue, s, t1) -> {
            codeFieldEmpty = t1.trim().equals("");
            setButtonClickable();
        });
    }

    private void setButtonClickable() {
        joinButton.setDisable(nameFieldEmpty || codeFieldEmpty);
    }

    /**
     * On Action method if the cancelButton is pressed.
     */
    public void cancelButtonPressed() {
        returnToMainMenu();
    }

    /**
     * On Action method if the joinButton is pressed.
     */
    public void joinButtonPressed() {
        joinClient();
    }

    //Create a client for the player to join an existing Server.
    private void joinClient() {
        String name = nameField.getText();
        String code = gameCodeField.getText();

        logger.fine("Joincode: " + code + " Name: " + name);
        new Client(code, name, false);
    }
}
