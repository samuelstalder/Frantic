package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ThirdTimeLuckTest extends MockTestKit {

    /**
     * Test: on card action each player get 3 card.
     * 1. Set up:
     * 3 players with zero cards each.
     * 2. Act: thirdTimeLucky.action
     */
    @Test
    void actionSimple() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate players
        final int playerCount = 4;
        generatePlayers(playerCount);
        //generate cards
        ThirdTimeLucky thirdTimeLucky = (ThirdTimeLucky) CardFactory.create(0, Config.Cards.THIRD_TIME_LUCKY);
        //generate deck
        generateDeckOfHandCards();

        // verify players have zero cards each
        for (Player p : getPlayers()) {
            assertTrue(p.getCards().isEmpty());
        }
        //act
        assert thirdTimeLucky != null;
        thirdTimeLucky.action(gameStateController);

        // validate card count
        for (Player p : getPlayers()) {
            assertEquals(ThirdTimeLucky.CARDS_COUNT_TO_DRAW, p.getCards().size());
        }
    }

    /**
     * Test: on card action each player get 3 card.
     * 1. Set up:
     * 3 players with zero cards each.
     * deck is empty
     * 2. Act: thirdTimeLucky.action
     * 3. validate:
     * deck is still empty
     * GameState.isDrawnFromEmptyCardsPile is true
     */
    @Test
    void actionOnEmptyDeck() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate players
        final int playerCount = 4;
        generatePlayers(playerCount);
        //generate cards
        ThirdTimeLucky thirdTimeLucky = (ThirdTimeLucky) CardFactory.create(0, Config.Cards.THIRD_TIME_LUCKY);
        //generate deck
        generateDeckOfHandCards();
        //clear deck
        getDeck().clear();
        // verify players have zero cards each
        for (Player p : getPlayers()) {
            assertTrue(p.getCards().isEmpty());
        }
        assertTrue(gameState.getDeckOfHandCards().isEmpty());
        assertFalse(gameState.isDrawnFromEmptyCardsPile());

        //act
        assert thirdTimeLucky != null;
        thirdTimeLucky.action(gameStateController);

        //validate
        assertTrue(gameState.getDeckOfHandCards().isEmpty());
        assertTrue(gameState.isDrawnFromEmptyCardsPile());

    }
}
