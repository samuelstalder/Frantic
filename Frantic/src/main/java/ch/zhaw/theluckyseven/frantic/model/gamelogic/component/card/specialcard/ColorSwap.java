package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameException;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Interactive;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.SpecialCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for special card `Color Swap'.
 */
public class ColorSwap extends SpecialCard implements Interactive {
    private final List<Config.CardColor> cardColors = new ArrayList<>();
    private final CardQuestion[] question = new CardQuestion[]{CardQuestion.CHOOSE_COLOR};
    private final List<Config.CardColor> offer = new ArrayList<>();
    private Config.CardColor actualColor;
    private List<Object> answer;

    /**
     * Method to create a ColorSwap card
     *
     * @param name       name of the card
     * @param imageFront url to front image
     * @param imageBack  url to back image
     * @param id         id of the card
     * @param value      value/points of the card
     * @param color      color of the card
     */
    public ColorSwap(String name, String imageFront, String imageBack, int id, int value, Config.CardColor color) {
        super(name, imageFront, imageBack, id, value, color);
        setCardColors();
        offer.addAll(cardColors);
    }

    private void setCardColors() {
        String[] colors = this.getColor().name().split("_");
        for (String c : colors) {
            cardColors.add(Config.CardColor.valueOf(c));
        }
    }

    /**
     * Can  be  played  on  two  different  colours.
     * Hence,  it  changes from one colour to another.
     * (e.g. if the red-green 'Colour Swap' is played on green the game continues with red).
     * Note: can be only played on card which has matching color
     *
     * @param gameStateController contains all information needed from the game
     * @throws GameException thrown when color is null
     */
    @Override
    public void action(GameStateController gameStateController) throws GameException {
        parseAnswer();
        if (actualColor == null) {
            throw new GameException(GameException.GameError.CARD_CONTRACT_UNFULFILLED);
        }
        setColor(actualColor);
    }

    @Override
    public void updatePlayable(HandCard currentCard) {
        setNotPlayable();
        actualColor = currentCard.getColor();
        if (cardColors.contains(actualColor)) {
            setPlayable();
            updateOffer();
        }
    }

    /**
     * Method to update the offer to be sent to the clients
     */
    void updateOffer() {
        offer.clear();
        offer.addAll(cardColors);
        offer.remove(actualColor);
    }

    /**
     * Method to parse Answer from the clients
     */
    void parseAnswer() {
        actualColor = (Config.CardColor) answer.get(0);
    }

    @Override
    public boolean hasOpenQuestion() {
        return actualColor == null;
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
     *
     * @param gameStateController gameStateController
     * @return the list of colors actor can choose from.
     */
    public List<Object> getOffer(GameStateController gameStateController) {
        return new ArrayList<>(offer);
    }
}
