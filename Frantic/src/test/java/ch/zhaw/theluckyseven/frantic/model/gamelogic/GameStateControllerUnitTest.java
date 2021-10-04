package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.TestKit;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.CardFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.NumeralCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.Skip;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateControllerUnitTest extends TestKit {
    private GameStateController gameStateController;
    private GameState gameState;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        gameStateController = new GameStateController();
        gameStateController.setGameState(new GameState());
        gameState = gameStateController.getGameState();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        gameStateController = null;
        gameState = null;
    }

    @Override
    protected void generatePlayers(int playerCount) {
        super.generatePlayers(playerCount);
        gameState.setPlayers(getPlayers());
    }

    @Override
    protected void generateDeckOfHandCards() {
        super.generateDeckOfHandCards();
        gameState.setDeckOfHandCards(getDeck());
    }

    /**
     * Test: generated objects of 'Player' contain expected names.
     */
    @Test
    void createPlayers() throws GameException {
        List<String> givenNames = Arrays.asList("A", "B");
        gameStateController = new GameStateController(Config.GameDuration.SHORT_GAME, givenNames);
        gameState = gameStateController.getGameState();
        for (Player p : gameState.getPlayers()) {
            assertEquals(p.getName(), givenNames.get(p.getId()));
        }
    }

    /**
     * Test: winner of round should be the person with least score.
     * 1. Set up:
     * generate game with 2 players: one with no cards, another with 1 card in hand
     * 2. act
     * 3. Validate:
     * player with no card wins the round.
     */
    @Test
    void evaluateWinnerAndLooserOfRound() throws GameException {
        final String nameOfExpectedLooser = "B";
        final String nameOfExpectedWinner = "A";
        List<String> givenNames = Arrays.asList(nameOfExpectedLooser, nameOfExpectedWinner);
        gameStateController = new GameStateController(Config.GameDuration.SHORT_GAME, givenNames);
        gameState = gameStateController.getGameState();
        List<HandCard> cardsOfExpectedWinner = new ArrayList<>();
        List<HandCard> cardsOfExpectedLooser = Arrays.asList(new NumeralCard("", "", "", 0, 8, Config.CardColor.ANY));
        gameState.getPlayer(0).setCards(cardsOfExpectedLooser);
        gameState.getPlayer(1).setCards(cardsOfExpectedWinner);
        //act
        gameStateController.evaluateWinnerOfRound();
        gameStateController.evaluateLooserOfRound();
        //validate
        assertEquals(nameOfExpectedWinner, gameState.getNameOfRoundWinner());
        assertEquals(nameOfExpectedLooser, gameState.getNameOfRoundLooser());
    }

    @Test
    void placeCardOnTopOfDiscardPileNumeral() throws GameException {
        final String nameOfExpectedLooser = "B";
        final String nameOfExpectedWinner = "A";

        List<String> givenNames = Arrays.asList(nameOfExpectedLooser, nameOfExpectedWinner);
        gameStateController = new GameStateController(Config.GameDuration.SHORT_GAME, givenNames);
        gameState = gameStateController.getGameState();
        HandCard targetCard = new NumeralCard("", "", "", 0, 0, Config.CardColor.ANY);

        assertFalse(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

        gameStateController.placeCardOnTopOfDiscardPile(targetCard);

        assertEquals(targetCard, gameState.getCardOnTopOfDiscardPile());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

    }

    @Test
    void placeCardOnTopOfDiscardPileNumeralBlack() throws GameException {
        final String nameOfExpectedLooser = "B";
        final String nameOfExpectedWinner = "A";

        List<String> givenNames = Arrays.asList(nameOfExpectedLooser, nameOfExpectedWinner);
        gameStateController = new GameStateController(Config.GameDuration.SHORT_GAME, givenNames);
        gameState = gameStateController.getGameState();
        HandCard targetCard = new NumeralCard("", "", "", 0, 0, Config.CardColor.BLACK);

        assertFalse(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

        gameStateController.placeCardOnTopOfDiscardPile(targetCard);

        assertEquals(targetCard, gameState.getCardOnTopOfDiscardPile());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertTrue(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

    }

    @Test
    void placeCardOnTopOfDiscardPileActionCard() throws GameException {
        final String nameOfExpectedLooser = "B";
        final String nameOfExpectedWinner = "A";

        List<String> givenNames = Arrays.asList(nameOfExpectedLooser, nameOfExpectedWinner);
        gameStateController = new GameStateController(Config.GameDuration.SHORT_GAME, givenNames);
        gameState = gameStateController.getGameState();
        HandCard targetCard = new Skip("", "", "", 0, 0, Config.CardColor.RED);

        assertFalse(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

        gameStateController.placeCardOnTopOfDiscardPile(targetCard);

        assertEquals(targetCard, gameState.getCardOnTopOfDiscardPile());
        assertTrue(gameStateController.lastPlacedCardHasTriggeredActionInvocation());
        assertFalse(gameStateController.lastPlacedCardHasTriggeredEventInvocation());

    }

    /**
     * Test: switching to the next player works properly.
     * 1. Test set up:
     * Set active player.
     * Block two players in row.
     * 2. Act:
     * Call .switchToNextActivePlayer()
     * 3. Verify:
     * Next player is expected player.
     */
    @Test
    void switchToNextActivePlayer() {
        final int playerCount = 5;
        generatePlayers(playerCount);
        //specify active player
        final int idOfActivePlayer = 2;
        final int idOfNextExpectedPlayer = 4;
        gameState.setCurrentPlayerId(idOfActivePlayer);
        //block 2 players in row
        gameState.getPlayer(idOfNextExpectedPlayer - 1).block();
        gameState.getPlayer(idOfNextExpectedPlayer - 2).block();
        //verify player count
        assertEquals(playerCount, gameState.getPlayerCount());
        assertEquals(idOfActivePlayer, gameStateController.getCurrentPlayer().getId());
        assertTrue(gameState.getPlayer(idOfActivePlayer + 1).isBlocked());
        assertTrue(gameState.getPlayer(idOfActivePlayer + 1).isBlocked());
        //act
        gameStateController.switchToNextActivePlayer();
        //validate current player
        assertEquals(idOfNextExpectedPlayer, gameStateController.getCurrentPlayer().getId());
    }

    /**
     * Test: dealing from empty deck will mark (isRoundEnded()>true) the end of round
     * 1. Set up:
     * 2 player: each has one card
     * empty deck of hand cards
     * 2. Act: deal card for current player
     * 3. Validate: isRoundEnded() returns true
     */
    @Test
    void dealHandCardFromEmptyDeckEndRound() throws Exception {
        final int playerCount = 2;
        generatePlayers(playerCount);
        int idOfCard = 0;
        getCardsOfPlayer(0).add((HandCard) CardFactory.create(idOfCard--, Config.Cards.BLUE_TWO));
        getCardsOfPlayer(1).add((HandCard) CardFactory.create(idOfCard--, Config.Cards.BLUE_TWO));
        //generate empty deck
        generateDeckOfHandCards();
        getDeck().clear();
        //verify
        assertFalse(gameStateController.isRoundEnded());
        //act
        gameStateController.dealHandCardToActivePlayer();
        //validate
        assertTrue(gameStateController.isRoundEnded());
    }

    /**
     * Test: placement of card which causes empty hand of cards will mark (isRoundEnded()>true) the end of round
     * 1. Set up:
     * 2 player: each has one card
     * player one has one playable card
     * 2. Act: place last card from hand of player one
     * 3. Validate: isRoundEnded() returns true
     */
    @Test
    void executeCardPlacementCausesPlayerWithNoCardsEndRound() throws Exception {
        final int playerCount = 2;
        final int idOfPlayerOne = 0;
        final int idOfPlayerTwo = 1;
        generatePlayers(playerCount);
        //generate cards
        int idOfCard = 0;
        HandCard cardOfPlayerOne = (HandCard) CardFactory.create(idOfCard--, Config.Cards.BLUE_TWO);
        cardOfPlayerOne.setPlayable();
        HandCard cardOfPlayerTwo = (HandCard) CardFactory.create(idOfCard--, Config.Cards.RED_NINE);
        getCardsOfPlayer(idOfPlayerOne).add(cardOfPlayerOne);
        getCardsOfPlayer(idOfPlayerTwo).add(cardOfPlayerTwo);
        //generate empty deck
        generateDeckOfHandCards();
        getDeck().clear();
        //verify
        assertFalse(gameStateController.isRoundEnded());

        assertEquals(1, getCardsOfPlayer(idOfPlayerOne).size());
        assertTrue(getCardsOfPlayer(idOfPlayerOne).contains(cardOfPlayerOne));

        //act
        gameStateController.executeCardPlacement(getCardsOfPlayer(idOfPlayerOne).get(0));
        //validate
        assertTrue(gameStateController.isRoundEnded());
    }
}
