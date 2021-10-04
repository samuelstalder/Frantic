package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.List;
import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Communism'.
 */
public class Communism extends EventCard {
    private static final Logger logger = Logger.getLogger(Communism.class.getCanonicalName());

    public Communism(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }


    /**
     * Everyone has to draw as many cards to equal the player,
     * who holds the most cards in his hand.
     *
     * @param gameStateController contains all the information needed for this event
     */
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
        List<Player> players = gameStateController.getGameState().getPlayers();
        int maxCardCardCount = calculateMaxCardCardCount(players);
        Deck<HandCard> deck = gameStateController.getGameState().getDeckOfHandCards();
        for (Player player : players) {
            if (player.getCardCount() < maxCardCardCount) {
                int drawn = maxCardCardCount - player.getCardCount();
                for (int i = 0; i < drawn; ++i) {
                    gameStateController.dealHandCard(player.getId());
                    if (deck.isEmpty()) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * @param players list of players
     * @return integer number, representing max card count in players hand
     */
    private int calculateMaxCardCardCount(List<Player> players) {
        int maxCardCardCount = 0;
        for (Player player : players) {
            int cardCount = player.getCardCount();
            if (cardCount > maxCardCardCount) {
                maxCardCardCount = cardCount;
            }
        }
        return maxCardCardCount;
    }
}
