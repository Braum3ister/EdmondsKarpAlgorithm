package edu.kit.stephan.escaperoutes.main;


/**
 * This class provides the Main access point
 * @author Johannes Stephan
 * @version 1.0
 */
public final class Main {

    /**
     * Utility class constructor
     */
    private Main() {
        throw new IllegalStateException("Utility-class constructor.");
    }


    /**
     * EntryPoint of the program
     * @param args not used
     */
    public static void main(String[] args) {
        Session session = new Session();
        session.interactive();

    }
}
