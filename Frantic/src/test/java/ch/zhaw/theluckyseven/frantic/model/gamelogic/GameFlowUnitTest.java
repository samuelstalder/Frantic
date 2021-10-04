package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.TestKit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFlowUnitTest extends TestKit {

    /**
     * Test:
     * "GameFlow.stop()" should cause: "Game.Flow.isRoundRunning()", "GameFlow.isTurnRunning" and "GameFlow.isGameRunning" to return false.
     */
    @Test
    void stopGame() {
        GameFlowLogic gfl = new GameFlowLogic();
        assertFalse(gfl.isGameStopped());
        gfl.stopGame();
        assertTrue(gfl.isGameStopped());
        assertFalse(gfl.isGameRunning());
        assertFalse(gfl.isTurnRunning());
        assertFalse(gfl.isRoundRunning());
    }

}
