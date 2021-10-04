package ch.zhaw.theluckyseven.frantic.model.gamelogic;


import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Card;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.NumeralCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.eventcard.*;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.ColorSwap;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.Skip;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.specialcard.Thief;

import java.util.Random;

/**
 * Configuration class with comm objects
 */
public class Config {
    public static final Random random = new Random();
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 8;
    public static final int DECODER_OFFSET = 22;
    public static final String FRANTIC_URL = "https://rulefactory.ch/wp-content/uploads/2020/10/Pandoras_Box_Gesamtregelwerk_EN_web.pdf";
    private static final String CARD_IMAGE_BASE_PATH = "/ch.zhaw.theluckyseven.frantic.view/card/";
    public static final String IMAGE_PATH_BACK_HAND_CARD = CARD_IMAGE_BASE_PATH + "back/hand_card_back.png";
    private static final String IMAGE_PATH_NUMERAL_CARD = CARD_IMAGE_BASE_PATH + "front/numeralcard/";
    private static final String IMAGE_PATH_EVENT_CARD = CARD_IMAGE_BASE_PATH + "front/eventcard/";
    private static final String IMAGE_PATH_SPECIAL_CARD = CARD_IMAGE_BASE_PATH + "front/specialcard/";

    /**
     * Enum of the colors of cards
     */
    public enum CardColor {
        ANY, BLACK, YELLOW, BLUE, RED, GREEN, BLUE_YELLOW, BLUE_RED, BLUE_GREEN, RED_GREEN, YELLOW_RED, YELLOW_GREEN, NONE
    }

