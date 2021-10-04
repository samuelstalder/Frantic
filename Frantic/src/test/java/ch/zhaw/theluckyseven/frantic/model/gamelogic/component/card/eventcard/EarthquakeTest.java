package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EarthquakeTest extends MockTestKit {
    @Test
    void actionSimple() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final int playerCount = 3;
        //generate players
        generatePlayers(playerCount);
        //generate deck
        generateDeckOfHandCards();
        //generate cards
        HandCard cardOfPlayer1 = getDeckOfHandCards().getCardFromDeckTop();
        HandCard cardOfPlayer2 = getDeckOfHandCards().getCardFromDeckTop();
        HandCard cardOfPlayer3 = getDeckOfHandCards().getCardFromDeckTop();
        //manipulate decks
        getCardsOfPlayer(0).add(cardOfPlayer1);
        getCardsOfPlayer(1).add(cardOfPlayer2);
        getCardsOfPlayer(2).add(cardOfPlayer3);
        //verify
        assertTrue(getCardsOfPlayer(0).contains(cardOfPlayer1));
        assertTrue(getCardsOfPlayer(1).contains(cardOfPlayer2));
        assertTrue(getCardsOfPlayer(2).contains(cardOfPlayer3));
        //generate action cards
        Earthquake earthquake = (Earthquake) CardFactory.create(-1, Config.Cards.EARTHQUAKE);

        //validate
        assert earthquake != null;
        earthquake.action(gameStateController);
    }
}