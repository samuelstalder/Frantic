/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ch.zhaw.theluckyseven.frantic;

import ch.zhaw.theluckyseven.frantic.controller.Client;
import ch.zhaw.theluckyseven.frantic.controller.SetupController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.Server;
import ch.zhaw.theluckyseven.frantic.view.MenuView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class to start the Frantic Game
 */
public class GameSetup extends Application {
    private static Server server;

    /**
     * Starts the Frantic Game
     *
     * @param args arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new MenuView(stage);
    }

    /**
     * Method for testing purposes
     *
     * @return String that indicating the project is "working"
     */
    public Object getGreeting() {
        return "working";
    }

    /**
     * Method to start Server
     *
     * @param controller   controller that created the Server
     * @param playerCount  total amount of player in game
     * @param gameDuration duration of game
     * @throws IOException if server couldn't have been initialized
     */
    public static void initServer(SetupController controller, int playerCount, Config.GameDuration gameDuration) throws IOException {
        server = new Server(controller, playerCount, gameDuration);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    /**
     * Method that closes the server if open
     */
    public static void closeServer() {
        if (server != null) {
            server.terminate();
        }
    }

    /**
     * Creates a client
     *
     * @param name       name of the user
     * @param isAiPlayer true if Client is AIPlayer
     */
    public static void createClient(String name, boolean isAiPlayer) {
        new Client(getCode(), name, isAiPlayer);
    }

    /**
     * Initiates AI players
     *
     * @param amount number of AIPlayers to create
     */
    public static void initAIPlayers(int amount) {
        for (int i = 0; i < amount; ++i) {
            new Client(getCode(), "Computer " + i, true);
        }
    }

    /**
     * Method to get the JoinCode of the Server
     *
     * @return the joincode
     */
    public static String getCode() {
        return server.getJoinCode();
    }
}
