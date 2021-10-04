package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

/**
 * Custom ProtocolException for connection problem in the protocol
 */
public class ProtocolException extends Exception {

    /**
     * Creates new ProtocolException
     *
     * @param message message of the exception
     */
    public ProtocolException(String message) {
        super(message);
    }
}
