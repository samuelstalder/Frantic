package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * CardFactory class to create Cards
 */
public class CardFactory {
    private static final Logger logger = Logger.getLogger(CardFactory.class.getCanonicalName());
    private static final Class[] constructorSignatureOfEventCard = new Class[]{String.class, String.class, String.class};
    private static final Class[] constructorSignatureOfHandCard = new Class[]{String.class, String.class, String.class, int.class, int.class, Config.CardColor.class};

    private CardFactory() {
        logger.severe("Error while making a new CardFactory");
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a card
     * @param cardId internal id of the card
     * @param card card to create
     * @return the created card
     * @throws NoSuchMethodException if the constructor of the card doesn't exist
     * @throws IllegalAccessException if the method access is not in scope or private
     * @throws InvocationTargetException if an error occurs when creating a new instance of a card
     * @throws InstantiationException if an error occurs when creating a new instance of a card
     */
    public static Card create(int cardId, Config.Cards card) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (card.isEventCard()) {
            return card.getClazz().getDeclaredConstructor(constructorSignatureOfEventCard).newInstance(card.toString(), card.getImageFront(), Config.IMAGE_PATH_BACK_HAND_CARD);
        } else if (card.isHandCard()) {
            return card.getClazz().getDeclaredConstructor(constructorSignatureOfHandCard).newInstance(card.toString(), card.getImageFront(), Config.IMAGE_PATH_BACK_HAND_CARD, cardId, card.getNumber(), card.getColor());
        }
        return null;
    }
}
