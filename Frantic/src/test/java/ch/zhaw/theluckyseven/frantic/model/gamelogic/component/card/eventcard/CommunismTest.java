package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class CommunismTest extends MockTestKit {

    /**
     * Test: on cara action each player has the same amount of cards as the player with most cards.
     * 1. Set up:
     * 4 players: three of them have no cards, one has 7 cards.
     * 2. Act:
     * communism.action()
     * 3. Validation:
     * Each player has 7 cards.
     */
    @Test
    void actionSimple() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final int playerCount = 4;
        final int idOfPlayerWithMostCards = 2;
        final int cardCountOfPlayerWithMostCards = 7;
        //generate players
        generatePlayers(playerCount);
        //generate deck
        generateDeckOfHandCards();
        //generate cards
        //generate hand of player with 'idOfPlayerWithMostCards'
        for (int i = 0; i < cardCountOfPlayerWithMostCards; i++) {
            getCardsOfPlayer(idOfPlayerWithMostCards).add(getDeckOfHandCards().getCardFromDeckTop());

        }
        //verify each player except of player with id 'idOfPlayerWithMostCards' has zero cards
        for (Player p : getPlayers()) {
            if (p.getId() != idOfPlayerWithMostCards) {
                assertTrue(p.getCards().isEmpty());
            }
        }
        //generate card
        Communism communism = (Communism) CardFactory.create(0, Config.Cards.COMMUNISM);
        //assertTrue();
        communism.action(gameStateController);
        //validate each player except of player with id 'idOfPlayerWithMostCards' has zero cards
        for (Player p : getPlayers()) {
            if (p.getId() != idOfPlayerWithMostCards) {
                assertEquals(cardCountOfPlayerWithMostCards, p.getCards().size());
            }
        }
    }

    /**
     * Test: on cara action each player has the same amount of cards as the player with most cards.
     * 1. Set up:
     * 4 players: three of them have no cards, one has 7 cards.
     * empty the deck
     * 2. Act:
     * communism.action()
     * 3. Validation:
     * No exception thrown.
     * Flag is set: GameStateController.isDrawnFromEmptyCardsPile
     */
    @Test
    void actionOnEmptyDeck() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final int playerCount = 4;
        final int idOfPlayerWithMostCards = 2;
        final int cardCountOfPlayerWithMostCards = 7;
        //generate players
        generatePlayers(playerCount);
        //generate deck
        generateDeckOfHandCards();
        //generate cards
        //generate hand of player with 'idOfPlayerWithMostCards'
        for (int i = 0; i < cardCountOfPlayerWithMostCards; i++) {
            getCardsOfPlayer(idOfPlayerWithMostCards).add(getDeckOfHandCards().getCardFromDeckTop());

        }
        //empty deck
        getDeck().clear();
        //verify each player except of player with id 'idOfPlayerWithMostCards' has zero cards
        for (Player p : getPlayers()) {
            if (p.getId() != idOfPlayerWithMostCards) {
                assertTrue(p.getCards().isEmpty());
            }
        }
        assertTrue(gameState.getDeckOfHandCards().isEmpty());
        assertFalse(gameState.isDrawnFromEmptyCardsPile());
        //generate card
        Communism communism = (Communism) CardFactory.create(0, Config.Cards.COMMUNISM);

        communism.action(gameStateController);

        assertTrue(gameState.isDrawnFromEmptyCardsPile());
    }
}
