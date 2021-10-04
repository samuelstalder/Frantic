package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Card;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Factory class for game logic to create {@link DataPackage} to be sent
 */
public class PackageFactory {
    private static final Logger logger = Logger.getLogger(PackageFactory.class.getCanonicalName());
    private static GameStateController gameStateController;
    private static GameState gameState;

    private PackageFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static void setGameStateController(GameStateController gameStateController) {
        PackageFactory.gameStateController = gameStateController;
    }

    public static void setGameState(GameState gameState) {
        PackageFactory.gameState = gameState;
    }

    /**
     * create a dataPackage
     *
     * @param packageType the type of the package
     * @return the package
     */
    public static DataPackage create(DataPackage.LogicAction packageType) {
        DataPackage dataPackage = null;
        switch (packageType) {
            case REFRESH:
                dataPackage = buildRoundStateUpdate();
                break;
            case QUESTION:
                dataPackage = buildQuestion();
                break;
            case END_ROUND:
                dataPackage = buildEndOfRoundPackage();
                break;
            case END_GAME:
                dataPackage = buildEndOfGamePackage();
                break;
        }
        return dataPackage;
    }

    /**
     * - Set package type: Question
     * - Set actor
     * - Set questions
     * - Return package
     * Note: if cards is not 'interactive' null is returned.
     *
     * @return null, if card non-interactive
     * DataPackage, else
     */
    public static DataPackage buildQuestion() {
        DataPackage dataPackage = new DataPackage();
        Card card = gameState.getCardOnTopOfDiscardPile();

        if (card instanceof Interactive) {
            dataPackage = new DataPackage();
            logger.fine("Setting up question for " + card.getClass().getSimpleName() + " card");
            dataPackage.setCurrentPlayer(gameState.getCurrentPlayer().getName());
            dataPackage.setQuestions(Arrays.asList(((Interactive) card).getQuestion()));
            dataPackage.setLogicAction(DataPackage.LogicAction.QUESTION);
            logger.fine("QUESTION set: " + dataPackage.getQuestions());
            dataPackage.setOffer(((Interactive) card).getOffer(gameStateController));
            logger.fine("OFFER made: " + dataPackage.getOffer());
        }
        return dataPackage;
    }

    private static DataPackage buildRoundStateUpdate() {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setLogicAction(DataPackage.LogicAction.REFRESH);
        dataPackage.setCurrentCard(gameState.getCardOnTopOfDiscardPile());
        dataPackage.setEventCard(gameState.getLastOpenedEventCard());
        dataPackage.setCurrentPlayer(gameState.getCurrentPlayer().getName());
        dataPackage.setPlayers(gameState.getPlayers());
        String msg = String.format("#%d p: %S c: %S e: %S", gameState.getTurnCounter(), gameState.getNameOfActivePlayer(), gameState.getColorOfCardOnTopOfDiscardPile(), gameState.getLastOpenedEventCard());
        dataPackage.setMessage(msg);
        logger.fine("package: ROUND STATE UPDATE");
        return dataPackage;
    }

    private static DataPackage buildEndOfRoundPackage() {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setLogicAction(DataPackage.LogicAction.END_ROUND);
        dataPackage.setCurrentPlayer(gameState.getNameOfRoundWinner());
        dataPackage.setPlayers(gameState.getPlayers());
        StringBuilder msg = new StringBuilder();
        msg.append("Round has ended.\n".toUpperCase());
        gameState.getPlayers().forEach(p -> msg.append(String.format("%s has %d points%n", p.getName(), p.getScore())));
        msg.append(String.format("WINNER of the round: %S.%n", gameState.getNameOfRoundWinner()));
        msg.append(String.format("LOOSER of the round: %S.%n", gameState.getNameOfRoundLooser()));
        dataPackage.setMessage(msg.toString());
        logger.fine("package: ROUND END");
        return dataPackage;
    }

    private static DataPackage buildEndOfGamePackage() {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setLogicAction(DataPackage.LogicAction.END_GAME);
        dataPackage.setCurrentPlayer(gameState.getNameOfGameWinner());
        dataPackage.setPlayers(gameState.getPlayers());
        StringBuilder msg = new StringBuilder();
        msg.append("Game has ended.\n".toUpperCase());
        msg.append(String.format("GAME SCORE: %n %s", gameState.getGameScore().toString()));
        msg.append(String.format("Player \"%s\" won the game.%n", gameState.getNameOfGameWinner()));
        dataPackage.setMessage(msg.toString());
        logger.fine("package: GAME END ");
        return dataPackage;
    }
}