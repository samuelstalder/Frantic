package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.LogicAction;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Class to handle the gameflow.
 * This class is responsible for the correct sequence of the game
 */
public class GameFlowLogic {
    private static final Logger logger = Logger.getLogger(GameFlowLogic.class.getCanonicalName());
    private Server server;
    private GameStateController gameStateController;
    private DataPackage dataPackage;
    private boolean gameStopped = false;

    /**
     * @param server       game server
     * @param gameDuration game duration, contains information about number of cards and game goal score
     * @param players      list containing player names.
     *                     NOTE:   The order of the names in the list,
     *                     will be taken as players turns order.
     * @throws GameException when GameStateController couldn't have been startet
     */
    public GameFlowLogic(Server server, Config.GameDuration gameDuration, List<String> players) throws GameException {
        logConfiguration();
        gameStateController = new GameStateController(gameDuration, players);
        this.server = server;
        PackageFactory.setGameStateController(gameStateController);
        PackageFactory.setGameState(gameStateController.getGameState());
    }

    /**
     * For testing purposes
     */
    GameFlowLogic() {
        server = null;
    }

    private void logConfiguration() {
        Logger.getLogger(GameFlowLogic.class.getPackageName());
        InputStream logConfig = this.getClass().getClassLoader().getResourceAsStream("log.properties");
        try {
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (IOException e) {
            logger.severe("Can't find log.properties file.");
        }
    }

    /**
     * Method to force stop the game immediatley
     */
    public void stopGame() {
        this.gameStopped = true;
    }

    /**
     * Method to check if the game is stopped
     *
     * @return whether the game is stopped
     */
    boolean isGameStopped() {
        return gameStopped;
    }

    /**
     * Method to init the game
     *
     * @throws GameException if the game is stopped unexpectedly (ex. user disconnected)
     */
    public void executeGame() throws GameException {
        runGame();
        endGame();
    }

    /**
     * Method to run only the game
     *
     * @throws GameException
     */
    void runGame() throws GameException {
        while (isGameRunning()) {
            executeRound();
        }
    }

    boolean isGameRunning() {
        return !gameStopped && !gameStateController.isGameEnded();
    }

    void endGame() {
        gameStateController.endGame();
        DataPackage data = PackageFactory.create(LogicAction.END_GAME);
        server.broadcast(data);
    }

    void executeRound() throws GameException {
        startRound();
        runRound();
        endRound();
    }

    void startRound() throws GameException {
        gameStateController.startRound();
        DataPackage data = PackageFactory.create(LogicAction.REFRESH);
        server.broadcast(data);
        handleCardActions();
        runTurn();
    }

    void runRound() throws GameException {
        while (isRoundRunning()) {
            executeTurn();
        }
    }

    boolean isRoundRunning() {
        return !gameStopped && !gameStateController.isRoundEnded();
    }

    void endRound() {
        gameStateController.endRound();
        DataPackage data = PackageFactory.create(LogicAction.END_ROUND);
        server.broadcast(data);
    }

    void executeTurn() throws GameException {
        gameStateController.startTurn();
        runTurn();
        endTurn();
    }

    void runTurn() throws GameException {
        DataPackage data;
        do {
            data = PackageFactory.create(LogicAction.REFRESH);
            dataPackage = server.sendPackage(data);
            executePlayerAction(dataPackage.getPlayerAction());
        } while (isTurnRunning());
    }

    boolean isTurnRunning() {
        return !gameStopped && !canFinishTurn();
    }

    boolean canFinishTurn() {
        return gameStateController.currentPlayerCanFinishTurn() && PlayerAction.END_TURN.equals(dataPackage.getPlayerAction());
    }

    void endTurn() {
        DataPackage data;
        gameStateController.endTurn();
        data = PackageFactory.create(LogicAction.REFRESH);
        server.broadcast(data);
    }

    void executePlayerAction(PlayerAction playerAction) throws GameException {
        logger.info("PLAYER ACTION: ." + playerAction.name());
        switch (playerAction) {
            case DRAW:
                if (gameStateController.currentPlayerCanDrawCard()) {
                    gameStateController.dealHandCardToActivePlayer();
                }
                break;
            case PLACE:
                if (gameStateController.currentPlayerCanPlaceCard()) {
                    handleCardPlacement();
                }
                break;
            case END_TURN:
                break;
            default:
                logger.severe("missing case in switch");
        }
    }

    void handleCardPlacement() throws GameException {
        gameStateController.executeCardPlacement(dataPackage.getCurrentCard());
        handleCardActions();
        DataPackage data;
        data = PackageFactory.create(LogicAction.REFRESH);
        server.broadcast(data);
    }

    void handleCardActions() throws GameException {
        if (gameStateController.lastPlacedCardHasTriggeredEventInvocation()) {
            handleTriggeredEvenOfLastPlacedCard();
        }
        if (gameStateController.lastPlacedCardHasTriggeredActionInvocation()) {
            handleActionOfLastPlacedCard();
        }
    }

    void handleTriggeredEvenOfLastPlacedCard() {
        DataPackage data;
        if (gameStateController.lastPlacedCardHasTriggeredEventInvocation()) {
            gameStateController.openEventCard();
            data = PackageFactory.create(LogicAction.REFRESH);
            server.broadcast(data);
            gameStateController.playLastOpenedEventCard();
            server.broadcast(data);
        }
    }

    void handleActionOfLastPlacedCard() throws GameException {
        DataPackage data;
        data = PackageFactory.buildQuestion();
        if (data != null) {
            data = server.sendPackage(data);
            logger.fine("ANSWER received: " + data.getAnswers());
            gameStateController.playLastPlacedPlayingCard(data.getAnswers());
        }
    }

    GameStateController getGameStateController() {
        return gameStateController;
    }

    void setGameStateController(GameStateController gameStateController) {
        this.gameStateController = gameStateController;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}