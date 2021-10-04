package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.List;
import java.util.Map;

/**
 * GameState class that represents in which state the game currently is
 */
public class GameState {
    private int scoreToWinGame;
    private Deck<HandCard> deckOfHandCards;
    private Deck<EventCard> deckOfEventCards;
    private List<Player> players;
    private int currentPlayerId;
    private HandCard cardOnTopOfDiscardPile;
    private EventCard lastOpenedEventCard;
    private boolean drawnFromEmptyCardsPile;
    private int turnCounter = 0;
    private int roundCounter = 0;
    private Player roundLooser;
    private Player roundWinner;
    private Player gameWinner;
    private Map<String, Integer> gameScore;

    public int getScoreToWinGame() {
        return scoreToWinGame;
    }

    public void setScoreToWinGame(int scoreToWinGame) {
        this.scoreToWinGame = scoreToWinGame;
    }

    public Deck<HandCard> getDeckOfHandCards() {
        return deckOfHandCards;
    }

    public void setDeckOfHandCards(Deck<HandCard> deckOfHandCards) {
        this.deckOfHandCards = deckOfHandCards;
    }

    public Deck<EventCard> getDeckOfEventCards() {
        return deckOfEventCards;
    }

    public void setDeckOfEventCards(Deck<EventCard> deckOfEventCards) {
        this.deckOfEventCards = deckOfEventCards;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public List<HandCard> getPlayerCards(int playerId) {
        return players.get(playerId).getCards();
    }

    public HandCard getCardOnTopOfDiscardPile() {
        return cardOnTopOfDiscardPile;
    }

    public void setCardOnTopOfDiscardPile(HandCard cardOnTopOfDiscardPile) {
        this.cardOnTopOfDiscardPile = cardOnTopOfDiscardPile;
    }

    public EventCard getLastOpenedEventCard() {
        return lastOpenedEventCard;
    }

    public void setLastOpenedEventCard(EventCard lastOpenedEventCard) {
        this.lastOpenedEventCard = lastOpenedEventCard;
    }

    public boolean isDrawnFromEmptyCardsPile() {
        return drawnFromEmptyCardsPile;
    }

    public void setDrawnFromEmptyCardsPile(boolean drawnFromEmptyCardsPile) {
        this.drawnFromEmptyCardsPile = drawnFromEmptyCardsPile;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public void setRoundCounter(int roundCounter) {
        this.roundCounter = roundCounter;
    }

    public int getIdOfRoundLooser() {
        return roundLooser.getId();
    }


    public int getCardCountInDeckOfHandCards() {
        return deckOfHandCards.getCardCount();
    }

    public Config.CardColor getColorOfCardOnTopOfDiscardPile() {
        return cardOnTopOfDiscardPile.getColor();
    }

    void setWinnerOfRound(Player roundWinner) {
        this.roundWinner = roundWinner;
    }

    public void setLooserOfRound(Player roundLooser) {
        this.roundLooser = roundLooser;
    }

    public String getNameOfRoundWinner() {
        return roundWinner.getName();
    }

    public String getNameOfActivePlayer() {
        return getCurrentPlayer().getName();
    }

    void setGameWinner(Player gameWinner) {
        this.gameWinner = gameWinner;
    }

    public String getNameOfGameWinner() {
        return gameWinner.getName();
    }

    public boolean isCurrentPlayerBlocked() {
        return getCurrentPlayer().isBlocked();
    }

    public Map<String, Integer> getGameScore() {
        return gameScore;
    }

    void setGameScore(Map<String, Integer> gameScore) {
        this.gameScore = gameScore;
    }

    public String getNameOfRoundLooser() {
        return roundLooser.getName();
    }

    public boolean deckOfHandCardsIsEmpty() {
        return deckOfHandCards.isEmpty();
    }

    public HandCard getCardFromDeckTopOfDeckOfHandCards() {
        return deckOfHandCards.getCardFromDeckTop();
    }
}