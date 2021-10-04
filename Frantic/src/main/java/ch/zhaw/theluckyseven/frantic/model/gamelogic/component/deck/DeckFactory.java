package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Card;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;

import java.util.logging.Logger;

/**
 * Factory class for the Deck
 * Creates a Deck with specified type
 */
public class DeckFactory {
    private static final Logger logger = Logger.getLogger(DeckFactory.class.getCanonicalName());
    private static int cardId = 0;

    /**
     * Creates a deck with the specified {@link DeckType}
     *
     * @param deckType type of the deck
     * @return the created deck with given type
     */
    public static Deck<? extends Card> create(DeckType deckType) {
        switch (deckType) {
            case EVENT_DECK:
                return createDeckOfEventCards();
            case HAND_CARD_DECK:
                return createDeckOfHandCards();
            default:
                throw new IllegalStateException("Unexpected value: " + deckType);
        }
    }

    private static Deck<EventCard> createDeckOfEventCards() {
        Deck<EventCard> deck = new Deck<>();
        for (Config.Cards card : Config.Cards.values()) {
            try {
                if (card.isEventCard()) {
                    for (int i = 0; i < card.getCount(); i++) {
                        deck.addCard((EventCard) CardFactory.create(cardId, card));
                        cardId++;
                    }
                }
            } catch (Exception e) {
                logger.severe("Something wrong with EventCard deck building...");
                e.printStackTrace();
            }
        }
        return deck;
    }

    private static Deck<HandCard> createDeckOfHandCards() {
        Deck<HandCard> deck = new Deck<>();
        for (Config.Cards card : Config.Cards.values()) {
            try {
                if (card.isHandCard()) {
                    for (int i = 0; i < card.getCount(); i++) {
                        deck.addCard((HandCard) CardFactory.create(cardId, card));
                        cardId++;
                    }
                }
            } catch (Exception e) {
                logger.severe("Something wrong with HandCard deck building...");
                e.printStackTrace();
            }
        }
        return deck;
    }

    /**
     * Decktypes
     */
    public enum DeckType {
        EVENT_DECK, HAND_CARD_DECK
    }
}