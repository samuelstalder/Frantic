package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;

import java.util.List;

import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction;

/**
 * Utility Class to generate {@link DataPackage} for clients to be sent to the Server
 */
public class ClientPackageFactory {

    private ClientPackageFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Method to generate a {@link DataPackage} with given {@link PlayerAction}
     *
     * @param packageType action taken by the client
     * @return the resulting DataPackage with given action
     */
    public static DataPackage create(PlayerAction packageType) {
        return create(packageType, null);
    }

    /**
     * Method to generate a {@link DataPackage} with given {@link PlayerAction} and answers
     *
     * @param packageType action taken by the client
     * @param answers     answers coming with the action
     * @return the resulting dataPackage
     */
    public static DataPackage create(PlayerAction packageType, Object answers) {
        DataPackage dp = null;
        switch (packageType) {
            case END_TURN:
                dp = buildEndTurnPackage();
                break;
            case DRAW:
                dp = buildDrawPackage();
                break;
            case PLACE:
                dp = buildPlacePackage(answers);
                break;
            case ANSWER:
                dp = buildAnswerPackage(answers);
                break;
        }
        return dp;
    }

    private static DataPackage buildDrawPackage() {
        DataPackage dp = new DataPackage();
        dp.setPlayerAction(PlayerAction.DRAW);
        return dp;
    }

    private static DataPackage buildPlacePackage(Object answers) {
        DataPackage dp = new DataPackage();
        dp.setPlayerAction(PlayerAction.PLACE);
        dp.setCurrentCard((HandCard) answers);
        return dp;
    }

    private static DataPackage buildAnswerPackage(Object answers) {
        DataPackage dp = new DataPackage();
        dp.setPlayerAction(PlayerAction.ANSWER);
        dp.setAnswers((List<Object>) answers);
        return dp;
    }

    private static DataPackage buildEndTurnPackage() {
        DataPackage dp = new DataPackage();
        dp.setPlayerAction(PlayerAction.END_TURN);
        return dp;
    }
}
