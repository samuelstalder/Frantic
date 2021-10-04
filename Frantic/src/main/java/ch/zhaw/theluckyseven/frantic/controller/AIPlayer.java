package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.controller.computer_strategies.AIStrategy;
import ch.zhaw.theluckyseven.frantic.controller.computer_strategies.ComputerStrategy;
import ch.zhaw.theluckyseven.frantic.controller.computer_strategies.SimpleStrategy;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig;

import java.util.List;
import java.util.stream.Collectors;

import static ch.zhaw.theluckyseven.frantic.controller.ClientPackageFactory.create;
import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.CardColor;
import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.random;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction.*;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State.DISCONNECTED;

/**
 * AIPlayer represents a computerplayer used in Singleplayer
 * AIPlayer is a computer controlled player
 */
public class AIPlayer extends PlayController {
    private ComputerStrategy strategy;
    private List<HandCard> playOptions;

    /**
     * Creates an AIPlayer
     *
     * @param myName name of the player
     * @param level  difficulty level of AIPlayer
     * @param client calling client (parent)
     */
    public AIPlayer(String myName, Smartness level, Client client) {
        this.myName = myName;
        strategy = chooseStrategy(level);
        initController(client);
    }

    //for later implementation of a more intellifent AI
    private ComputerStrategy chooseStrategy(Smartness level) {
        if (level == Smartness.SOPHISTICATED) {
            strategy = new AIStrategy(100);
        } else {
            strategy = new SimpleStrategy();
        }
        return strategy;
    }

    void setMyPlayer(DataPackage dataPackage) {
        dataPackage.getPlayers().stream()
                .filter(player -> player.getName().equals(myName))
                .findFirst()
                .ifPresent(myPlayer -> me = myPlayer);
    }

    /**
     * Method to update game data
     *
     * @param dataPackage new game data
     */
    @Override
    public void refreshData(DataPackage dataPackage) {
        setMyPlayer(dataPackage);
        playOptions = onlyPlayableCards();
        if (isMyTurn) {
            if (me.canFinishTurn()) {
                send(create(END_TURN));
            } else {
                play();
            }
        }
    }

    @Override
    protected CardColor colorChoice(List<Object> offer) {
        return (CardColor) offer.get(random.nextInt(offer.size()));
    }

    @Override
    protected int numberChoice(List<Object> offer) {
        return (int) offer.get(random.nextInt(offer.size()));
    }

    @Override
    protected Object numberOrColorChoice(List<Object> offer) {
        return offer.get(random.nextInt(offer.size()));
    }

    @Override
    protected Player playerChoice(List<Object> offer) {
        return (Player) offer.get(random.nextInt(offer.size()));
    }

    @Override
    protected HandCard cardChoice(Player target) {
        List<HandCard> cards = target.getCards();
        if (cards.isEmpty()) {
            return null;
        }
        HandCard choice = cards.get(random.nextInt(cards.size()));
        cards.remove(choice);
        return choice;
    }

    @Override
    Player actorChoice() {
        return me;
    }

    @Override
    void handleRoundEnd(DataPackage dataPackage) {
        //nothing has to be done since this is AI Player
    }

    @Override
    void handleGameEnd(DataPackage dataPackage) {
        terminateConnectionHandler();
    }

    /**
     * Method to inform of an Error
     *
     * @param error message of the error
     */
    @Override
    public void writeError(String error) {
        //no writing of error required since it is AI and not user
    }

    /**
     * Method to display information
     *
     * @param info information to dispay
     */
    @Override
    public void writeInfo(String info) {
        //no writing of error required since it is AI and not user
    }

    /**
     * Method to change the state
     *
     * @param newState new state
     */
    @Override
    public void stateChanged(NetworkConfig.State newState) {
        if (newState == DISCONNECTED) {
            terminateConnectionHandler();
        }
    }

    private void terminateConnectionHandler() {
        client.terminateConnectionHandler();
    }

    private void play() {
        if (!playOptions.isEmpty()) {
            //Play Card
            HandCard chosenCard = strategy.chooseCard(null, playOptions, null);
            send(create(PLACE, chosenCard));
        } else {
            if (me.canDrawCard()) {
                send(create(DRAW));
            } else {
                send(create(END_TURN));
            }
        }
    }

    List<HandCard> onlyPlayableCards() {
        return me.getCards().stream().filter(HandCard::isPlayable).collect(Collectors.toList());
    }

    /**
     * Difficulty level of the AI-Player
     */
    public enum Smartness {
        //for future release
        SIMPLE, SOPHISTICATED
    }
}
