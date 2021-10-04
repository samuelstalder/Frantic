package ch.zhaw.theluckyseven.frantic;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameState;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.PackageFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.when;

public class MockTestKit extends TestKit {
    @Spy
    protected GameStateController gameStateController;
    @Spy
    protected GameState gameState;
    private AutoCloseable closeable;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        closeable = MockitoAnnotations.openMocks(this);
        gameStateController.setGameState(gameState);
        PackageFactory.setGameStateController(gameStateController);
        PackageFactory.setGameState(gameState);
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        closeable.close();
    }

    /**
     * Generate players with no cards in hand(empty):
     * Note: gameState will be set up accordingly.
     *
     * @param playerCount player count to be generated
     */
    @Override
    protected void generatePlayers(int playerCount) {
        super.generatePlayers(playerCount);
        gameState.setPlayers(getPlayers());
    }

    @Override
    public void generateDeckOfHandCards() {
        super.generateDeckOfHandCards();
        gameState.setDeckOfHandCards(getDeck());
        gameState.setCardOnTopOfDiscardPile(getDeck().getCardFromDeckTop());
    }

    public void specifyCurrentPlayer(int actorId) {
        gameState.setCurrentPlayerId(actorId);
    }
}
