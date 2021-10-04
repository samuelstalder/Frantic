package ch.zhaw.theluckyseven.frantic.view;

import ch.zhaw.theluckyseven.frantic.controller.Client;
import ch.zhaw.theluckyseven.frantic.controller.GameViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import static ch.zhaw.theluckyseven.frantic.view.MenuView.primaryStage;

/**
 * Class to display the GameView
 */
public class GameView {
    private final Client client;

    /**
     * Creates a GameView and sets name of the client as title of the stage
     *
     * @param client the (parent) client opening the view
     */
    public GameView(Client client) {
        this.client = client;
        primaryStage.setTitle(client.getName());
    }

    /**
     * Displays the GameView
     *
     * @return the controller of the GameView
     */
    public GameViewController display() {
        GameViewController controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch.zhaw.theluckyseven.frantic.view/MainGameWindow.fxml"));
            Pane rootPane = loader.load();

            //View calls Controller to work with
            controller = loader.getController();
            controller.initGameViewController(primaryStage, client);
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return controller;
    }
}
