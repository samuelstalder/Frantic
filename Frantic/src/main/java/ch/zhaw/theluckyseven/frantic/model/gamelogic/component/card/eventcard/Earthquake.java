package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Model class representing 'event' card 'Earthquake'.
 */
public class Earthquake extends EventCard {
    private static final Logger logger = Logger.getLogger(Earthquake.class.getCanonicalName());

    public Earthquake(String name, String imageFront, String imageBack) {
        super(name, imageFront, imageBack);
    }

    /**
     * Every player gives his cards to the player to his right.
     *
     * @param gameStateController contains all the information needed for this event
     */
    @Override
    public void action(GameStateController gameStateController) {
        logger.fine("executing event: " + this);
        List<List<HandCard>> cards = new ArrayList<>();

        for (int i = 0; i < gameStateController.getGameState().getPlayerCount(); i++) {
            cards.add(gameStateController.getGameState().getPlayerCards(i));
        }

        for (int i = 0; i < gameStateController.getGameState().getPlayerCount(); i++) {
            gameStateController.getGameState().getPlayer(i).setCards(cards.get((i + 1) % gameStateController.getGameState().getPlayerCount()));
        }
    }
}
