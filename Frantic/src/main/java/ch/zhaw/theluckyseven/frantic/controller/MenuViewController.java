package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * MenuViewController handles all the the functionalities of the MainMenu.
 */
public class MenuViewController {
    private static Stage primaryStage;

    //FXML
    @FXML
    Button singlePlayerButton;
    @FXML
    Button multiPlayerButton;
    @FXML
    Button joinGameButton;
    @FXML
    Button helpButton;

    /**
     * Sets the first stage when creating UI with all its functionalities.
     *
     * @param primaryStage stage on which de Menu gets displayed
     */
    public void startMenuViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Method to display SinglePlayer view.
     */
    public void displaySinglePlayer() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/SingleplayerWindow.fxml"));
            Parent root = loader.load();

            SingleplayerController controller =
                    loader.getController();
            controller.startSingleplayerController(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to display MultiPlayer view.
     */
    public void displayMultiPlayer() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/MultiplayerWindow.fxml"));
            Parent root = loader.load();

            MultiplayerController controller =
                    loader.getController();
            controller.startMultiplayerController(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display JoinGame view.
     */
    public void displayJoinGame() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/JoinGameWindow.fxml"));
            Parent root = loader.load();

            JoinGameController controller =
                    loader.getController();
            controller.startJoinGameController(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On Action method if the singlePlayerButton is pressed.
     */
    public void singlePlayerPressed() {
        displaySinglePlayer();
    }

    /**
     * On Action method if the mulitPlayerButton is pressed.
     */
    public void multiPlayerPressed() {
        displayMultiPlayer();
    }

    /**
     * On Action method if the joinGameButton is pressed.
     */
    public void joinGamePressed() {
        displayJoinGame();
    }

    /**
     * On Action method if the helpButton is pressed.
     */
    public void helpButtonPressed() {
        GameViewController.openWebpage(Config.FRANTIC_URL);
    }
}
