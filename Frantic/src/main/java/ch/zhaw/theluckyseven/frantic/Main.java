package ch.zhaw.theluckyseven.frantic;

/**
 * Start of the program. Needs to call the main method of our project to make it compatible
 * in all Enviroments.
 * <p>
 * Like described in this issue https://github.com/javafxports/openjdk-jfx/issues/236
 * <p>
 * "You can just create another class (which doesn't subclass Application)
 * with a main method which simply calls your Application-class
 * main method and it then works from Intellij"
 */
public class Main {

    /**
     * Start of the programm.
     *
     * @param args the arguments passed by the user
     */
    public static void main(String[] args) {
        GameSetup.main(args);
    }
}