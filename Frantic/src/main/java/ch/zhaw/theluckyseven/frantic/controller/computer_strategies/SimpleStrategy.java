package ch.zhaw.theluckyseven.frantic.controller.computer_strategies;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;

import java.util.List;

/**
 * Class that implements the {@link ComputerStrategy} for the AI-Player
 * This Strategy is very basic and picks a card by random
 */
public class SimpleStrategy implements ComputerStrategy {

    /**
     * Method to choose a card to play
     *
     * @param state       the data to base your decision on
     * @param playOptions list of all the cards you are able to play
     * @param me          player representing yourself
     * @return the chosen card
     */
    @Override
    public HandCard chooseCard(DataPackage state, List<HandCard> playOptions, Player me) {
        HandCard chosenCard;
        if (playOptions.size() >= 2) {
            //choose random card
            chosenCard = playOptions.get(Config.random.nextInt(playOptions.size()));
        } else {
            //only one possible card
            chosenCard = playOptions.get(0);
        }
        return chosenCard;
    }
}
