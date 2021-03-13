package edu.kit.stephan.escaperoutes.commands;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SyntaxException;
import edu.kit.stephan.escaperoutes.utilities.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represents a Command Parser, which has the purpose to check the Validation of the Syntax and handles
 * the conversion of the inputted String
 * @author Johannes Stephan
 * @version 1.0
 */
public class CommandParser {
    /**
     * String representation of add Command
     */
    public static final String ADD = "add";
    /**
     * String representation of Add Network Command
     */
    public static final String ADD_NETWORK = "addNetwork";
    /**
     * String representation of Add Section Command
     */
    public static final String ADD_SECTION = "addSection";
    /**
     * String representation of quit Command
     */
    public static final String QUIT = "quit";
    /**
     * String representation of flow Command
     */
    public static final String FLOW = "flow";
    /**
     * String representation of list Command
     */
    public static final String LIST = "list";
    /**
     * String representation of Print Command
     */
    public static final String PRINT = "print";
    /**
     * String representation of List all Networks
     */
    public static final String LIST_ONE = "listAllNetworks";
    /**
     * String representation of List all flow of a specific network
     */
    public static final String LIST_TWO = "listFlowOfNetwork";
    private static final char SPACE_CHAR = ' ';
    private static final String REGEX_IDENTIFIER = "[A-Z]{1,6}";
    private static final String REGEX_VERTEX = "[a-z]{1,6}";
    private static final String REGEX_NUMBER = "[0]*[1-9][0-9]*";
    private static final char SECTION_SPLITTER = ';';
    private static final String REGEX_SECTION = REGEX_VERTEX + REGEX_NUMBER + REGEX_VERTEX;
    private static final String REGEX_ADD_NETWORK
            = ADD + SPACE_CHAR + REGEX_IDENTIFIER + SPACE_CHAR
            + "(" + REGEX_SECTION + SECTION_SPLITTER + ")++" + REGEX_SECTION;
    private static final String REGEX_ADD_SECTION = ADD + SPACE_CHAR + REGEX_IDENTIFIER + SPACE_CHAR + REGEX_SECTION;
    private static final String REGEX_PRINT = PRINT + SPACE_CHAR + REGEX_IDENTIFIER;
    private static final String REGEX_FLOW = FLOW + SPACE_CHAR + REGEX_IDENTIFIER + SPACE_CHAR
            + REGEX_VERTEX + SPACE_CHAR + REGEX_VERTEX;
    private static final String REGEX_LIST_NETWORKS = LIST;
    private static final String REGEX_LIST_NETWORK = LIST + SPACE_CHAR + REGEX_IDENTIFIER;



    /**
     * Parses the inputted String to be handled more easily
     * @param inputUser The String which should be parsed
     * @return Pair, which consists out of a Command and its Parameters
     * @throws SyntaxException if the Regex is wrong, or the Command is not Valid and Error gets thrown.
     */
    public Pair<String, List<String>> parseCommand(String inputUser) throws SyntaxException {
        checkBasicRegex(inputUser);
        String commandValue = inputUser.split(String.valueOf(SPACE_CHAR))[0];
        String modifiedInput = inputUser.substring(commandValue.length());
        return new Pair<>(checkCommand(commandValue, inputUser), createParameters(modifiedInput));
    }

    /**
     * Creates the parameters and putts them in a specific String
     * @param modifiedInput Input which consists out of Parameter which are separated by a white space.
     * @return List of Parameters
     */
    private List<String> createParameters(String modifiedInput) {
        if (modifiedInput.isEmpty()) {
            return new LinkedList<>();
        }
        String[] outputAsStringArray = modifiedInput.split(String.valueOf(SPACE_CHAR));
        return new LinkedList<>(Arrays.asList(outputAsStringArray));
    }

    /**
     * Checks the basic validation of an inputted String
     * @param inputUser The String which needs to be checked
     * @throws SyntaxException throws an error if the syntax is not valid
     */
    private void checkBasicRegex(String inputUser) throws SyntaxException {
        if (inputUser.isEmpty()) throw new SyntaxException(Errors.SYNTAX_ERROR);
        if (inputUser.charAt(0) == SPACE_CHAR) throw new SyntaxException(Errors.SYNTAX_ERROR);
    }

    /**
     * Checks the Regex of each Command and throws an Error if the Regex does not match
     * @param command The first String which should be checked if its an valid command
     * @param inputUser the inputted User String
     * @return the valid command
     * @throws SyntaxException if command does not match the Regex or is not implemented it throws an Error
     */
    private String checkCommand(String command, String inputUser) throws SyntaxException {

        switch (command) {
            case ADD:
                if (inputUser.matches(REGEX_ADD_NETWORK)) return ADD_NETWORK;
                if (inputUser.matches(REGEX_ADD_SECTION)) return ADD_SECTION;

                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case PRINT:
                if (inputUser.matches(REGEX_PRINT)) {
                    return PRINT;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case FLOW:
                if (inputUser.matches(REGEX_FLOW)) {
                    return FLOW;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case LIST:
                if (inputUser.matches(REGEX_LIST_NETWORKS)) {
                    return LIST_ONE;
                }
                if (inputUser.matches(REGEX_LIST_NETWORK)) {
                    return LIST_TWO;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case QUIT:
                if (inputUser.matches(QUIT)) {
                    return QUIT;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            default:
                throw new SyntaxException(Errors.COMMAND_DOES_NOT_EXIST);
        }
    }
}
