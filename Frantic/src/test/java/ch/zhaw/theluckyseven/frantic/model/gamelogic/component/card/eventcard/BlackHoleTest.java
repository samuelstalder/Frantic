package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlackHoleTest extends MockTestKit {

    /**
     * Test: actor should receive all 'black' cards.
     * 1. Check if actor has no cards
     * 2. 2 Victims have 5 black cards each.
     */
    @Test
    void actionSimpleRemove() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate players
        final int playerCount = 3;
        generatePlayers(playerCount);
        final int actorId = 0;
        final int victim1Id = 1;
        final int victim2Id = 2;

        //specify current player
        specifyCurrentPlayer(actorId);

        //specify played card
        BlackHole blackHole = (BlackHole) CardFactory.create(-1, Config.Cards.BLACK_HOLE);

        //manipulate player hands
        final int cardCountInHand = 5;
        int cardId = 0;
        for (int i = 0; i < cardCountInHand; i++) {
            getCardsOfPlayer(victim1Id).add((HandCard) CardFactory.create(cardId, Config.Cards.BLACK_ONE));
            cardId++;
        }
        for (int i = 0; i < cardCountInHand; i++) {
            getCardsOfPlayer(victim2Id).add((HandCard) CardFactory.create(cardId, Config.Cards.BLACK_ONE));
            cardId++;
        }
        //verify initial state
        assertTrue(gameStateController.getCurrentPlayer().getCards().isEmpty());
        assertEquals(cardCountInHand, getCardsOfPlayer(victim1Id).size());
        assertEquals(cardCountInHand, getCardsOfPlayer(victim2Id).size());

        //act
        assert blackHole != null;
        blackHole.action(gameStateController);

        //validate
        assertEquals((playerCount - 1) * cardCountInHand, gameStateController.getCurrentPlayer().getCardCount());
        assertTrue(getCardsOfPlayer(victim1Id).isEmpty());
        assertTrue(getCardsOfPlayer(victim2Id).isEmpty());
    }
}
