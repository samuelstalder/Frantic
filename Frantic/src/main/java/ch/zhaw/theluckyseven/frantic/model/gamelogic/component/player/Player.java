package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class representing a Player in the game
 * Player has a name, points, a hand of cards and other internal fields.
 */
public class Player implements Serializable {
    private static final Logger logger = Logger.getLogger(Player.class.getCanonicalName());
    private final String name;
    private final int id;
    private List<HandCard> cards = new ArrayList<>();
    private int score;
    private boolean blocked;
    private boolean hasDrawnCard;
    private boolean hasPlacedCard;

    /**
     * @param name     name of the player, given by user
     * @param playerId Is meant to be assigned by game class,
     */
    public Player(String name, int playerId) {
        this.name = name;
        this.id = playerId;
        score = 0;
        resetPlayerForNewRound();
    }

    /**
     * remove cards
     * Note: game score will not be reset.
     */
    public void resetPlayerForNewRound() {
        blocked = false;
        hasDrawnCard = false;
        hasPlacedCard = false;
        cards.clear();
    }

    /**
     * Returns card form players hand at specified index
     * @param index index of card in hand
     * @return the card at given index
     */
    public HandCard getCard(int index) {
        return cards.get(index);
    }

    /**
     * Get all cards in players hand
     * @return all cards in players hand
     */
    public List<HandCard> getCards() {
        return cards;
    }

    public void setCards(List<HandCard> cards) {
        this.cards = cards;
    }

    public int getCardCount() {
        return cards.size();
    }

    /**
     * Adds a card to the players hand
     * @param card card to add
     */
    public void takeCard(HandCard card) {
        cards.add(card);
    }

    /**
     * remove a card from the players hand
     *
     * @param card to remove
     * @return the card, that has been removed
     */
    public HandCard removeCard(HandCard card) {
        logger.info(String.format("REMOVED: %S from %S. Cards left: %d.", card.toString(),name,cards.size()));
        cards.remove(card);
        return card;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    /**
     * mark all cards, that can be played on the current card on the deck
     *
     * @param currentCard the card that is currently in the middle
     */
    public void markPlayableCards(HandCard currentCard) {
        for (HandCard card : cards) {
            card.updatePlayable(currentCard);
        }
    }

    /**
     * Method to block the player
     */
    public void block() {
        blocked = true;
        logger.info("Player " + name + " was BLOCKED.");
    }

    /**
     * Method to unlbock a player
     */
    public void unblock() {
        blocked = false;
        logger.info("Player " + name + " was UNBLOCKED.");
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setHasDrawnCard(boolean hasDrawnCard) {
        this.hasDrawnCard = hasDrawnCard;
    }

    public boolean hasDrawnCard() {
        return hasDrawnCard;
    }

    public void setHasPlacedCard(boolean hasPlacedCard) {
        this.hasPlacedCard = hasPlacedCard;
    }

    public boolean hasPlacedCard() {
        return hasPlacedCard;
    }

    /**
     * Returns the name of the Player
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * calculate the score of the player in the current round
     *
     * @return score of this player in this round
     */
    public int calculateRoundScore() {
        int roundScore = 0;
        for (HandCard c : cards) {
            roundScore += c.getValue();
        }
        return roundScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return this.getName().equals(((Player) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, score, blocked, hasDrawnCard, hasPlacedCard);
    }

    /**
     * Resets internal flags
     */
    public void resetTurnActionFlags() {
        hasPlacedCard = false;
        hasDrawnCard = false;
    }

    /**
     * Marks all cards
     */
    public void markCardsPlayable() {
        for (HandCard handCard : cards) {
            handCard.setNotPlayable();
        }
    }

    public void updateScore() {
        score += calculateRoundScore();
    }

    /**
     * Mehtod to check if the player is allowed to finish his turn
     * @return true if the player can finish the turn
     */
    public boolean canFinishTurn() {
        return hasDrawnCard || hasPlacedCard;
    }

    /**
     * Method to check if the player is allowed to draw a card
     * @return true if the player can draw a card
     */
    public boolean canDrawCard() {
        return !hasDrawnCard() && !hasPlacedCard;
    }
}
