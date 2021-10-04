package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.DataType;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State;

/**
 * Server Receiver to handle different types of {@link DataPackage}
 */
public class ServerReceiver extends Receiver implements Runnable {

    private final ServerConnectionHandler serverConnectionHandler;
    private final NetworkHandler.NetworkConnection<DataPackage> connection;
    private final HashMap<String, ServerConnectionHandler> connectionRegistry;
    private final Server server;


    private State state = State.NEW;
    private String userName;

    /**
     * Creates instance of ServerReceiver
     *
     * @param serverConnectionHandler calling ConnectionHandler
     * @param connection              connection to listen on
     * @param connectionRegistry      map of all connections
     * @param server                  parent Server
     */
    public ServerReceiver(ServerConnectionHandler serverConnectionHandler,
                          NetworkHandler.NetworkConnection<DataPackage> connection,
                          HashMap<String, ServerConnectionHandler> connectionRegistry,
                          Server server) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.connection = connection;
        this.connectionRegistry = connectionRegistry;
        this.server = server;
        this.userName = serverConnectionHandler.getUserName();
    }

    @Override
    public void processConfirm() {
        Server.logger.warning("Not expecting to receive a CONFIRM request from client");
    }

    @Override
    public void processConnect() throws ProtocolException {
        if (state != State.NEW) {
            throw new ProtocolException("Illegal state for connect request: " + state);
        }
        if (connectionRegistry.containsKey(getCurrentPlayer())) {
            throw new ProtocolException("Username already taken: " + getCurrentPlayer());
        }
        this.userName = getCurrentPlayer();
        connectionRegistry.put(userName, serverConnectionHandler);
        server.newPlayerConnected(userName);
        DataPackage data = new DataPackage(DataType.CONFIRM);
        data.setMessage("Registration successful for " + userName);
        serverConnectionHandler.sendData(data);
        this.state = State.CONNECTED;
    }

    @Override
    public void processDisconnect() throws ProtocolException {
        if (state == State.DISCONNECTED) {
            throw new ProtocolException("Illegal state for disconnect request: " + state);
        }
        if (state == State.CONNECTED) {
            connectionRegistry.remove(this.userName);
        }
        dataPackage.setType(DataType.CONFIRM);
        dataPackage.setMessage("Confirm the disconnect of " + userName);
        serverConnectionHandler.sendData(dataPackage);
        this.state = State.DISCONNECTED;
        serverConnectionHandler.stopReceiving();
    }

    @Override
    public void processError() {
        Server.logger.info("Received error from the client (" + getCurrentPlayer() + "): " + getMessage());
    }

    @Override
    public void processMessage() throws ProtocolException {
        Server.logger.fine("Server received message");
        if (state != State.CONNECTED) {
            throw new ProtocolException("Illegal state for message request: " + state);
        }
        server.setAnswer(dataPackage);
        Server.logger.fine("Server sent message");
    }

    @Override
    public void handleProtocolException(ProtocolException e) {
        Server.logger.severe("Error while processing data: ");
        e.printStackTrace();
        dataPackage.setType(DataType.ERROR);
        dataPackage.setMessage(e.getMessage());
        serverConnectionHandler.sendData(dataPackage);
    }

    @Override
    public void run() {
        try {
            Server.logger.info("Start receiving data...");
            while (connection.isAvailable()) {
                DataPackage data = connection.receive();
                processData(data);
            }
            Server.logger.info("Stopped receiving data");
        } catch (SocketException e) {
            Server.logger.severe("Connection terminated locally " + e);
            removePlayer(userName);
            Server.logger.severe("Unregistered because client connection terminated: " + userName);
            e.printStackTrace();
        } catch (EOFException e) {
            Server.logger.severe("Connection terminated by remote " + e);
            removePlayer(userName);
            Server.logger.severe("Unregistered because client connection terminated: " + userName);
            e.printStackTrace();
        } catch (IOException e) {
            Server.logger.severe("Communication error.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Server.logger.severe("Received an object of unknown type: " + e);
        }
        Server.logger.info("Stopping Connection Handler for " + userName);
    }

    /**
     * Method to remove a player (disconnected) and stop server
     * @param userName name of user to be removed
     */
    private void removePlayer(String userName) {
        connectionRegistry.remove(userName);
        server.terminate();
    }
}
