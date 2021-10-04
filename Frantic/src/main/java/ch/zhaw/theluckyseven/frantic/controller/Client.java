package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkHandler;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.ProtocolException;
import ch.zhaw.theluckyseven.frantic.view.GameView;

import java.io.IOException;
import java.util.logging.Logger;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.DECODER_OFFSET;

/**
 * Client to create GUI or AIPlayer and establish connection to Server
 */
public class Client {
    static final Logger logger = Logger.getLogger(Client.class.getName());
    private final String joinCode;
    private final String ipAddress;
    private final String name;
    private PlayController controller;
    private ClientConnectionHandler connectionHandler;

    /**
     * Creates client instance
     *
     * @param joinCode   code to join
     * @param name       name of the player
     * @param isAiPlayer true if instance is for an AIPlayer
     */
    public Client(String joinCode, String name, boolean isAiPlayer) {
        this.joinCode = addressDecoder(joinCode);
        ipAddress = joinCode;
        this.name = name;

        if (isAiPlayer) {
            initAiPlayer();
        } else {
            openGameView();
        }

        startConnectionHandler();
    }

    /**
     * simple decoder with ASCII to make the code userfriendly
     *
     * @param addressToCode ip Adress from the Server
     * @return decoded joinCode
     */
    private static String addressDecoder(String addressToCode) {
        addressToCode = addressToCode.toUpperCase();
        StringBuilder identifier = new StringBuilder();
        for (char character : addressToCode.toCharArray()) {
            identifier.append((char) (character - DECODER_OFFSET));
        }
        return identifier.toString();
    }

    public String getName() {
        return name;
    }

    /**
     * Method to start connectionHandler for the connection to the sever
     */
    private void startConnectionHandler() {
        try {
            connectionHandler = new ClientConnectionHandler(
                    NetworkHandler.openConnection(joinCode, NetworkHandler.DEFAULT_PORT),
                    name, controller);
            logger.info("Connecting to " + ipAddress + " with port " + NetworkHandler.DEFAULT_PORT);
            connectionHandler.startReceiving();
            connectionHandler.connect();
        } catch (IOException e) {
            logger.severe("Error while opening connection to the Networkhandler");
            controller.writeError(e.getMessage());
        } catch (ProtocolException e) {
            logger.severe("Error: couldn't connect to the connectionHandler");
            controller.writeError(e.getMessage());
        }
    }

    /**
     * Method to stop the connectionHandler
     */
    void terminateConnectionHandler() {
        if (connectionHandler != null) {
            connectionHandler.stopReceiving();
            connectionHandler = null;
        }
    }

    private void openGameView() {
        GameView gameView = new GameView(this);
        controller = gameView.display();
    }

    /**
     * Closes the client application
     */
    void applicationClose() {
        terminateConnectionHandler();
    }

    private void initAiPlayer() {
        controller = new AIPlayer(name, AIPlayer.Smartness.SIMPLE, this);
        //beta version only supports Simple as smartness
    }

    /**
     * Method to submit data to the server
     *
     * @param dataPackage data to send
     */
    void send(DataPackage dataPackage) {
        try {
            connectionHandler.message(dataPackage);
        } catch (ProtocolException e) {
            logger.severe("no connection available");
            e.printStackTrace();
        }
    }
}
