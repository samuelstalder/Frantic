package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorSwapTest extends MockTestKit {

    /**
     * Test: getOffer() of bi-colored card should return a list of it's both colors.
     */
    @Test
    void getOffer() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate card
        ColorSwap colorSwap = (ColorSwap) CardFactory.create(-1, Config.Cards.COLOR_SWAP_BLUE_YELLOW);
        //verify
        assertEquals(List.of(Config.CardColor.BLUE, Config.CardColor.YELLOW), colorSwap.getOffer(gameStateController));
    }

    /**
     * Test: bi-colored cord changes it's color according to the card on top of discard pile as wanted.
     * 1. Set up:
     * generate card 'COLOR_SWAP_BLUE_YELLOW'
     * set card on top of discard pile to blue-colored one
     * 2. Act
     * 3. Validate:
     * the color of color swat card is now blue
     */
    @Test
    void simpleAction() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, GameException {
        //generate card
        ColorSwap colorSwap = (ColorSwap) CardFactory.create(-1, Config.Cards.COLOR_SWAP_BLUE_YELLOW);
        gameState.setCardOnTopOfDiscardPile((HandCard) CardFactory.create(0, Config.Cards.BLUE_FOUR));
        //verify
        assert colorSwap != null;
        assertEquals(Config.CardColor.BLUE_YELLOW, colorSwap.getColor());
        assertEquals(Config.CardColor.BLUE, gameState.getColorOfCardOnTopOfDiscardPile());
        //act
        colorSwap.setAnswer(List.of(Config.CardColor.BLUE));
        colorSwap.action(gameStateController);
        //validate
        assertEquals(Config.CardColor.BLUE, colorSwap.getColor());
    }
}
