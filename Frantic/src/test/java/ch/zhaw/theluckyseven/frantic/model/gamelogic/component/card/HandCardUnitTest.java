package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.Cards;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.ColorSwap;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.Skip;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.Thief;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.CardColor;
import static ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory.create;
import static org.junit.jupiter.api.Assertions.*;

class HandCardUnitTest {

    @Test
    void testBlueOnBlue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLUE_ONE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_FIVE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testBlueOnBlue2() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLUE_FIVE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_THREE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testGreenOnBlue2() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.GREEN_THREE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_THREE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void negativTestGreenOnBlue2() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.GREEN_FIVE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_THREE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertFalse(handCard.isPlayable());
    }


    @Test
    void testSkipOnBlue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        Skip handCard = (Skip) create(0, Cards.SKIP_BLUE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_FIVE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testBlueOnSkip() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLUE_ONE);
        Skip topCard = (Skip) create(1, Cards.SKIP_BLUE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testBlackOnRed() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLACK_TWO);
        NumeralCard topCard = (NumeralCard) create(1, Cards.RED_TWO);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testRedOnBlack() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.RED_TWO);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLACK_TWO);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testColorSwapOnGreen() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        ColorSwap handCard = (ColorSwap) create(0, Cards.COLOR_SWAP_GREEN_BLUE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.GREEN_THREE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void testColorSwapOnBlue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        ColorSwap handCard = (ColorSwap) create(0, Cards.COLOR_SWAP_GREEN_BLUE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_SEVEN);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void negativeTestColorSwapOnBlue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        ColorSwap handCard = (ColorSwap) create(0, Cards.COLOR_SWAP_RED_YELLOW);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_SEVEN);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertFalse(handCard.isPlayable());
    }

    @Test
    void testBlueOnColorSwap() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLUE_FIVE);
        ColorSwap topCard = (ColorSwap) create(1, Cards.COLOR_SWAP_GREEN_BLUE);
        topCard.setColor(CardColor.BLUE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void negativeTestBlueOnColorSwap() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        NumeralCard handCard = (NumeralCard) create(0, Cards.BLUE_FIVE);
        ColorSwap topCard = (ColorSwap) create(1, Cards.COLOR_SWAP_RED_YELLOW);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertFalse(handCard.isPlayable());
    }

    @Test
    void testThiefOnBlue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        Thief handCard = (Thief) create(0, Cards.THIEF_BLUE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.BLUE_FIVE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertTrue(handCard.isPlayable());
    }

    @Test
    void negativeTestThiefOnGreen() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        Thief handCard = (Thief) create(0, Cards.THIEF_BLUE);
        NumeralCard topCard = (NumeralCard) create(1, Cards.GREEN_THREE);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertFalse(handCard.isPlayable());
    }

    @Test
    void updatePlayableThiefOnColorSwap() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //generate 2 cards
        Thief handCard = (Thief) create(0, Cards.THIEF_BLUE);
        ColorSwap topCard = (ColorSwap) create(1, Cards.COLOR_SWAP_RED_YELLOW);
        topCard.setColor(CardColor.RED);
        //check initial state
        assertFalse(handCard.isPlayable());
        handCard.updatePlayable(topCard);
        //validate
        assertFalse(handCard.isPlayable());
    }

    @Test
    void equals() throws Exception {
        //same id's different types
        assertNotEquals(CardFactory.create(0, Cards.BLUE_FIVE), CardFactory.create(0, Cards.RED_EIGHT));
        assertNotEquals(CardFactory.create(0, Cards.SKIP_RED), CardFactory.create(0, Cards.RED_EIGHT));
        assertNotEquals(CardFactory.create(0, Cards.SKIP_RED), CardFactory.create(0, Cards.THIEF_RED));
        assertNotEquals(CardFactory.create(0, Cards.COLOR_SWAP_GREEN_BLUE), CardFactory.create(0, Cards.THIEF_RED));
    }

    @Test
    void colorRuleSatisfiedSameColor() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.RED_EIGHT);
        HandCard cardInPlayerHand = (HandCard) CardFactory.create(0, Cards.RED_NINE);
        assert cardInPlayerHand != null;
        assertTrue(cardInPlayerHand.colorRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void colorRuleSatisfiedDifferentColors() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.BLUE_SEVEN);
        HandCard cardInPlayerHand = (HandCard) CardFactory.create(0, Cards.RED_NINE);
        assert cardInPlayerHand != null;
        assertFalse(cardInPlayerHand.colorRuleSatisfied(cardOnTopOfDiscardPile));
    }

    /**
     * According to rules: 'Black' on 'Black' is not playable.
     */
    @Test
    void colorRuleSatisfiedBlackAndBlack() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.BLACK_FIVE);
        HandCard cardInPlayerHand = (HandCard) CardFactory.create(0, Cards.BLACK_TWO);
        assert cardInPlayerHand != null;
        assertFalse(cardInPlayerHand.colorRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void colorRuleSatisfiedColorSwapSameColor() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.COLOR_SWAP_GREEN_BLUE);
        cardOnTopOfDiscardPile.setColor(CardColor.GREEN);
        HandCard cardInPlayerHand = (HandCard) CardFactory.create(0, Cards.GREEN_EIGHT);
        assert cardInPlayerHand != null;
        assertTrue(cardInPlayerHand.colorRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void colorRuleSatisfiedColorSwapDifferentColor() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.COLOR_SWAP_GREEN_BLUE);
        cardOnTopOfDiscardPile.setColor(CardColor.BLUE);
        HandCard cardInPlayerHand = (HandCard) CardFactory.create(0, Cards.GREEN_EIGHT);
        assert cardInPlayerHand != null;
        assertFalse(cardInPlayerHand.colorRuleSatisfied(cardOnTopOfDiscardPile));
    }
}
