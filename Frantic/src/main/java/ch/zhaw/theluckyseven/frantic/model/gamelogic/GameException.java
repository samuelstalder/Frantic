package ch.zhaw.theluckyseven.frantic.model.gamelogic;

/**
 * Custom exception in GameLogic
 */
public class GameException extends Exception {
    private final GameError err;

    /**
     * Creates new Game exception
     *
     * @param error error that occured
     */
    public GameException(GameError error) {
        this.err = error;
    }

    @Override
    public String toString() {
        return err.toString();
    }

    /**
     * GameError enum
     */
    public enum GameError {
        MAX_PLAYERS("The number of maximum Players is " + Config.MAX_PLAYERS),
        NOT_ENOUGH_PLAYERS("The minimum of Players is " + Config.MIN_PLAYERS),
        CARD_CONTRACT_UNFULFILLED("GOT WRONG ANSWER");
        private final String msg;

        /**
         * Creates a GameError
         *
         * @param msg message of the error
         */
        GameError(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return this.msg;
        }
    }
}