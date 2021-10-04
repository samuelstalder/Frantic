package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkipTest extends MockTestKit {
    /**
     * Test: correct list of players is offered.
     * 1. Set up:
     * generate game with 4 players: block actor and another player
     * 2. getOffer()
     * 3. Validate:
     * 'non-blocked' players are offered.
     */
    @Test
    void getOfferNonBlocked() throws Exception {
        final int idOfBlockedPlayer1 = 0;
        final int idOfBlockedPlayer2 = 3;
        final int idOfNonBlockedPlayer1 = 1;
        final int idOfNonBlockedPlayer2 = 2;
        //generate players
        generatePlayers(4);
        //block 2 players
        getPlayer(idOfBlockedPlayer1).block();
        getPlayer(idOfBlockedPlayer2).block();
        //verify blocked players
        assertTrue(getPlayer(idOfBlockedPlayer1).isBlocked());
        assertTrue(getPlayer(idOfBlockedPlayer2).isBlocked());
        //verify not blocked players
        assertFalse(getPlayer(idOfNonBlockedPlayer1).isBlocked());
        assertFalse(getPlayer(idOfNonBlockedPlayer2).isBlocked());
        //generate card
        Skip skip = (Skip) CardFactory.create(-1, Config.Cards.SKIP_GREEN);
        //act
        List<Object> offer = skip.getOffer(gameStateController);
        //validate size of offer
        assertEquals(2, offer.size());
        assertEquals(List.of(getPlayer(idOfNonBlockedPlayer1), getPlayer(idOfNonBlockedPlayer2)), offer);

    }
    /**
     * Test: correct list of players is offered.
     * 1. Set up:
     * generate game with 4 players: block first and last
     * actor is not blocked
     * 2. getOffer()
     * 3. Validate:
     * 'non-blocked' players and not an actor are offered.
     */
    @Test
    void getOfferNonBlockedNotActive() throws Exception {
        final int idOfBlockedPlayer1 = 0;
        final int idOfBlockedPlayer2 = 3;
        final int idOfActor = 1;
        final int idOfNonBlockedPlayer2 = 2;
        //generate players
        generatePlayers(4);

        specifyCurrentPlayer(idOfActor);
        //block 2 players
        getPlayer(idOfBlockedPlayer1).block();
        getPlayer(idOfBlockedPlayer2).block();
        //verify blocked players
        assertTrue(getPlayer(idOfBlockedPlayer1).isBlocked());
        assertTrue(getPlayer(idOfBlockedPlayer2).isBlocked());
        //verify not blocked players
        assertFalse(getPlayer(idOfActor).isBlocked());
        assertFalse(getPlayer(idOfNonBlockedPlayer2).isBlocked());
        //generate card
        Skip skip = (Skip) CardFactory.create(-1, Config.Cards.SKIP_GREEN);
        //act
        List<Object> offer = skip.getOffer(gameStateController);
        //validate size of offer
        assertEquals(1, offer.size());
        assertEquals(List.of(getPlayer(idOfNonBlockedPlayer2)), offer);
        //validate actor is not blocked
        assertFalse(getPlayer(idOfActor).isBlocked());
    }

    /**
     * Test: on action no other than wished player is blocked.
     * 1. Set up:
     * generate game with 4 players(not blocked)
     * 2.
     * set wished player in skip.setAnswer()
     * skip.action()
     * 3. Validate:
     * only wished player was blocked.
     */
    @Test
    void simpleAction() throws Exception {
        final int idOfPlayerExpectedToBeBlocked = 3;
        //generate players
        generatePlayers(4);
        //verify: no player is blocked
        for (Player p : getPlayers()) {
            assertFalse(p.isBlocked());
        }
        //generate card
        Skip skip = (Skip) CardFactory.create(-1, Config.Cards.SKIP_GREEN);
        assert skip != null;
        skip.setAnswer(List.of(getPlayer(idOfPlayerExpectedToBeBlocked)));
        skip.action(gameStateController);

        //validate: blocked player
        assertTrue(getPlayer(idOfPlayerExpectedToBeBlocked).isBlocked());
        //validate: no other player is blocked
        for (Player p : getPlayers()) {
            if (p.getId() != idOfPlayerExpectedToBeBlocked) {
                assertFalse(p.isBlocked());
            }
        }
    }
}