    /**
     * Enum to define all Cards
     */
    public enum Cards {
        BLUE_ONE(NumeralCard.class, 1, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_I.png"),
        BLUE_TWO(NumeralCard.class, 2, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_II.png"),
        BLUE_THREE(NumeralCard.class, 3, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_III.png"),
        BLUE_FOUR(NumeralCard.class, 4, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_IV.png"),
        BLUE_FIVE(NumeralCard.class, 5, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_V.png"),
        BLUE_SIX(NumeralCard.class, 6, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_VI.png"),
        BLUE_SEVEN(NumeralCard.class, 7, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_VII.png"),
        BLUE_EIGHT(NumeralCard.class, 8, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_VIII.png"),
        BLUE_NINE(NumeralCard.class, 9, CardColor.BLUE, 2, IMAGE_PATH_NUMERAL_CARD + "blue_IX.png"),

        GREEN_ONE(NumeralCard.class, 1, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_I.png"),
        GREEN_TWO(NumeralCard.class, 2, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_II.png"),
        GREEN_THREE(NumeralCard.class, 3, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_III.png"),
        GREEN_FOUR(NumeralCard.class, 4, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_IV.png"),
        GREEN_FIVE(NumeralCard.class, 5, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_V.png"),
        GREEN_SIX(NumeralCard.class, 6, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_VI.png"),
        GREEN_SEVEN(NumeralCard.class, 7, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_VII.png"),
        GREEN_EIGHT(NumeralCard.class, 8, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_VIII.png"),
        GREEN_NINE(NumeralCard.class, 9, CardColor.GREEN, 2, IMAGE_PATH_NUMERAL_CARD + "green_IX.png"),

        RED_ONE(NumeralCard.class, 1, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_I.png"),
        RED_TWO(NumeralCard.class, 2, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_II.png"),
        RED_THREE(NumeralCard.class, 3, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_III.png"),
        RED_FOUR(NumeralCard.class, 4, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_IV.png"),
        RED_FIVE(NumeralCard.class, 5, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_V.png"),
        RED_SIX(NumeralCard.class, 6, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_VI.png"),
        RED_SEVEN(NumeralCard.class, 7, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_VII.png"),
        RED_EIGHT(NumeralCard.class, 8, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_VIII.png"),
        RED_NINE(NumeralCard.class, 9, CardColor.RED, 2, IMAGE_PATH_NUMERAL_CARD + "red_IX.png"),

        YELLOW_ONE(NumeralCard.class, 1, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_I.png"),
        YELLOW_TWO(NumeralCard.class, 2, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_II.png"),
        YELLOW_THREE(NumeralCard.class, 3, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_III.png"),
        YELLOW_FOUR(NumeralCard.class, 4, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_IV.png"),
        YELLOW_FIVE(NumeralCard.class, 5, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_V.png"),
        YELLOW_SIX(NumeralCard.class, 6, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_VI.png"),
        YELLOW_SEVEN(NumeralCard.class, 7, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_VII.png"),
        YELLOW_EIGHT(NumeralCard.class, 8, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_VIII.png"),
        YELLOW_NINE(NumeralCard.class, 9, CardColor.YELLOW, 2, IMAGE_PATH_NUMERAL_CARD + "yellow_IX.png"),

        BLACK_ONE(NumeralCard.class, 1, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_I.png"),
        BLACK_TWO(NumeralCard.class, 2, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_II.png"),
        BLACK_THREE(NumeralCard.class, 3, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_III.png"),
        BLACK_FOUR(NumeralCard.class, 4, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_IV.png"),
        BLACK_FIVE(NumeralCard.class, 5, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_V.png"),
        BLACK_SIX(NumeralCard.class, 6, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_VI.png"),
        BLACK_SEVEN(NumeralCard.class, 7, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_VII.png"),
        BLACK_EIGHT(NumeralCard.class, 8, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_VIII.png"),
        BLACK_NINE(NumeralCard.class, 9, CardColor.BLACK, 1, IMAGE_PATH_NUMERAL_CARD + "black_IX.png"),

        SKIP_BLUE(Skip.class, 7, CardColor.BLUE, 1, IMAGE_PATH_SPECIAL_CARD + "blue_skip.png"),
        SKIP_YELLOW(Skip.class, 7, CardColor.YELLOW, 1, IMAGE_PATH_SPECIAL_CARD + "yellow_skip.png"),
        SKIP_GREEN(Skip.class, 7, CardColor.GREEN, 1, IMAGE_PATH_SPECIAL_CARD + "green_skip.png"),
        SKIP_RED(Skip.class, 7, CardColor.RED, 1, IMAGE_PATH_SPECIAL_CARD + "red_skip.png"),

        THIEF_RED(Thief.class, 7, CardColor.RED, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/red_thief.png"),
        THIEF_BLUE(Thief.class, 7, CardColor.BLUE, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/blue_thief.png"),
        THIEF_GREEN(Thief.class, 7, CardColor.GREEN, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/green_thief.png"),
        THIEF_YELLOW(Thief.class, 7, CardColor.YELLOW, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/yellow_thief.png"),

        COLOR_SWAP_RED_BLUE(ColorSwap.class, 7, CardColor.BLUE_RED, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/red_blue.png"),
        COLOR_SWAP_RED_GREEN(ColorSwap.class, 7, CardColor.RED_GREEN, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/red_green.png"),
        COLOR_SWAP_GREEN_BLUE(ColorSwap.class, 7, CardColor.BLUE_GREEN, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/green_blue.png"),
        COLOR_SWAP_RED_YELLOW(ColorSwap.class, 7, CardColor.YELLOW_RED, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/red_yellow.png"),
        COLOR_SWAP_GREEN_YELLOW(ColorSwap.class, 7, CardColor.YELLOW_GREEN, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/yellow_green.png"),
        COLOR_SWAP_BLUE_YELLOW(ColorSwap.class, 7, CardColor.BLUE_YELLOW, 1, IMAGE_PATH_SPECIAL_CARD + "troublemaker/yellow_blue.png"),

        BLACK_HOLE(BlackHole.class, -1, CardColor.NONE, 1, IMAGE_PATH_EVENT_CARD + "troublemaker/black_hole.png"),
        COMMUNISM(Communism.class, -1, CardColor.NONE, 1, IMAGE_PATH_EVENT_CARD + "communism.png"),
        CAPITALISM(Capitalism.class, -1, CardColor.NONE, 1, IMAGE_PATH_EVENT_CARD + "troublemaker/capitalism.png"),
        EARTHQUAKE(Earthquake.class, -1, CardColor.NONE, 1, IMAGE_PATH_EVENT_CARD + "earthquake.png"),
        FRIDAY_THE_13TH(FridayThe13th.class, -1, CardColor.NONE, 4, IMAGE_PATH_EVENT_CARD + "friday_the_13th.png"),
        THIRD_TIME_LUCKY(ThirdTimeLucky.class, -1, CardColor.NONE, 1, IMAGE_PATH_EVENT_CARD + "third_time_lucky.png");

        private final Class<? extends Card> clazz;
        private final int value;
        private final CardColor color;
        private final int count;
        private final String imagePath;


        /**
         * Card enum
         * @param clazz class of the card
         * @param value value of the card (points)
         * @param color color of the card
         * @param count amount of this card in the game
         * @param imagePath  path to the image of the card
         */
        Cards(Class<? extends Card> clazz, int value, CardColor color, int count, String imagePath) {
            this.clazz = clazz;
            this.value = value;
            this.color = color;
            this.count = count;
            this.imagePath = imagePath;
        }

        public Class<? extends Card> getClazz() {
            return clazz;
        }

        public int getNumber() {
            return value;
        }

        public CardColor getColor() {
            return color;
        }

        public int getCount() {
            return count;
        }

        public String getImageFront() {
            return imagePath;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase().replace("_", " ");
        }

        public boolean isEventCard() {
            return EventCard.class.isAssignableFrom(this.getClazz());
        }

        public boolean isHandCard() {
            return HandCard.class.isAssignableFrom(this.getClazz());
        }
    }

    /**
     * Enum to define the different durations of a game
     */
    public enum GameDuration {
        QUICK_PLAY(20, 20, 2),
        SHORT_GAME(137, 113, 5),
        NORMAL_GAME(154, 137, 7),
        LONG_GAME(179, 154, 7);

        private final int pointsSmallParty;
        private final int pointsBigParty;
        private final int initialCardCountInPlayerHand;

        GameDuration(int pointsSmallParty, int pointsBigParty, int initialCardCountInPlayerHand) {
            this.pointsSmallParty = pointsSmallParty;
            this.pointsBigParty = pointsBigParty;
            this.initialCardCountInPlayerHand = initialCardCountInPlayerHand;
        }

        public int getInitialCardCountInPlayerHand() {
            return initialCardCountInPlayerHand;
        }

        public int getMaxPointsByPlayerCount(int playerCount) {
            try {
                if (getPartySize(playerCount) == PartySize.SMALL) {
                    return pointsSmallParty;
                } else {
                    return pointsBigParty;
                }
            } catch (GameException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public String toString() {
            return this.name().replace("_", " ").toUpperCase();
        }
    }

    private static PartySize getPartySize(int playerCount) throws GameException {
        if (playerCount <= 1) {
            throw new GameException(GameException.GameError.NOT_ENOUGH_PLAYERS);
        } else if (playerCount <= PartySize.SMALL.getMax()) {
            return PartySize.SMALL;
        } else if (playerCount <= PartySize.BIG.getMax()) {
            return PartySize.BIG;
        } else {
            throw new GameException(GameException.GameError.MAX_PLAYERS);
        }
    }

    private enum PartySize {
        SMALL(2, 4),
        BIG(5, 8);

        private final int min;
        private final int max;

        PartySize(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMax() {
            return max;
        }
    }
}

