package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.server_client_communication.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;

import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State.*;

/**
 * Receiver to handle different types of messages and states
 */
public class ClientReceiver extends Receiver implements Runnable {
    private final ClientConnectionHandler clientConnectionHandler;
    private final NetworkHandler.NetworkConnection<DataPackage> connection;
    private final PlayController controller;

    /**
     * Creates a client receiver
     *
     * @param clientConnectionHandler connection handler for the client
     * @param connection              the actual connection to the server
     * @param controller              controller of the client
     */
    public ClientReceiver(ClientConnectionHandler clientConnectionHandler,
                          NetworkHandler.NetworkConnection<DataPackage> connection,
                          PlayController controller) {
        this.clientConnectionHandler = clientConnectionHandler;
        this.connection = connection;
        this.controller = controller;
    }

    @Override
    public void processConfirm() {
        if (clientConnectionHandler.getState() == CONFIRM_CONNECT) {
            controller.writeInfo(getMessage());
            Client.logger.info("CONFIRM" + getMessage());
            clientConnectionHandler.setState(CONNECTED);
        } else if (clientConnectionHandler.getState() == CONFIRM_DISCONNECT) {
            controller.writeInfo(getMessage());
            Client.logger.info("CONFIRM: " + getMessage());
            clientConnectionHandler.setState(DISCONNECTED);
        } else {
            Client.logger.warning("Got unexpected confirm message: " + getMessage());
        }
    }

    @Override
    public void processConnect() {
        Client.logger.warning("Illegal connect request from server");
    }

    @Override
    public void processDisconnect() {
        if (clientConnectionHandler.getState() == DISCONNECTED) {
            Client.logger.warning("DISCONNECT: Already in disconnected: " + getMessage());
            return;
        }
        controller.writeInfo(getMessage());
        Client.logger.info("DISCONNECT: " + getMessage());
        clientConnectionHandler.setState(DISCONNECTED);
    }

    @Override
    public void processError() {
        controller.writeError(getMessage());
        Client.logger.warning("ERROR: " + getMessage());
    }

    @Override
    public void processMessage() {
        if (clientConnectionHandler.getState() != CONNECTED) {
            Client.logger.warning("MESSAGE: Illegal state " + clientConnectionHandler.getState() + " for message: " + getMessage());
            return;
        }
        controller.execute(getDataPackage());
        Client.logger.info("Data received : " + getMessage());
    }

    @Override
    public void handleProtocolException(ProtocolException e) {
        Client.logger.log(Level.SEVERE, "Error while processing data: ", e);
        DataPackage data = new DataPackage(NetworkConfig.DataType.ERROR);
        data.setMessage(e.getMessage());
        clientConnectionHandler.sendData(data);
    }

    @Override
    public void run() {
        Client.logger.info("Start receiving data...");
        while (connection.isAvailable()) {
            try {
                DataPackage data = connection.receive();
                processData(data);
            } catch (SocketException e) {
                Client.logger.log(Level.SEVERE, "Connection terminated locally", e);
                controller.writeInfo("Connection to server lost");
                clientConnectionHandler.stopReceiving();
                clientConnectionHandler.setState(DISCONNECTED);
                Client.logger.log(Level.SEVERE, "Unregistered because connection terminated", e);
            } catch (EOFException e) {
                Client.logger.log(Level.SEVERE, "Connection terminated by remote", e);
                controller.writeInfo("Connection to server lost");
                clientConnectionHandler.stopReceiving();
                clientConnectionHandler.setState(DISCONNECTED);
                Client.logger.log(Level.SEVERE, "Unregistered because connection terminated", e);
            } catch (IOException e) {
                Client.logger.log(Level.SEVERE, "Communication error", e);
            } catch (ClassNotFoundException e) {
                Client.logger.log(Level.SEVERE, "Received object of unknown type", e);
            }
        }
        Client.logger.info("Stopped receiving data");
    }
}
