package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;

/**
 * Represents cards having some actions.
 */
public interface Active {

    /**
     * Execute action of the card.
     *
     * @param gameStateController controller object of the game state
     * @throws GameException if an error appears while active
     */
    void action(GameStateController gameStateController) throws GameException;

}
