package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Capitalism'.
 */
public class Capitalism extends EventCard {
    private static final Logger logger = Logger.getLogger(Capitalism.class.getCanonicalName());

    public Capitalism(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }

    /**
     * All players double their hand cards.
     *
     * @param gameStateController contains all the information needed for this event
     */
    @Override
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
        Deck<HandCard> deck = gameStateController.getGameState().getDeckOfHandCards();

        for (Player player : gameStateController.getGameState().getPlayers()) {
            int cardCount = player.getCardCount();
            for (int i = 0; i < cardCount; i++) {
                gameStateController.dealHandCard(player.getId());
                if (deck.isEmpty()) {
                    return;
                }
            }
        }
    }
}

