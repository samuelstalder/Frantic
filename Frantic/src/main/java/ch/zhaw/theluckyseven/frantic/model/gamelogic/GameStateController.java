package ch.zhaw.theluckyseven.frantic.model.gamelogic;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Active;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.DeckFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class that controls the GameState and triggers events of cards
 */
public class GameStateController {
    private static final Logger logger = Logger.getLogger(GameStateController.class.getCanonicalName());
    private final Config.GameDuration gameDuration;
    private final List<Integer> idOfSkippedPlayerThisTurn = new ArrayList<>();
    private final int gameOwnerId = 0;
    private GameState gameState;
    private boolean eventTriggered;
    private boolean actionTriggered;

    /**
     * @param gameDuration defines how long the game should take
     * @param players      list containing player names.
     *                     NOTE:   The order of the names in the list,
     * @throws GameException if the game is stopped unexpectedly (ex. user disconnected)
     */
    public GameStateController(Config.GameDuration gameDuration, List<String> players) throws GameException {
        logger.fine("Setting up game ...");
        gameState = new GameState();

        if (players.size() > Config.MAX_PLAYERS) {
            throw new GameException(GameException.GameError.MAX_PLAYERS);
        } else if (players.size() < Config.MIN_PLAYERS) {
            throw new GameException(GameException.GameError.NOT_ENOUGH_PLAYERS);
        }

        logger.info("NUMBER PLAYERS: " + players.size());
        createPlayers(players);

        this.gameDuration = gameDuration;
        logger.info("GAME MODE: " + gameDuration);

        int scoreToWinGame = gameDuration.getMaxPointsByPlayerCount(players.size());
        gameState.setScoreToWinGame(scoreToWinGame);
        logger.info("GOAL POINTS TO WIN: " + scoreToWinGame);

        gameState.setDrawnFromEmptyCardsPile(false);
    }

    /**
     * For testing purposes
     */
    GameStateController() {
        gameDuration = Config.GameDuration.LONG_GAME;
    }

    void createPlayers(List<String> playerNickNames) {
        List<Player> players = new ArrayList<>();
        int playerId = 0;
        for (String playerNickName : playerNickNames) {
            players.add(new Player(playerNickName, playerId));
            playerId++;
        }
        gameState.setPlayers(players);
    }

