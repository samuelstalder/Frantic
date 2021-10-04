package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

/**
 * Receiver for incoming Data
 */
public abstract class Receiver implements Runnable {

    DataPackage dataPackage;

    /**
     * processes the data
     *
     * @param data data to process
     */
    protected void processData(DataPackage data) {
        if (data != null) {
            try {
                dataPackage = data;
                switch (data.getType()) {
                    case CONNECT:
                        processConnect();
                        break;
                    case CONFIRM:
                        processConfirm();
                        break;
                    case DISCONNECT:
                        processDisconnect();
                        break;
                    case DATA:
                        processMessage();
                        break;
                    case ERROR:
                        processError();
                        break;
                    default:
                        throw new ProtocolException("unexpected type received");
                }
            } catch (ProtocolException e) {
                handleProtocolException(e);
            }
        }
    }


    /**
     * Processes Data of type CONFIRM
     */
    public abstract void processConfirm();

    /**
     * Processes Data of type CONNECT
     *
     * @throws ProtocolException if already connected
     */
    public abstract void processConnect() throws ProtocolException;

    /**
     * Processes Data of type DISCONNECT
     *
     * @throws ProtocolException if already disconnected
     */
    public abstract void processDisconnect() throws ProtocolException;

    /**
     * Processes Data of type ERROR
     */
    public abstract void processError();

    /**
     * Processes Data of type MESSAGE
     *
     * @throws ProtocolException if message is corrupted or not expecting a message
     */
    public abstract void processMessage() throws ProtocolException;

    /**
     * handles ProtocolException
     *
     * @param e ProtocolException
     */
    public abstract void handleProtocolException(ProtocolException e);

    public DataPackage getDataPackage() {
        return dataPackage;
    }

    public String getMessage() {
        return dataPackage.getMessage();
    }

    public String getCurrentPlayer() {
        return dataPackage.getCurrentPlayer();
    }
}
