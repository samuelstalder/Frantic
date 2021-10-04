package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.Server;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import static ch.zhaw.theluckyseven.frantic.controller.ClientPackageFactory.create;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IntegrationTest extends MockTestKit {
    @Mock
    private Server server;
    @Spy
    private GameFlowLogic gameFlowLogic;
    private AutoCloseable closeable;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        gameFlowLogic = new GameFlowLogic();
        closeable = MockitoAnnotations.openMocks(this);
        gameFlowLogic.setServer(server);
        gameFlowLogic.setGameStateController(gameStateController);
    }

    @Override
    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        gameFlowLogic = null;
        closeable.close();
    }

    /**
     * Test: Two players, dealing round skipped, actor: player0, turn=1, places 'skip', chooses victim: player1,
     */
    @Test
    void skipTest() throws Exception {
        final int idOfActor = 0;
        final int idOfVictim = 1;
        //set up game state
        generatePlayers(2);
        generateDeckOfHandCards();
        int cardId = -2;
        HandCard actorCard1 = (HandCard) CardFactory.create(cardId--, Config.Cards.SKIP_BLUE);
        actorCard1.setOpened();
        HandCard actorCard2 = (HandCard) CardFactory.create(cardId--, Config.Cards.BLUE_FIVE);
        HandCard victimCard1 = (HandCard) CardFactory.create(cardId--, Config.Cards.RED_EIGHT);
        HandCard victimCard2 = (HandCard) CardFactory.create(cardId--, Config.Cards.SKIP_RED);
        //set up hand cards of fist player
        getCardsOfPlayer(idOfActor).add(actorCard1);
        getCardsOfPlayer(idOfActor).add(actorCard2);
        getCardsOfPlayer(idOfVictim).add(victimCard1);
        getCardsOfPlayer(idOfVictim).add(victimCard2);

        gameState.setCardOnTopOfDiscardPile((HandCard) CardFactory.create(-1, Config.Cards.BLUE_TWO));
        assertNotEquals(actorCard1, gameState.getCardOnTopOfDiscardPile());

        specifyCurrentPlayer(idOfVictim);

        DataPackage place = create(PLACE, actorCard1);
        DataPackage select = create(ANSWER, Lists.newArrayList(getPlayers().get(idOfVictim)));
        DataPackage endTurn = create(END_TURN);
        DataPackage place2 = create(PLACE,actorCard2);

        // skip dealer turn
        doNothing().when(gameFlowLogic).startRound();
        // specify sequence of answers of active player
        when(server.sendPackage(any(DataPackage.class))).thenReturn(place).thenReturn(select).thenReturn(endTurn).thenReturn(place2).thenReturn(endTurn);
        // after turn 1 game will stop
        doAnswer((Answer<Boolean>) invocation -> gameState.getTurnCounter() >= 2).when(gameStateController).isRoundEnded();

        //verify hands
        assertEquals(2, getCardsOfPlayer(idOfActor).size());
        assertEquals(2, getCardsOfPlayer(idOfVictim).size());
        assertTrue(getPlayer(idOfActor).getCards().contains(actorCard1));
        assertTrue(getPlayer(idOfActor).getCards().contains(actorCard2));
        assertTrue(getPlayer(idOfVictim).getCards().contains(victimCard1));
        assertTrue(getPlayer(idOfVictim).getCards().contains(victimCard2));

        gameFlowLogic.runRound();

        assertEquals(0, getCardsOfPlayer(idOfActor).size());
        assertEquals(2, getCardsOfPlayer(idOfVictim).size());
        assertEquals(actorCard2, gameState.getCardOnTopOfDiscardPile());
        assertEquals(actorCard2, gameState.getCardOnTopOfDiscardPile());
    }
}
