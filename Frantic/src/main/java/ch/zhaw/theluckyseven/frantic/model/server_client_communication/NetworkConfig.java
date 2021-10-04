package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

/**
 * Class for global network communication configs of the application
 */
public class NetworkConfig {

    /**
     * DataTypes for network communication
     */
    public enum DataType {
        CONNECT, CONFIRM, DISCONNECT, DATA, ERROR
    }

    /**
     * States for client
     */
    public enum State {
        NEW, CONFIRM_CONNECT, CONNECTED, CONFIRM_DISCONNECT, DISCONNECTED
    }
}
