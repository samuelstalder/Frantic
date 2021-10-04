package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;

import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Friday The 13th'.
 */
public class FridayThe13th extends EventCard {
    private static final Logger logger = Logger.getLogger(FridayThe13th.class.getCanonicalName());

    public FridayThe13th(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }


    /**
     * The game round continues without further ado.
     *
     * @param gameStateController contains all the information needed for this event
     */
    @Override
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
    }
}
