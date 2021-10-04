package ch.zhaw.theluckyseven.frantic.controller.computer_strategies;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;

import java.util.List;

/**
 * Strategy Pattern class for AI-Player strategy
 */
public interface ComputerStrategy {

    /**
     * Method to choose a card to play
     *
     * @param state       the data to base your decision on
     * @param playOptions list of all the cards you are able to play
     * @param me          player representing yourself
     * @return the chosen card
     */
    HandCard chooseCard(DataPackage state, List<HandCard> playOptions, Player me);
}
