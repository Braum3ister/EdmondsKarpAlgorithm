package edu.kit.stephan.escaperoutes.errors;

/**
 * This class stores the ErrorMessages, to prevent MagicStrings
 * @author Johannes Stephan
 * @version 1.0
 */
public final class Errors {
    /**
     * Gets Thrown when the Regex is invalid
     */
    public static final String SYNTAX_ERROR = "The Regex of the command is invalid.";
    /**
     * Gets Thrown when the Command does not exist
     */
    public static final String COMMAND_DOES_NOT_EXIST = "Command doesn't exist.";
    /**
     * Gets Thrown when the User tries to add Graph Segment which is invalid
     */
    public static final String CANT_ADD_OPPOSITE_GRAPH_DIRECTION = "Can't add graph segment, which is invalid.";
    /**
     * Gets Thrown when the User tries to print a Graph which is not existent
     */
    public static final String GRAPH_NOT_EXIST = "Graph to print doesn't exist";
    /**
     * Gets Thrown when the Graph already exists
     */
    public static final String GRAPH_ALREADY_EXIST = "Try a new name, the graph already exists.";
    /**
     * Gets Thrown when the Section which should be added is Invalid
     */
    public static final String GRAPH_SECTION_ADD_IS_INVALID
            = "Cant add section, because it would create an invalid graph";
    /**
     * Gets Thrown when the Capacity is not an Integer
     */
    public static final String CAPACITY_IS_NOT_A_INT = "The given capacity is not an integer";
    /**
     *  Gets thrown when the Graph is not existent
     */
    public static final String GRAPH_DOES_NOT_EXIST = "The Graph does not exist";
    /**
     * Gets thrown when Start or End Points are not existent
     */
    public static final String POINT_DOES_NOT_EXIST = "Start/End point doesn't exist";

    /**
     * Gets thrown when Start and Endpoints are equal
     */
    public static final String POINTS_CANNOT_BE_EQUAL = "Start and Endpoints have to be different";

    /**
     * Gets thrown when Points are Unreachable
     */
    public static final String POINTS_ARE_UNREACHABLE = "Points are unreachable";

    /**
     * Gets thrown Points dont meet the requirements
     */
    public static final String POINTS_DONT_MEET_REQUIREMENTS = "Points dont meet the requirements";

    /**
     * Gets thrown Graph has no start and EndPoints
     */
    public static final String GRAPH_HAS_NO_START_OR_END_POINTS = "Graph does not have start or endpoints";

    /**
     * Gets thrown when the Edge already exist.
     */
    public static final String CANT_ADD_EDGE_WHICH_EXIST = "This edge exist already";

    /**
     * Gets thrown when the Graph has loops
     */
    public static final String LOOPS_NOT_ALLOWED = "Loops are not allowed";

    /**
     * Error-Message if something was not implemented, but should
     */
    public static final String NOT_IMPLEMENTED = "Should have been implemented";
    /**
     * Error-Message if a command did not succeed but no message was supplied
     */
    public static final String COMMAND_ENDED_ERROR = "Command did not succeed.";

    /**
     * Utility-class constructor
     */
    private Errors() {
        throw new IllegalStateException("Utility-class constructor.");
    }
}
