package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.SpecialCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Model class for special card `Skip'.
 */
public class Skip extends SpecialCard implements Interactive {
    private static final Logger logger = Logger.getLogger(Skip.class.getCanonicalName());
    private final CardQuestion[] question = new CardQuestion[]{CardQuestion.CHOOSE_PLAYER};
    private Player victim;
    private List<Object> answer;

    /**
     * Create a Skip card
     *
     * @param name       name of the card
     * @param imageFront url to front image
     * @param imageBack  url to back image
     * @param id         id of the card
     * @param value      value/points of the card
     * @param color      color of the card
     */
    public Skip(String name, String imageFront, String imageBack, int id, int value, Config.CardColor color) {
        super(name, imageFront, imageBack, id, value, color);
    }

    /**
     * The player of this card chooses a fellow player, who is suspended for one turn.
     * Important: A player can only be skipped again, after he already suspended his last turn.
     * Important: A player cannot punish himself.
     * Important: If no target player available, card can't be played.
     * Note: Not allowed to select a player who is already being skipped in THAT round.
     *
     * @param gameStateController contains all information needed from the game
     * @throws GameException if the victim is blocked
     */
    @Override
    public void action(GameStateController gameStateController) throws GameException {
        parseAnswer();
        verifyCardContract();
        logger.fine("\"Skip\" on player " + victim.getName() + " was activated.");
        victim.block();
    }

    private void verifyCardContract() throws GameException {
        if (hasOpenQuestion()) {
            throw new GameException(GameException.GameError.CARD_CONTRACT_UNFULFILLED);
        }
        if (victim.isBlocked()) {
            throw new GameException(GameException.GameError.CARD_CONTRACT_UNFULFILLED);
        }
    }

    /**
     * Method to parse Answer from the clients
     */
    void parseAnswer() {
        this.victim = (Player) answer.get(0);
    }

    @Override
    public boolean hasOpenQuestion() {
        return victim == null;
    }

    public CardQuestion[] getQuestion() {
        return question;
    }

    public void setAnswer(List<Object> answer) {
        this.answer = answer;
    }

    /**
     * Get offer to choose from.
     *
     * @param gameStateController gameStateController
     * @return the list of players actor can choose from.
     */
    @Override
    public List<Object> getOffer(GameStateController gameStateController) {
        List<Object> offer = new ArrayList<>();
        for (Player p : gameStateController.getGameState().getPlayers()) {
            if (!p.isBlocked() && !gameStateController.getCurrentPlayer().equals(p)) {
                offer.add(p);
            }
        }
        return offer;
    }
}
