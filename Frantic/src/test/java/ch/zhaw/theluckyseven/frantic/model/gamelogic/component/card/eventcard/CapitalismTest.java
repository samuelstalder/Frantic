package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class CapitalismTest extends MockTestKit {
    /**
     * 1. Set up:
     * 4 players with 1, 2, 3, 4 cards in hand
     * 2. act
     * 3. validate
     * players have 2, 4, 6, 8 cards in their hands respectively.
     */
    @Test
    void simpleAction() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final int playerCount = 4;
        //generate players
        generatePlayers(playerCount);
        //generate deck
        generateDeckOfHandCards();
        //manipulate hand cards
        for (int i = 0; i < getPlayers().size(); i++) {
            for (int k = 0; k < i + 1; k++) {
                getCardsOfPlayer(i).add(getDeckOfHandCards().getCardFromDeckTop());
            }
        }
        //verify hand cards size
        for (int i = 0; i < getPlayers().size(); i++) {
            assertEquals(i + 1, getCardsOfPlayer(i).size());
        }
        //generate card
        Capitalism capitalism = (Capitalism) CardFactory.create(-1, Config.Cards.CAPITALISM);
        //act
        capitalism.action(gameStateController);
        //validate cards count
        for (int i = 0; i < getPlayers().size(); i++) {
            assertEquals(2 * (i + 1), getCardsOfPlayer(i).size());
        }
    }

    /**
     * 1. Set up:
     * 4 players with 1, 2, 3, 4 cards in hand
     * deck is empty
     * 2. act
     * 3. validate
     * no exceptions
     * Flag is set: GameStateController.isDrawnFromEmptyCardsPile
     */
    @Test
    void simpleOnEmptyDeck() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final int playerCount = 4;
        //generate players
        generatePlayers(playerCount);
        //generate deck
        generateDeckOfHandCards();
        //manipulate hand cards
        for (int i = 0; i < getPlayers().size(); i++) {
            for (int k = 0; k < i + 1; k++) {
                getCardsOfPlayer(i).add(getDeckOfHandCards().getCardFromDeckTop());
            }
        }
        //empty deck
        getDeck().clear();

        //verify hand cards size
        for (int i = 0; i < getPlayers().size(); i++) {
            assertEquals(i + 1, getCardsOfPlayer(i).size());
        }
        assertTrue(gameState.getDeckOfHandCards().isEmpty());
        assertFalse(gameState.isDrawnFromEmptyCardsPile());
        //generate card
        Capitalism capitalism = (Capitalism) CardFactory.create(-1, Config.Cards.CAPITALISM);
        //act
        assertTrue(gameState.getDeckOfHandCards().isEmpty());
        capitalism.action(gameStateController);

        assertTrue(gameState.isDrawnFromEmptyCardsPile());
    }
}
