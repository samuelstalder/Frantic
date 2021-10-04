package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class NumeralCardUnitTest {
    @Test
    void equalsSameClassDifferentId() throws Exception {
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(1, Config.Cards.BLUE_FIVE));
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(1, Config.Cards.BLUE_SEVEN));
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(1, Config.Cards.RED_EIGHT));
    }

    @Test
    void equalsSameClassSameIdSameNumberSameColor() throws Exception {
        assertEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(0, Config.Cards.BLUE_FIVE));

    }

    @Test
    void equalsSameClassSameIdDifferentColor() throws Exception {
        assertEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(0, Config.Cards.BLUE_FIVE));
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(0, Config.Cards.BLACK_FIVE));
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(0, Config.Cards.BLUE_SEVEN));
    }

    @Test
    void equalsDifferentTypes() throws Exception {
        assertNotEquals(CardFactory.create(0, Config.Cards.BLUE_FIVE), CardFactory.create(0, Config.Cards.COLOR_SWAP_GREEN_BLUE));
    }
}
