package ch.zhaw.theluckyseven.frantic.model.server_client_communication;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Client has to determine by himself if it is his turn, using {@link DataPackage#currentPlayer}  and
 * {@link DataPackage#players}
 * Play Card: player sets {@link DataPackage#currentCard} with the {@link HandCard} played
 * Question: list with {@link Interactive.CardQuestion} for client
 * Answers: list with Answers to the {@link DataPackage#questions}
 */
public class DataPackage implements Serializable {
    private HandCard currentCard;       //last played card
    private EventCard eventCard;        //last played eventCard or null
    private String currentPlayer;       //unique name of player in playerList who's turn it is
    private List<Player> players;       //list of players (including Computerplayers)
    private String message;             //most recent game message
    private Player target;              //target for question or null if target is player himself
    private List<Interactive.CardQuestion> questions;   //list of Questions to ask client or empty
    private List<Object> answers;       //list of answers to the Question or empty
    private List<Object> offer;         //list of possibilities to choose from
    private int playerCount = 0;        //amount of players
    private PlayerAction playerAction;  //action of player
    private LogicAction logicAction;    //action of logic
    private NetworkConfig.DataType type;

    /**
     * Creates an empty Datapackage
     */
    public DataPackage() {
        currentCard = null;
        eventCard = null;
        currentPlayer = "";
        players = new ArrayList<>();
        message = "";
        type = NetworkConfig.DataType.DATA;
        answers = new ArrayList<>();
    }

    /**
     * Generate a Datapackage with type set
     * Use this to setup the communication
     * @param type package type for network setup
     */
    public DataPackage(NetworkConfig.DataType type) {
        this.type = type;
    }

    public NetworkConfig.DataType getType() {
        return type;
    }

    public void setType(NetworkConfig.DataType type) {
        this.type = type;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public List<Object> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Object> answers) {
        this.answers = answers;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<Interactive.CardQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Interactive.CardQuestion> questions) {
        this.questions = questions;
    }

    public HandCard getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(HandCard currentCard) {
        this.currentCard = currentCard;
    }

    public EventCard getEventCard() {
        return eventCard;
    }

    public void setEventCard(EventCard eventCard) {
        this.eventCard = eventCard;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> p) {
        this.players = p;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public LogicAction getLogicAction() {
        return logicAction;
    }

    public void setLogicAction(LogicAction logicAction) {
        this.logicAction = logicAction;
    }

    public List<Object> getOffer() {
        return offer;
    }

    public void setOffer(List<Object> offer) {
        this.offer = offer;
    }

    /**
     * Actions for the Player
     */
    public enum PlayerAction {
        DRAW, PLACE, ANSWER, END_TURN
    }

    /**
     * Actions for the Logic
     */
    public enum LogicAction {
        REFRESH, QUESTION, END_ROUND, END_GAME
    }
}
