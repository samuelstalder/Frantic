package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Third Time Lucky'.
 */
public class ThirdTimeLucky extends EventCard {
    public static final int CARDS_COUNT_TO_DRAW = 3;
    private static final Logger logger = Logger.getLogger(ThirdTimeLucky.class.getCanonicalName());

    public ThirdTimeLucky(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }

    /**
     * Every player has to draw three cards.
     *
     * @param gameStateController contains all the information needed for this event
     */
    @Override
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
        Deck<HandCard> deck = gameStateController.getGameState().getDeckOfHandCards();
        for (Player player : gameStateController.getGameState().getPlayers()) {
            for (int i = 0; i < CARDS_COUNT_TO_DRAW; ++i) {
                gameStateController.dealHandCard(player.getId());
                if (deck.isEmpty()) {
                    return;
                }
            }
        }
    }
}
