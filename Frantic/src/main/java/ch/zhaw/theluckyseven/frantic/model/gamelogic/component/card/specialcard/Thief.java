package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.SpecialCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException.GameError;
import static ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive.CardQuestion.CHOOSE_CARD;

/**
 * Model class for special card `Thief'.
 */
public class Thief extends SpecialCard implements Interactive {
    private final List<HandCard> cardsWished = new ArrayList<>();
    private final CardQuestion[] question = new CardQuestion[]{CardQuestion.CHOOSE_PLAYER, CHOOSE_CARD, CHOOSE_CARD};
    private Player actor;
    private Player victim;
    private List<Object> answer;

    /**
     * Creates a Thief card
     *
     * @param name       name of the card
     * @param imageFront url to front image
     * @param imageBack  url to back image
     * @param id         id of the card
     * @param value      value/points of the card
     * @param color      color of the card
     */
    public Thief(String name, String imageFront, String imageBack, int id, int value, Config.CardColor color) {
        super(name, imageFront, imageBack, id, value, color);
    }

    /**
     * The player of 'Thief' has to look into the cards of another player and steal two of his/her hand cards.
     *
     * @param gameStateController contains all information needed from the game
     * @throws GameException if there is already a question happening
     */
    @Override
    public void action(GameStateController gameStateController) throws GameException {
        actor = gameStateController.getCurrentPlayer();
        parseAnswer();
        verifyCardContract();
        for (HandCard card : cardsWished) {
            victim.removeCard(card);
            card.setNotPlayable();
            actor.takeCard(card);
        }
    }

    /**
     * Method to parse Answer from the clients
     */
    void parseAnswer() {
        this.victim = (Player) answer.get(0);
        this.cardsWished.add((HandCard) answer.get(1));
        if (answer.size() == 3 && answer.get(2) != null) {
            this.cardsWished.add((HandCard) answer.get(2));
        }
    }

    private void verifyCardContract() throws GameException {
        if (hasOpenQuestion()) {
            throw new GameException(GameError.CARD_CONTRACT_UNFULFILLED);
        }
    }

    @Override
    public boolean hasOpenQuestion() {
        return actor == null || victim == null || cardsWished.isEmpty();
    }

    @Override
    public CardQuestion[] getQuestion() {
        return question;
    }

    @Override
    public void setAnswer(List<Object> answer) {
        this.answer = answer;
    }

    /**
     * Get offer to choose from.
     * <p>
     * Note: only player will be given to choose from. Further actions(access to player cards) musst be done by actor.
     *
     * @param gameStateController gameStateController
     * @return the list of players actor can choose from.
     */
    @Override
    public List<Object> getOffer(GameStateController gameStateController) {
        List<Object> players = new ArrayList<>(gameStateController.getGameState().getPlayers());
        players.remove(gameStateController.getCurrentPlayer());
        return players;
    }
}
