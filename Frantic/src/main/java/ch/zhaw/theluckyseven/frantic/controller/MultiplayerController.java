package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.GameSetup;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The MultiplayerController handles all functionalities for players creating a multiplayer game.
 * A player needs to input his name, the amount of players and the game duration.
 */
public class MultiplayerController extends PreGameController implements SetupController {

    @FXML
    Button openLobbyButton;
    @FXML
    TextField codeField;
    //FXML TextArea
    @FXML
    TextArea logTextArea;

    /**
     * Sets the first stage when creating UI with all its functionalities.
     *
     * @param primaryStage stage on which the UI is displayed
     */
    public void startMultiplayerController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createButtonGroup();
        createTextListener();
    }

    void createTextListener() {
        openLobbyButton.setDisable(true);

        nameField.textProperty().addListener((observableValue, s, t1) -> openLobbyButton.setDisable(t1.trim().equals("")));
    }

    /**
     * On Action method if the cancelButton is pressed.
     */
    public void cancelButtonPressed() {
        GameSetup.closeServer();
        returnToMainMenu();
    }

    /**
     * On Action method if openLobby is pressed.
     */
    public void openLobbyButtonPressed() {
        createNewLobby();
    }

    /**
     * On Action method if startGame is pressed.
     */
    public void startGameButtonPressed() {
        GameSetup.createClient(nameField.getText(), false);
    }

    private void createNewLobby() {
        setupServer();
        disableGuiElements();
    }

    void setupServer() {
        int humanPlayers = getSliderValue();
        try {
            GameSetup.initServer(this, humanPlayers, getRadioButtonValue());
        } catch (IOException e) {
            logger.severe("couldn't initialize Server");
            e.printStackTrace();
        }
        displayCode(GameSetup.getCode());
    }

    private int getSliderValue() {
        int sliderValue = (int) playerSlider.getValue();

        if (sliderValue >= Config.MIN_PLAYERS && sliderValue <= Config.MAX_PLAYERS) {
            return sliderValue;
        } else {
            return (int) playerSlider.getMin();
        }
    }

    private void displayCode(String code) {
        codeField.setEditable(false);
        codeField.setText(code);
    }

    /**
     * Disables GUI elements after a Lobby is created.
     * This prevents changes of inputed values for the server.
     */
    private void disableGuiElements() {
        openLobbyButton.setDisable(true);
        nameField.setEditable(false);
        playerSlider.setDisable(true);

        buttonGroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle;
            node.setDisable(true);
        });
    }

    private void setStartGameButton() {
        int myself = 1;
        if (playerCount + myself == getSliderValue()) {
            startGameButton.setDisable(false);
        }
    }

    /**
     * Everytime a new Client connects to the Server, this method is called.
     * Adds new player to logTextArea.
     *
     * @param name String as a playername
     */
    public void newPlayerConnected(String name) {
        playerCount++;
        logTextArea.appendText("Player: " + name + " has joined the game. \n");
        setStartGameButton();
    }
}
