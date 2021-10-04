package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkHandler;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.ProtocolException;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.DataType;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State.CONFIRM_CONNECT;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State.NEW;

/**
 * Connection Handler between the client and the server.
 * Talks to controller and NetworkHandler to translate GUI inputs to the server and vice-verca.
 */
public class ClientConnectionHandler {
    private final NetworkHandler.NetworkConnection<DataPackage> connection;
    private final PlayController controller;
    private final String userName;
    private State state = NEW;

    /**
     * Creates a new instance of ClientConnectionHandler
     *
     * @param connection NetworkHandler instance to talk to the server
     * @param userName   userName of the user
     * @param controller controller instance to hanlde the received packages
     */
    public ClientConnectionHandler(NetworkHandler.NetworkConnection<DataPackage> connection,
                                   String userName,
                                   PlayController controller) {
        this.connection = connection;
        this.userName = userName;
        this.controller = controller;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State newState) {
        this.state = newState;
        controller.stateChanged(newState);
    }

    /**
     * Method to start a new thread to listen to messages from the server.
     */
    public void startReceiving() {
        Client.logger.fine("Starting Connection Handler");
        ClientReceiver receiver = new ClientReceiver(this, connection, controller);
        Thread thread = new Thread(receiver);
        thread.start();
    }

    /**
     * Method to stop the receiving thread to stop listening to messages from the server
     */
    public void stopReceiving() {
        Client.logger.info("Closing Connection Handler to Server");
        try {
            Client.logger.finer("Stop receiving data...");
            connection.close();
            Client.logger.finer("Stopped receiving data.");
        } catch (IOException e) {
            Client.logger.severe("Failed to close connection.");
            e.printStackTrace();
        }
        Client.logger.fine("Closed Connection Handler to Server");
    }

    /**
     * Method to send data to the server
     *
     * @param data data object with details
     */
    public void sendData(DataPackage data) {
        if (connection.isAvailable()) {
            try {
                connection.send(data);
            } catch (SocketException e) {
                Client.logger.severe("Connection closed");
                e.printStackTrace();
            } catch (EOFException e) {
                Client.logger.severe("Connection terminated by remote");
                e.printStackTrace();
            } catch (IOException e) {
                Client.logger.severe("Communication error");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to connect to the server.
     *
     * @throws ProtocolException if already connected.
     */
    public void connect() throws ProtocolException {
        if (state != NEW) {
            throw new ProtocolException("Illegal state for connect: " + state);
        }
        DataPackage data = new DataPackage(DataType.CONNECT);
        data.setCurrentPlayer(userName);
        this.sendData(data);
        this.setState(CONFIRM_CONNECT);
    }

    /**
     * Method to send a message to a specified receiver
     *
     * @param dataPackage the datapackage to be sent
     * @throws ProtocolException if not connected
     */
    public void message(DataPackage dataPackage) throws ProtocolException {
        this.sendData(dataPackage);
    }
}
