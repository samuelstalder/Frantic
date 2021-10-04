package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory.create;
import static org.junit.jupiter.api.Assertions.*;

class ThiefTest extends MockTestKit {

    /**
     * Test: cards are removed from 'victim', added to 'actor', cards of 'observer' stay untouched.
     * Test set up:
     * 1. 'actor' has no cards.
     * 2. 'victim' has 3 cards: two target cards and one non-target.
     * 3. 'observer' has 2 cards: two non-target cards.
     * <p>
     * 2.
     * Execute thief.action()
     * <p>
     * 3.
     * Verify:
     * 'actor' has 2 cards: two target cards from 'victim'.
     * 'victim' has 1 card: its non-target card.
     * 'observer' has 2 cards: its non-target cards.
     */
    @Test
    void actionSimpleRemoval() throws GameException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate players
        final int playerCount = 3;
        generatePlayers(playerCount);
        final int actorId = 0;
        final int victimId = 1;
        final int observerId = 2;

        //specify current player
        specifyCurrentPlayer(actorId);
        //specify played card
        int cardId = -1;
        Thief thief = (Thief) create(cardId, Config.Cards.THIEF_BLUE);

        //generate target cards
        HandCard targetCard1 = (HandCard) CardFactory.create(cardId--, Config.Cards.BLACK_ONE);
        HandCard targetCard2 = (HandCard) CardFactory.create(cardId--, Config.Cards.SKIP_BLUE);

        //add cards to the hand of victim
        getCardsOfPlayer(victimId).add(targetCard1);
        getCardsOfPlayer(victimId).add(targetCard2);

        //generate non-target card
        HandCard nonTargetCard1 = (HandCard) CardFactory.create(cardId--, Config.Cards.YELLOW_FIVE);
        //add non-target card in hand of victim
        getCardsOfPlayer(victimId).add(nonTargetCard1);

        //generate non-target card
        HandCard nonTargetCard2 = (HandCard) CardFactory.create(cardId--, Config.Cards.YELLOW_FIVE);
        HandCard nonTargetCard3 = (HandCard) CardFactory.create(cardId--, Config.Cards.SKIP_RED);
        //add non-target card in hand of observer
        getCardsOfPlayer(observerId).add(nonTargetCard2);
        getCardsOfPlayer(observerId).add(nonTargetCard3);

        //verify initial state
        //card count
        assertTrue(getPlayer(actorId).getCards().isEmpty());
        assertEquals(3, getCardsOfPlayer(victimId).size());
        assertEquals(2, getCardsOfPlayer(observerId).size());
        //verify card of victim
        assertTrue(getPlayer(victimId).getCards().contains(targetCard1));
        assertTrue(getPlayer(victimId).getCards().contains(targetCard2));
        assertTrue(getPlayer(victimId).getCards().contains(nonTargetCard1));
        //verify cards of observer
        assertTrue(getPlayer(observerId).getCards().contains(nonTargetCard2));
        assertTrue(getPlayer(observerId).getCards().contains(nonTargetCard3));

        //start test
        assert thief != null;
        assert targetCard1 != null;
        assert targetCard2 != null;
        thief.setAnswer(List.of(getPlayer(victimId), targetCard1, targetCard2));

        //act
        thief.action(gameStateController);

        //validate card count
        assertEquals(2, getCardsOfPlayer(actorId).size());
        assertEquals(1, getCardsOfPlayer(victimId).size());
        assertEquals(2, getCardsOfPlayer(observerId).size());

        //validate card of actor
        assertTrue(getPlayer(actorId).getCards().contains(targetCard1));
        assertTrue(getPlayer(actorId).getCards().contains(targetCard2));
        //validate card of victim
        assertTrue(getPlayer(victimId).getCards().contains(nonTargetCard1));
        //validate cards of observer
        assertTrue(getPlayer(observerId).getCards().contains(nonTargetCard2));
        assertTrue(getPlayer(observerId).getCards().contains(nonTargetCard3));
    }

    @Test
    void parseAnswerOneCardWished() throws Exception {
        generatePlayers(2);
        int cardId = -1;
        Thief thief = (Thief) create(cardId--, Config.Cards.THIEF_BLUE);
        HandCard dummyCard = (HandCard) CardFactory.create(cardId--, Config.Cards.BLUE_TWO);
        assert thief != null;
        thief.setAnswer(Lists.newArrayList(getPlayer(0),dummyCard));
        assertDoesNotThrow(()->thief.parseAnswer());
    }
}
