package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

import ch.zhaw.theluckyseven.frantic.controller.SetupController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameFlowLogic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.DECODER_OFFSET;

/**
 * Server to communicate with all Clients
 */
public class Server implements Runnable {
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int totalAmountOfPlayers;
    private final Config.GameDuration gameDuration;
    private final String joinCode;
    private DataPackage answer = null;
    private final SetupController controller;
    private GameFlowLogic gameFlowLogic;

    // Server connection
    private final NetworkHandler.NetworkServer<DataPackage> networkServer;

    // Connection registry
    private final HashMap<String, ServerConnectionHandler> connections = new HashMap<>();

    /**
     * Creates a Server using the NetworkHandler protocol.
     *
     * @param serverPort           the port as the interface of the server.
     * @param controller           controller of the host
     * @param totalAmountOfPlayers amount of players (including AI players)
     * @param gameDuration         duration of the game
     * @throws IOException if there is a problem with the protocol
     */
    public Server(int serverPort, SetupController controller, int totalAmountOfPlayers, Config.GameDuration gameDuration) throws IOException {
        this.controller = controller;
        this.totalAmountOfPlayers = totalAmountOfPlayers;
        this.gameDuration = gameDuration;
        InetAddress inetAddress = InetAddress.getLocalHost();
        joinCode = adressEncoder(inetAddress.getHostAddress());
        logger.info("Create server connection");
        networkServer = NetworkHandler.createServer(serverPort);
        logger.info("Listening on " + networkServer.getHostAddress() + ":" + networkServer.getHostPort());
    }

    /**
     * Creates a Server using the NetworkHandler protocol.
     *
     * @param controller           controller of the host
     * @param totalAmountOfPlayers amount of players (including AI players)
     * @param gameDuration         duration of the game
     * @throws IOException if there is a problem with the protocol
     */
    public Server(SetupController controller, int totalAmountOfPlayers, Config.GameDuration gameDuration) throws IOException {
        this(NetworkHandler.DEFAULT_PORT, controller, totalAmountOfPlayers, gameDuration);
    }

    /**
     * method to start the server.
     * server keeps running until exception is thrown.
     */
    @Override
    public void run() {
        logger.info("Server started.");
        try {
            networkServer.setSocketTimeout(1000);
            while (connections.size() < totalAmountOfPlayers) {
                NetworkHandler.NetworkConnection<DataPackage> connection;
                try {
                    connection = networkServer.waitForConnection();
                } catch (SocketTimeoutException e) {
                    continue;
                }
                if (connections.size() >= totalAmountOfPlayers) continue;
                ServerConnectionHandler connectionHandler = new ServerConnectionHandler(connection, connections, this);
                connectionHandler.startReceiving();
                logger.info(String.format("Connected new Client %s with IP:Port <%s:%d>",
                        connectionHandler.getUserName(),
                        connection.getRemoteHost(),
                        connection.getRemotePort()
                ));
            }
            logger.info("Server no longer accepting new connections.");
            initGameFlow();
            logger.fine("Game logic finished");
        } catch (SocketException e) {
            logger.fine("Server connection terminated");
        } catch (IOException e) {
            logger.severe("Communication error");
            e.printStackTrace();
        }
        logger.fine("Server thread finished");
    }

    /**
     * Method to send message to all connected clients
     *
     * @param data data to send
     */
    public void broadcast(DataPackage data) {
        for (ServerConnectionHandler connection : connections.values()) {
            connection.sendData(data);
        }
    }

    /**
     * Method to tell the controller that a new player is connected
     *
     * @param name name of Player
     */
    void newPlayerConnected(String name) {
        controller.newPlayerConnected(name);
    }

    /**
     * method to stop the server
     */
    public void terminate() {
        try {
            if (networkServer != null && !networkServer.isClosed()) {
                logger.info("Close server port.");
                networkServer.close();
            }
        } catch (IOException e) {
            logger.severe("Failed to close server connection");
            e.printStackTrace();
        }
        if (gameFlowLogic != null) {
            synchronized (this) {
                logger.info("stopping game");
                gameFlowLogic.stopGame();
                DataPackage data = new DataPackage();
                data.setPlayerAction(DataPackage.PlayerAction.END_TURN);
                setAnswer(data);
            }
        }
    }

    public String getJoinCode() {
        return joinCode;
    }

    /**
     * Method to answer to the logic
     *
     * @param data answer received
     */
    public void setAnswer(DataPackage data) {
        synchronized (this) {
            answer = data;
            notifyAll();
        }
    }

    /**
     * Method to send a {@link DataPackage} to all clients and waiting for a response
     * This method is blocking until an answer is received
     * The first answer received is returned
     *
     * @param dataPackage data to send (question)
     * @return data received (answer)
     */
    public DataPackage sendPackage(DataPackage dataPackage) {
        synchronized (this) {
            broadcast(dataPackage);
            answer = null;
            while (answer == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.severe("the connection was disrupted while waiting or answer");
                    e.printStackTrace();
                }
            }
            return answer;
        }
    }

    private static String adressEncoder(String addressToCode) {
        StringBuilder identifier = new StringBuilder();
        for (char character : addressToCode.toCharArray()) {
            identifier.append((char) (character + DECODER_OFFSET));
        }
        return identifier.toString();
    }

    private void initGameFlow() {
        ArrayList<String> names = new ArrayList<>(connections.keySet());
        logger.info("Amount of players: " + names.size());
        try {
            gameFlowLogic = new GameFlowLogic(this, gameDuration, names);
            gameFlowLogic.executeGame();
        } catch (GameException e) {
            logger.severe("GameFlowLogic couldn't be initialised");
            e.printStackTrace();
        }
    }
}
