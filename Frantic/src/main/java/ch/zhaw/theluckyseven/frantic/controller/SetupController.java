package ch.zhaw.theluckyseven.frantic.controller;

/**
 * Interface for different types of SetupControllers
 * A setupController is a controller that only is important before the game starts.
 * A setupController has no function once the game started.
 */
public interface SetupController {

    /**
     * Method to signalize the Controller that a new Player is connected
     *
     * @param name name of the Player
     */
    void newPlayerConnected(String name);
}
