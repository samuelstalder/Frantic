package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Abstract class of Handcards
 * All Handcards can be in a players hand
 * Handcards have a unique id
 */
public abstract class HandCard extends Card {
    private static final Logger logger = Logger.getLogger(HandCard.class.getCanonicalName());
    private final int id;
    private final int value;
    private Config.CardColor color;
    private boolean playable;

    /**
     * Creates a Handcard
     *
     * @param cardName   name of the card
     * @param imageFront url to front of the card
     * @param imageBack  url to the back of the card
     * @param id         id of the card
     * @param value      value/points of the card
     * @param color      color of the card
     */
    protected HandCard(String cardName, String imageFront, String imageBack, int id, int value, Config.CardColor color) {
        super(cardName, imageFront, imageBack);
        this.id = id;
        this.value = value;
        this.color = color;
        this.playable = false;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Config.CardColor getColor() {
        return color;
    }

    public void setColor(Config.CardColor color) {
        this.color = color;
    }

    public void setPlayable() {
        playable = true;
    }

    public void setNotPlayable() {
        playable = false;
    }

    public boolean isPlayable() {
        return playable;
    }

    /**
     * Mark the card, if can be played on the card on top of the discard pile.
     *
     * @param currentCard card on top of discard pile
     */
    public void updatePlayable(HandCard currentCard) {
        playable = colorRuleSatisfied(currentCard);
    }

    /**
     * @param currentCard card on top of discard pile
     * @return true, if the card can be played on the card given, according to 'color' rule
     */
    boolean colorRuleSatisfied(HandCard currentCard) {
        boolean result = false;
        switch (this.getColor()) {
            case ANY:
                result = true;
                break;
            case BLACK:
                break;
            case YELLOW:
            case BLUE:
            case RED:
            case GREEN:
                result = this.getColor().equals(currentCard.getColor());
                break;
            default:
                logger.severe("missing case in colorRuleSatisfied switch");
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandCard handCard = (HandCard) o;
        return id == handCard.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
