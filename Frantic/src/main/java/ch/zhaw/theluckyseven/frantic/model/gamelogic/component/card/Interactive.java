package ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.GameStateController;

import java.util.List;

/**
 * Represents interface of 'active' cards which need further information from the actor.
 */
public interface Interactive extends Active {

    /**
     * Set actor's answer.
     * NOTE: the answer musst follow the order of question being set.
     *
     * @param answer array of answers answered in response to questions.
     */
    void setAnswer(List<Object> answer);

    boolean hasOpenQuestion();

    /**
     * Get question actor should answer in order to play 'interactive' card
     *
     * @return Array with questions
     */
    CardQuestion[] getQuestion();


    /**
     * Note: if there is no possibilities to choose from empty array will be returned
     *
     * @param gameStateController the current gameStateController
     * @return array of possibilities to choose from
     */
    List<Object> getOffer(GameStateController gameStateController);

    /**
     * Cardquestion for special cards
     */
    enum CardQuestion {
        SET_ACTOR, CHOOSE_COLOR, CHOOSE_NUMBER, CHOOSE_PLAYER, CHOOSE_CARD, CHOOSE_COLOR_OR_NUMBER
    }
}
