package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;

import java.util.Objects;

/**
 * Abstract class for all Cards with a number
 * All Numberal Cards are Handcards
 * the number of the card is also the value of the card
 */
public class NumeralCard extends HandCard {

    private final int number;

    /**
     * Creates a NumeralCard
     *
     * @param cardName   name of the card
     * @param imageFront url to the front image
     * @param imageBack  url to the back image
     * @param id         id of the card
     * @param number     number of the card
     * @param color      color of the card
     */
    public NumeralCard(String cardName, String imageFront, String imageBack, int id, int number, Config.CardColor color) {
        super(cardName, imageFront, imageBack, id, number, color);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Mark the card, if can be played on the card on top of the discard pile.
     *
     * @param currentCard card on top of discard pile
     */
    @Override
    public void updatePlayable(HandCard currentCard) {
        super.updatePlayable(currentCard);
        if (isPlayable() || numberRuleSatisfied(currentCard)) {
            setPlayable();
        }
    }

    /**
     * Method to check whether a card with the same number is put on this card
     *
     * @param currentCard card put on top
     * @return true if both cards have the same number
     */
    boolean numberRuleSatisfied(HandCard currentCard) {
        boolean result = false;
        if (currentCard instanceof NumeralCard) {
            result = this.number == ((NumeralCard) currentCard).getNumber();

        }
        return result;
    }

    /**
     * @param o another object
     * @return true, if object is of the same class, has same id, color and number
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NumeralCard that = (NumeralCard) o;
        return getId() == that.getId() && number == that.number && getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), number, getColor());
    }
}
