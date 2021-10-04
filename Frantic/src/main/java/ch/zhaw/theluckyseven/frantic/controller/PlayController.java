package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.theluckyseven.frantic.controller.Client.logger;
import static ch.zhaw.theluckyseven.frantic.controller.ClientPackageFactory.create;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction.ANSWER;

/**
 * Base Controller for controllers responsible during the game
 * defines general behavior of the controllers
 */
public abstract class PlayController {
    protected boolean isMyTurn = false;
    protected String myName = "";
    protected Player me;
    protected Client client;

    /**
     * Sets the client and name of the controller
     *
     * @param client the calling/parent client
     */
    public void initController(Client client) {
        this.client = client;
        this.myName = client.getName();
    }

    /**
     * Method that gets invoked when the client receives new data from the server
     *
     * @param dataPackage the data received
     */
    public void execute(DataPackage dataPackage) {
        if (dataPackage != null && dataPackage.getLogicAction() != null) {
            updateIsMyTurn(dataPackage);
            switch (dataPackage.getLogicAction()) {
                case REFRESH:
                    refreshData(dataPackage);
                    break;
                case QUESTION:
                    if (isMyTurn) handleQuestions(dataPackage);
                    break;
                case END_ROUND:
                    handleRoundEnd(dataPackage);
                    break;
                case END_GAME:
                    handleGameEnd(dataPackage);
                    break;
                default:
                    logger.severe("missing enum in logicAction");
                    break;
            }
        }
    }

    /**
     * Method to update {@link PlayController#isMyTurn}
     *
     * @param dataPackage new data to update with
     */
    protected void updateIsMyTurn(DataPackage dataPackage) {
        isMyTurn = myName.equals(dataPackage.getCurrentPlayer());
    }

    /**
     * Method to update game data
     *
     * @param dataPackage new game data
     */
    abstract void refreshData(DataPackage dataPackage);

    /**
     * Method to handle Questions and send back to the Server
     *
     * @param dataPackage package with questions to handle
     */
    protected void handleQuestions(DataPackage dataPackage) {
        if (dataPackage.getQuestions() == null || dataPackage.getQuestions().isEmpty()) return;
        List<Object> answer = new ArrayList<>();
        for (Interactive.CardQuestion question : dataPackage.getQuestions()) {
            logger.fine("Handling question: " + question);
            Player target = dataPackage.getTarget() != null ? dataPackage.getTarget() : me;
            switch (question) {
                case SET_ACTOR:
                    answer.add(actorChoice());
                    break;
                case CHOOSE_COLOR:
                    answer.add(colorChoice(dataPackage.getOffer()));
                    break;
                case CHOOSE_NUMBER:
                    answer.add(numberChoice(dataPackage.getOffer()));
                    break;
                case CHOOSE_COLOR_OR_NUMBER:
                    answer.add(numberOrColorChoice(dataPackage.getOffer()));
                    break;
                case CHOOSE_PLAYER:
                    Player choiceOfPlayer = playerChoice(dataPackage.getOffer());
                    if (dataPackage.getTarget() == null) dataPackage.setTarget(choiceOfPlayer);
                    answer.add(choiceOfPlayer);
                    break;
                case CHOOSE_CARD:
                    answer.add(cardChoice(target));
                    break;
            }
        }
        send(create(ANSWER, answer));
    }

    /**
     * Method to send data to the server
     *
     * @param dataPackage data to send
     */
    protected void send(DataPackage dataPackage) {
        if (client == null) {
            writeError("No Connectionhandler");
            return;
        }
        client.send(dataPackage);
    }

    /**
     * Method to choose a single color {@link ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.CardColor}
     *
     * @return the chosen color
     */
    abstract Config.CardColor colorChoice(List<Object> offer);

    /**
     * Method to choose a number
     *
     * @param offer numbers to choose from
     * @return the chosen number
     */
    abstract int numberChoice(List<Object> offer);

    /**
     * Method to choose a number OR a color
     *
     * @param offer the numbers and colors to choose from
     * @return the chosen number or color
     */
    abstract Object numberOrColorChoice(List<Object> offer);

    /**
     * Method to choose a Player
     *
     * @param offer players to choose from
     * @return the chosen Player
     */
    abstract Player playerChoice(List<Object> offer);

    /**
     * Method to choose a {@link HandCard}
     *
     * @param target cards to choose from
     * @return the chosen card
     */
    abstract HandCard cardChoice(Player target);

    /**
     * Method to choose an Actor
     *
     * @return the chosen actor
     */
    abstract Player actorChoice();

    /**
     * Method to handle the end of the round
     *
     * @param dataPackage package received
     */
    abstract void handleRoundEnd(DataPackage dataPackage);

    /**
     * Method to handle the end of the game
     *
     * @param dataPackage package received
     */
    abstract void handleGameEnd(DataPackage dataPackage);

    /**
     * Method to inform of an Error
     *
     * @param error message of the error
     */
    abstract void writeError(String error);

    /**
     * Method to display information
     *
     * @param info information to dispay
     */
    abstract void writeInfo(String info);

    /**
     * Method to change the state
     *
     * @param newState new state
     */
    abstract void stateChanged(NetworkConfig.State newState);
}
