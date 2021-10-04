package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.TestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.DeckFactory;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.Config.GameDuration.SHORT_GAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameFlowMockTest extends TestKit {
    private AutoCloseable closeable;
    @Mock
    private Server server;
    private GameFlowLogic gameFlowLogic;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Override
    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        server = null;
        gameFlowLogic = null;
        closeable.close();
    }

    /**
     * Test: On start-up player count should be valid.
     * 1. Pass List with 4 players.
     * 2. Check if "GameState" contains list of players of the same size.
     */
    @Test
    void gamePlayerCountOnStartUp() throws GameException {
        final int expectedPlayerCount = 4;
        generatePlayers(expectedPlayerCount);
        gameFlowLogic = new GameFlowLogic(server, SHORT_GAME, getNamesOfPlayers());
        GameState gameState = gameFlowLogic.getGameStateController().getGameState();
        assertEquals(expectedPlayerCount, gameState.getPlayerCount());
    }

}
