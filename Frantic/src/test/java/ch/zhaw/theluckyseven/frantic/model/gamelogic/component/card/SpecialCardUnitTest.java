package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import org.junit.jupiter.api.Test;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.CardColor;
import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.Cards;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialCardUnitTest {
    @Test
    void symbolRuleSatisfied() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.COLOR_SWAP_GREEN_BLUE);
        SpecialCard cardInPlayerHand = (SpecialCard) CardFactory.create(0, Cards.COLOR_SWAP_RED_YELLOW);
        assert cardInPlayerHand != null;
        assertTrue(cardInPlayerHand.symbolRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void symbolRuleSatisfiedSameClass() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.SKIP_RED);
        SpecialCard cardInPlayerHand = (SpecialCard) CardFactory.create(0, Cards.SKIP_BLUE);
        assert cardInPlayerHand != null;
        assertTrue(cardInPlayerHand.symbolRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void symbolRuleSatisfiedDifferentClass() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.COLOR_SWAP_RED_YELLOW);
        cardOnTopOfDiscardPile.setColor(CardColor.YELLOW);
        SpecialCard cardInPlayerHand = (SpecialCard) CardFactory.create(0, Cards.THIEF_GREEN);
        assert cardInPlayerHand != null;
        assertFalse(cardInPlayerHand.symbolRuleSatisfied(cardOnTopOfDiscardPile));
    }

    @Test
    void symbolRuleSatisfiedDifferentClassSameColor() throws Exception {
        HandCard cardOnTopOfDiscardPile = (HandCard) CardFactory.create(0, Cards.COLOR_SWAP_GREEN_YELLOW);
        cardOnTopOfDiscardPile.setColor(CardColor.GREEN);
        SpecialCard cardInPlayerHand = (SpecialCard) CardFactory.create(0, Cards.THIEF_GREEN);
        assert cardInPlayerHand != null;
        assertFalse(cardInPlayerHand.symbolRuleSatisfied(cardOnTopOfDiscardPile));
    }
}
