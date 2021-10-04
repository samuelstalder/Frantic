package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;

/**
 * Abstract class for all Handcards without a number
 */
public abstract class SpecialCard extends HandCard {

    /**
     * Creates a SpecialCard
     *
     * @param cardName   name of the card
     * @param imageFront url to front image
     * @param imageBack  url to back image
     * @param id         id of the card
     * @param value      value of the card
     * @param color      color of the card
     */
    protected SpecialCard(String cardName, String imageFront, String imageBack, int id, int value, Config.CardColor color) {
        super(cardName, imageFront, imageBack, id, value, color);
    }

    /**
     * The colourful Special Cards can be played on any card.
     * The coloured Special Cards can only be played on said colour or symbol
     *
     * @param currentCard the current card
     */
    @Override
    public void updatePlayable(HandCard currentCard) {
        super.updatePlayable(currentCard);
        if (!isPlayable() && symbolRuleSatisfied(currentCard)) {
            setPlayable();
        }
    }

    /**
     * Method to check if the card put on this one is the same symbol
     *
     * @param currentCard card put on top
     * @return true if the cards have the same symbol
     */
    boolean symbolRuleSatisfied(HandCard currentCard) {
        return this.getClass().equals(currentCard.getClass());
    }
}
