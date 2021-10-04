package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Abstract Class defining general behavior of the PreGameControllers like {@link SingleplayerController},
 * {@link MultiplayerController} and {@link JoinGameController}.
 */
public abstract class PreGameController {
    protected static final Logger logger = Logger.getLogger(PreGameController.class.getCanonicalName());
    protected final ToggleGroup buttonGroup = new ToggleGroup();
    protected int playerCount = 0;
    protected Stage primaryStage;

    //FXML Button
    @FXML
    Button helpButton;
    @FXML
    Button startGameButton;
    @FXML
    Button cancelButton;
    //FXML Textfield
    @FXML
    TextField nameField;
    //FXML Slider
    @FXML
    Slider playerSlider;
    //FXML RadioButton
    @FXML
    RadioButton quickPlayRadioButton;
    @FXML
    RadioButton shortGameRadioButton;
    @FXML
    RadioButton normalGameRadioButton;
    @FXML
    RadioButton longGameRadioButton;

    abstract void createTextListener();

    /**
     * On Action method if helpButton is pressed.
     */
    public void helpButtonPressed() {
        GameViewController.openWebpage(Config.FRANTIC_URL);
    }

    /**
     * On Action method if the cancelButton is pressed.
     */
    public void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/MainMenuWindow.fxml"));
            Parent root = loader.load();

            MenuViewController controller =
                    loader.getController();
            controller.startMenuViewController(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            logger.severe("FXMLLoader error to return to main menu");
            e.printStackTrace();
        }
    }

    protected Config.GameDuration getRadioButtonValue() {
        Toggle toggle = buttonGroup.getSelectedToggle();

        if (toggle.equals(quickPlayRadioButton)) {
            return Config.GameDuration.QUICK_PLAY;
        } else if (toggle.equals(shortGameRadioButton)) {
            return Config.GameDuration.SHORT_GAME;
        } else if (toggle.equals(normalGameRadioButton)) {
            return Config.GameDuration.NORMAL_GAME;
        } else if (toggle.equals(longGameRadioButton)) {
            return Config.GameDuration.LONG_GAME;
        } else {
            // Default is Quickplay
            return Config.GameDuration.QUICK_PLAY;
        }
    }

    /**
     * Creates a toggleGroup so that only one RadioButton can be fired.
     * QuickPlay option is fired as a default.
     */
    void createButtonGroup() {
        quickPlayRadioButton.setToggleGroup(buttonGroup);
        quickPlayRadioButton.setSelected(true);
        shortGameRadioButton.setToggleGroup(buttonGroup);
        normalGameRadioButton.setToggleGroup(buttonGroup);
        longGameRadioButton.setToggleGroup(buttonGroup);

        buttonGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
        });
    }
}
