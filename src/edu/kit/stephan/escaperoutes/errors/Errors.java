package edu.kit.stephan.escaperoutes.errors;

/**
 * This class stores the ErrorMessages, to prevent MagicStrings
 * @author Johannes Stephan
 * @version 1.0
 */
public final class Errors {
    /**
     *
     */
    public static final String SYNTAX_ERROR = "The Regex of the command is invalid";
    /**
     *
     */
    public static final String COMMAND_DOES_NOT_EXIST = "Command doesnt exist";
    /**
     *
     */
    public static final String CANT_ADD_OPPOSITE_GRAPH_DIRECTION = "Can't add graph Segment";
    /**
     *
     */
    public static final String GRAPH_NOT_EXIST = "Graph to print does not exist";
    /**
     *
     */
    public static final String GRAPH_ALREADY_EXIST = "Try a new name";
    /**
     *
     */
    public static final String GRAPH_SECTION_ADD_IS_INVALID = "Cant add section";
    /**
     *
     */
    public static final String CAPACITY_IS_NOT_A_INT = "capacity is not an Integer";
    /**
     *
     */
    public static final String COMMAND_NOT_IMPLEMENTED = "Command was not implemented";
    /**
     *
     */
    public static final String GRAPH_DOES_NOT_EXIST = "Graph does not exist";

    /**
     *
     */
    public static final String POINT_DOES_NOT_EXIST = "Start/End point does not exist";

    /**
     *
     */
    public static final String POINTS_CANNOT_BE_EQUAL = "Start and Endpoints have to be different";

    /**
     *
     */
    public static final String POINTS_ARE_UNREACHABLE = "Points are unreachable";

    /**
     *
     */
    public static final String POINTS_DONT_MEET_REQUIREMENTS = "Points dont meet the requirements";

    /**
     *
     */
    public static final String GRAPH_HAS_NO_START_OR_END_POINTS = "Graph does not have start or endpoints";

    /**
     *
     */
    public static final String CANT_ADD_EDGE_WHICH_EXIST = "This edge exists already";

    /**
     *
     */
    public static final String LOOPS_NOT_ALLOWED = "Loops are not allowed";

    /**
     * Utility-class constructor
     */
    private Errors() {
        throw new IllegalStateException("Utility-class constructor.");
    }
}
