package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck class representing a Deck of Cards
 *
 * @param <T> type of cards
 */
public class Deck<T extends Card> {

    private final List<T> cards = new ArrayList<>();

    /**
     * @return returns all the cards of the deck
     */
    public List<T> getCards() {
        return cards;
    }

    /**
     * Adds a card to the deck
     *
     * @param card the card to add
     */
    public void addCard(T card) {
        cards.add(card);
    }

    /**
     * Returns the amount of cards
     *
     * @return the amount of cards in the deck
     */
    public int getCardCount() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Shuffles all the cards
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Removes and returns to topmost card on the deck
     *
     * @return the card on top of the deck
     */
    public T getCardFromDeckTop() {
        cards.get(0).setOpened();
        return cards.remove(0);
    }

    /**
     * Looks at the topmost card on the deck without removing it
     *
     * @return the card on top of the deck
     */
    public T peekCardFromDeckTop() {
        return cards.get(0);
    }

    /**
     * clears the deck
     */
    public void clear() {
        cards.clear();
    }
}
