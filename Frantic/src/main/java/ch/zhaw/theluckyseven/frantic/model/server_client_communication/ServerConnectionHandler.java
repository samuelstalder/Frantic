package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ConnectionHandler for the Server.
 * Works as an interface between the protocol and the server.
 * Holds a registry of all the connections with the connection clients.
 * If no username is provided in the connection, creates a 'Anonymous-*' name for the user.
 */
public class ServerConnectionHandler {
    private static final AtomicInteger connectionCounter = new AtomicInteger(0);
    private final int connectionId = connectionCounter.incrementAndGet();
    private final NetworkHandler.NetworkConnection<DataPackage> connection;
    private final HashMap<String, ServerConnectionHandler> connectionRegistry;
    private final Server server;
    private final String userName = "Anonymous-" + connectionId;

    /**
     * Creates ConnectionHandler for the server.
     *
     * @param connection networkHandler protocol
     * @param registry   registry of all the connected users.
     * @param server     Server/Parent instance of ConnectionHanlder
     */
    public ServerConnectionHandler(NetworkHandler.NetworkConnection<DataPackage> connection,
                                   HashMap<String, ServerConnectionHandler> registry,
                                   Server server) {
        Objects.requireNonNull(connection, "Connection must not be null");
        Objects.requireNonNull(registry, "Registry must not be null");
        this.connection = connection;
        this.connectionRegistry = registry;
        this.server = server;
    }

    public String getUserName() {
        return this.userName;
    }

    /**
     * Starts a new thread to start receiving messages from the client to the server.
     */
    public void startReceiving() {
        Server.logger.info("Starting Connection Handler for " + userName);
        ServerReceiver serverReceiver = new ServerReceiver(this, connection, connectionRegistry, server);
        Thread serverReceiverThread = new Thread(serverReceiver);
        serverReceiverThread.start();
    }

    /**
     * Stops thread to receive messages from the client
     */
    public void stopReceiving() {
        Server.logger.info("Closing Connection Handler for " + userName);
        try {
            Server.logger.info("Stop receiving data...");
            connection.close();
            Server.logger.info("Stopped receiving data.");
        } catch (IOException e) {
            Server.logger.severe("Failed to close connection.");
            e.printStackTrace();
        }
        Server.logger.info("Closed Connection Handler for " + userName);
    }

    /**
     * Method to send data
     *
     * @param data data instance to be sent
     */
    public void sendData(DataPackage data) {
        if (connection.isAvailable()) {
            try {
                connection.send(data);
            } catch (SocketException e) {
                Server.logger.severe("Connection closed.");
                e.printStackTrace();
            } catch (EOFException e) {
                Server.logger.severe("Connection terminated by remote.");
                e.printStackTrace();
            } catch (IOException e) {
                Server.logger.severe("Communication error.");
                e.printStackTrace();
            }
        }
    }
}
