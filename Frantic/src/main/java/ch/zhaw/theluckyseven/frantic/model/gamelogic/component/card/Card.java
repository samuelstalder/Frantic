package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import java.io.Serializable;

/**
 * Abstract class Card for all types of cards
 */
public abstract class Card implements Serializable {
    private final String imageFront;
    private final String imageBack;
    private final String cardName;
    private boolean closed;

    /**
     * Creates a Card
     * @param cardName name of card
     * @param imageFront url to front image
     * @param imageBack url to back image
     */
    protected Card(String cardName, String imageFront, String imageBack) {
        this.imageFront = imageFront;
        this.imageBack = imageBack;
        this.cardName = cardName;
        closed = true;
    }

    /**
     * Retuns card image. Depending on which direction the card is facing it is back or front image
     * @return the image facing upwards
     */
    public String getCardImage() {
        return closed ? imageBack : imageFront;
    }

    public String getCardName() {
        return cardName;
    }

    /**
     * Method to flip card so the card is facing up (visible)
     */
    public void setOpened() {
        closed = false;
    }

    /**
     * Method to check if card is facing upside down
     * @return true if card is facing upside down (backside visible)
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    /**
     * Returns the name of the card or "unknown" if the card is upside down
     */
    public String toString() {
        return (closed) ? "unknown" : cardName;
    }
}
