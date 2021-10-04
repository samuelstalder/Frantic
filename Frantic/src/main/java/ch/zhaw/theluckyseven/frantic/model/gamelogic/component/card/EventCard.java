package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

/**
 * Abstract class for EventCards
 * EventCards are cards that get played when a black card is put on the discard pile
 */
public abstract class EventCard extends Card implements Active {

    /**
     * Creates an eventCard
     *
     * @param cardName   name of the card
     * @param imageFront url to front of the card
     * @param imageBack  url to back of the card
     */
    public EventCard(String cardName, String imageFront, String imageBack) {
        super(cardName, imageFront, imageBack);
    }

    @Override
    public String toString() {
        return isClosed() ? "NONE" : getCardName().toUpperCase();
    }
}