    public Player getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }

    boolean currentPlayerCanDrawCard() {
        return getCurrentPlayer().canDrawCard();
    }

    boolean currentPlayerCanPlaceCard() {
        return !getCurrentPlayer().hasPlacedCard();
    }

    /**
     * @return true, when either card was placed, or card was drawn.
     */
    public boolean currentPlayerCanFinishTurn() {
        return getCurrentPlayer().canFinishTurn();
    }

    /**
     * Deals handCards to the current player
     */
    public void dealHandCardToActivePlayer() {
        dealHandCard(gameState.getCurrentPlayerId());
    }

    /**
     * Note: May also be used in case if some other than current player hast to draw a card.
     * Note: if player is the player in turn, the function will also set 'turnUnfinished' flag to false.
     *
     * @param playerId id of the player who has to take a card
     */
    public void dealHandCard(int playerId) {
        Player player = gameState.getPlayers().get(playerId);
        HandCard card;
        if (!gameState.deckOfHandCardsIsEmpty()) {
            card = gameState.getCardFromDeckTopOfDeckOfHandCards();
            card.updatePlayable(gameState.getCardOnTopOfDiscardPile());
            card.setOpened();
            player.takeCard(card);
        } else {
            gameState.setDrawnFromEmptyCardsPile(true);
        }
        player.setHasDrawnCard(true);
    }

    /**
     * Remove given card from the player hand.
     * Update card on top.
     * Execute action if the given card is special or an numeral card(black).
     * Update flag.
     * <p>
     * Note: card musst be 'playable' in order to be accepted
     *
     * @param playedCard card to be played
     */
    public void executeCardPlacement(HandCard playedCard) {
        if (playedCard.isPlayable()) {
            placeCardOnTopOfDiscardPile(playedCard);
            updatePlayerAfterCardPlacement(playedCard);
        }
    }

    void placeCardOnTopOfDiscardPile(HandCard playedCard) {
        setCardOnTopOfDiscardPile(playedCard);
        updateCardTriggerFlags(playedCard);
    }

    private void updatePlayerAfterCardPlacement(HandCard playedCard) {
        getCurrentPlayer().removeCard(playedCard);
        getCurrentPlayer().setHasPlacedCard(true);
        getCurrentPlayer().markCardsPlayable();
    }

    private void updateCardTriggerFlags(HandCard playedCard) {
        if (Config.CardColor.BLACK.equals(playedCard.getColor())) {
            eventTriggered = true;
        }
        if (playedCard instanceof Active) {
            actionTriggered = true;
        }
    }

    /**
     * Method to invoke an event card
     */
    public void openEventCard() {
        gameState.setLastOpenedEventCard(gameState.getDeckOfEventCards().getCardFromDeckTop());
        logger.info("EVENT triggered: " + gameState.getLastOpenedEventCard());
    }

    void playLastPlacedPlayingCard(List<Object> answers) throws GameException {
        HandCard card = gameState.getCardOnTopOfDiscardPile();
        if (card instanceof Interactive) {
            remapPlayers(answers);
            ((Interactive) card).setAnswer(answers);
            ((Active) card).action(this);
        } else if (card instanceof Active) {
            ((Active) card).action(this);
        }
    }

    /**
     * Remap all 'Player' received in 'answer' on 'internal' Player
     *
     * @param answers answer with 'remaped' players
     */
    private void remapPlayers(List<Object> answers) {
        for (int i = 0; i < answers.size(); i++) {
            Object answer = answers.get(i);
            if (answer != null && Player.class.equals(answer.getClass())) {
                int playerId = ((Player) answer).getId();
                answers.set(i, gameState.getPlayer(playerId));
            }
        }
    }

    /**
     * Execute the Event
     */
    public void playLastOpenedEventCard() {
        try {
            gameState.getLastOpenedEventCard().action(this);
        } catch (GameException e) {
            logger.severe("A wild error appeared while executing an event");
            e.printStackTrace();
        }
    }

    /**
     * Method to check if game is ended
     *
     * @return true if the game is ended
     */
    public boolean isGameEnded() {
        return isWinScoreAchieved();
    }

    boolean isWinScoreAchieved() {
        for (Player player : gameState.getPlayers()) {
            if (player.getScore() >= gameState.getScoreToWinGame()) {
                return true;
            }
        }
        return false;
    }

    /**
     * The game round is over as soon as:
     * - a player gets rid of all his hand cards
     * - the card deck is used up and a player would have to draw one or more cards
     * NOTE:
     * The current round will not be over, until the event has been executed completely.
     *
     * @return true, if round has finished
     */
    public boolean isRoundEnded() {
        return hasPlayerWithNoCardsLeft() || drawnFromEmptyPileOfHandCards();
    }

    /**
     * Method to update points of every player
     */
    public void updateGameScore() {
        for (Player player : gameState.getPlayers()) {
            player.updateScore();
        }
    }


    boolean hasPlayerWithNoCardsLeft() {
        for (Player player : gameState.getPlayers()) {
            if (player.getCardCount() == 0) {
                return true;
            }
        }
        return false;
    }

    boolean drawnFromEmptyPileOfHandCards() {
        return gameState.isDrawnFromEmptyCardsPile();
    }

    /**
     * starts a new Turn
     */
    public void startTurn() {
        updateTurnCounter();
        switchToNextActivePlayer();
        getCurrentPlayer().resetTurnActionFlags();
        markPlayableCards();
        updateIsBlockedStatus();
    }

    private void updateTurnCounter() {
        gameState.setTurnCounter(gameState.getTurnCounter() + 1);
        logger.info("TURN: #" + gameState.getTurnCounter());
    }

    /**
     * Method to set next Player as active player
     */
    public void switchToNextActivePlayer() {
        int playerId = gameState.getCurrentPlayerId();
        int playerCount = gameState.getPlayerCount();
        playerId++;
        playerId %= playerCount;
        gameState.setCurrentPlayerId(playerId);
        while (gameState.isCurrentPlayerBlocked()) {
            idOfSkippedPlayerThisTurn.add(playerId);
            playerId++;
            playerId %= playerCount;
            gameState.setCurrentPlayerId(playerId);
        }
        logger.info("NEXT PLAYER is player " + getCurrentPlayer().getName() + ".");
    }

    /**
     * Method to mark playable cards on top of current card
     */
    public void markPlayableCards() {
        for (Player player : gameState.getPlayers()) {
            player.markPlayableCards(gameState.getCardOnTopOfDiscardPile());
        }
    }

    void updateIsBlockedStatus() {
        logger.fine("checking blocks...");
        for (Integer id : idOfSkippedPlayerThisTurn) {
            gameState.getPlayer(id).unblock();
        }
        idOfSkippedPlayerThisTurn.clear();
    }

    /**
     * Method to end turn of current player
     */
    public void endTurn() {
        getCurrentPlayer().resetTurnActionFlags();
        resetCardTriggerFlags();
    }

    private void resetCardTriggerFlags() {
        eventTriggered = false;
        actionTriggered = false;
    }

    /**
     * 1. deal initial number of cards to each player.
     * 2. open top card.
     * 3. set initial player
     * 4. mark playable cards in his hand
     * Note:
     * - In the first round, the owner of the game is the dealer.
     * - In the following  rounds,
     * the  person  who  lost  the  last  round  –  i.e.  who scored the most points – becomes the dealer.
     */
    public void startRound() {
        logger.fine("initiating game round ...");
        updateRoundCounter();
        resetTurnCounter();
        initializeDecks();
        resetCardsOnTopOfCardPiles();
        shuffleDecks();
        initializePlayers();
        initializeRoundDealer();
        dealInitialHand();
        setCardOnTopOfDiscardPile(gameState.getCardFromDeckTopOfDeckOfHandCards());
        updateCardTriggerFlags(gameState.getCardOnTopOfDiscardPile());
    }

    private void updateRoundCounter() {
        gameState.setRoundCounter(gameState.getRoundCounter() + 1);
        logger.info("ROUND #" + gameState.getRoundCounter());
    }

    private void resetTurnCounter() {
        gameState.setTurnCounter(0);
        logger.info("TURN #" + gameState.getRoundCounter());
    }

    private void resetCardsOnTopOfCardPiles() {
        gameState.setCardOnTopOfDiscardPile(gameState.getDeckOfHandCards().peekCardFromDeckTop());
        gameState.setLastOpenedEventCard(gameState.getDeckOfEventCards().peekCardFromDeckTop());
    }

    private void initializeDecks() {
        gameState.setDeckOfHandCards((Deck<HandCard>) DeckFactory.create(DeckFactory.DeckType.HAND_CARD_DECK));
        gameState.setDeckOfEventCards((Deck<EventCard>) DeckFactory.create(DeckFactory.DeckType.EVENT_DECK));
    }

    private void shuffleDecks() {
        gameState.getDeckOfHandCards().shuffle();
        gameState.getDeckOfEventCards().shuffle();
    }

    /**
     * Prepare player for round start.
     */
    private void initializePlayers() {
        for (Player player : gameState.getPlayers()) {
            player.resetPlayerForNewRound();
        }
    }

    private void initializeRoundDealer() {
        assignDealer();
        Player dealer = getCurrentPlayer();
        dealer.setHasPlacedCard(true);
        dealer.setHasDrawnCard(true);
        logger.info("ROUND DEALER is player ." + gameState.getCurrentPlayer().getName());
    }

    private void assignDealer() {
        if (gameState.getRoundCounter() == 0) {
            gameState.setCurrentPlayerId(gameState.getIdOfRoundLooser());
        } else {
            gameState.setCurrentPlayerId(gameOwnerId);
        }
    }

    /**
     * Deal initial number of cards to each player.
     */
    void dealInitialHand() {
        logger.fine("dealing cards...");
        final int cardCount = gameDuration.getInitialCardCountInPlayerHand();
        for (Player player : gameState.getPlayers()) {
            for (int i = 0; i < cardCount; i++) {
                HandCard card = gameState.getCardFromDeckTopOfDeckOfHandCards();
                card.setOpened();
                player.takeCard(card);
            }
        }
    }

    private void setCardOnTopOfDiscardPile(HandCard cardFromDeckTop) {
        gameState.setCardOnTopOfDiscardPile(cardFromDeckTop);
        gameState.getCardOnTopOfDiscardPile().setOpened();
        logger.info("CARD ON TOP of discard pile: " + gameState.getCardOnTopOfDiscardPile().toString().toUpperCase());
    }

    /**
     * Method to end round
     */
    public void endRound() {
        gameState.setLooserOfRound(null);
        updateGameScore();
        gameState.setDrawnFromEmptyCardsPile(false);
        evaluateLooserOfRound();
        evaluateWinnerOfRound();
    }

    /**
     * Method to evaluate Looser of the round
     */
    public void evaluateLooserOfRound() {
        Player playerWithHighestScore = gameState.getPlayer(gameOwnerId);
        for (Player player : gameState.getPlayers()) {
            if (playerWithHighestScore.calculateRoundScore() <= player.calculateRoundScore()) {
                playerWithHighestScore = player;
            }
        }
        gameState.setLooserOfRound(playerWithHighestScore);
    }

    /**
     * Method to evaluate winner of the round
     */
    public void evaluateWinnerOfRound() {
        Player playerWithLowestScore = gameState.getPlayer(gameOwnerId);
        for (Player player : gameState.getPlayers()) {
            if (playerWithLowestScore.calculateRoundScore() > player.calculateRoundScore()) {
                playerWithLowestScore = player;
            }
        }
        gameState.setWinnerOfRound(playerWithLowestScore);
    }

    /**
     * Method to end the game
     */
    public void endGame() {
        evaluateGameScore();
        evaluateGameWinner();
    }

    /**
     * Method to evaluate the game score
     */
    public void evaluateGameScore() {
        Map<String, Integer> score = new HashMap<>();
        for (Player player : gameState.getPlayers()) {
            score.put(player.getName(), player.getScore());
        }
        gameState.setGameScore(score);
    }

    /**
     * Method to evaluate the winner of the game
     */
    public void evaluateGameWinner() {
        Player playerWithLowestScore = gameState.getPlayer(gameOwnerId);
        for (Player player : gameState.getPlayers()) {
            if (playerWithLowestScore.getScore() > player.getScore()) {
                playerWithLowestScore = player;
            }
        }
        gameState.setGameWinner(playerWithLowestScore);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean lastPlacedCardHasTriggeredEventInvocation() {
        return eventTriggered;
    }

    public boolean lastPlacedCardHasTriggeredActionInvocation() {
        return actionTriggered;
    }
}