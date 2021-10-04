package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Black hole'.
 */
public class BlackHole extends EventCard {
    private static final Logger logger = Logger.getLogger(BlackHole.class.getCanonicalName());

    public BlackHole(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }

    /**
     * All Black Cards in the hands of the players go to the player of the Black Card.
     *
     * @param gameStateController contains all the information needed for this event
     */
    @Override
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
        List<HandCard> blackCards = new ArrayList<>();
        for (Player player : gameStateController.getGameState().getPlayers()) {
            HandCard card;
            Iterator<HandCard> iterator = player.getCards().iterator();
            while (iterator.hasNext()) {
                card = iterator.next();
                if (Config.CardColor.BLACK.equals(card.getColor())) {
                    blackCards.add(card);
                    iterator.remove();
                }
            }
        }
        if (!blackCards.isEmpty()) {
            for (HandCard card : blackCards) {
                gameStateController.getCurrentPlayer().takeCard(card);
            }
        }
    }
}
