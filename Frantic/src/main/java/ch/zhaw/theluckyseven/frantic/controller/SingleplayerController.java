package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.GameSetup;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The SingleplayerController handles all functionalities for players creating a singleplayer game.
 * A player needs to input his name, the amount of players and the game duration.
 */
public class SingleplayerController extends PreGameController implements SetupController {
    private static final int MAX_VALUE_SLIDER = 7;
    private static final int MIN_VALUE_SLIDER = 1;


    /**
     * Sets the first stage when creating UI with all its functionalities.
     *
     * @param primaryStage stage where the UI is displayed
     */
    public void startSingleplayerController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        startGameButton.setDisable(true);
        createButtonGroup();
        createTextListener();
    }

    void createTextListener() {
        nameField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> startGameButton.setDisable(newValue.trim().equals(""))
        );
    }

    /**
     * On Action method if startGame is pressed.
     */
    public void startGameButtonPressed() {
        setupServer();
        String name = nameField.getText();
        GameSetup.initAIPlayers(getAmountOfComputerPlayers());
        GameSetup.createClient(name, false);
    }

    void setupServer() {
        Config.GameDuration gameDuration = getRadioButtonValue();
        int myself = 1;
        int totalAmountOfPlayers = getAmountOfComputerPlayers() + myself;
        try {
            GameSetup.initServer(this, totalAmountOfPlayers, gameDuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getAmountOfComputerPlayers() {
        int sliderValue = (int) playerSlider.getValue();

        if (sliderValue >= MIN_VALUE_SLIDER && sliderValue <= MAX_VALUE_SLIDER) {
            return sliderValue;
        } else {
            return (int) playerSlider.getMin();
        }
    }

    /**
     * Logs a new player connected.
     *
     * @param name name of connected Player
     */
    @Override
    public void newPlayerConnected(String name) {
        //in future release display Computers that are already connected
        logger.info(name + " connected");
    }
}
