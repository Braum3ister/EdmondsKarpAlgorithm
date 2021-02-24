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
     * String representation of List Variant 1
     */
    public static final String LIST_ONE = "listAllNetworks";
    /**
     * String representation of List Variant 2
     */
    public static final String LIST_TWO = "listFlowOfNetwork";

    private static final String REGEX_ADD
            = " [A-Z]{1,6} ([a-z]{1,6}[0]*[1-9][0-9]*[a-z]{1,6};)*[a-z]{1,6}[0]*[1-9][0-9]*[a-z]{1,6}";
    private static final String REGEX_PRINT = " [A-Z]{1,6}";
    private static final String REGEX_FLOW = " [A-Z]{1,6} [a-z]{1,6} [a-z]{1,6}";
    private static final String REGEX_LIST = " [A-Z]{1,6}";

    /**
     * Constructor of CommandParser
     */
    public CommandParser() {
    }

    /**
     * Parses the inputted String to be handled more easily
     * @param inputUser The String which should be parsed
     * @return Pair, which consists out of a Command and its Parameters
     * @throws SyntaxException if the Regex is wrong, or the Command is not Valid and Error gets thrown.
     */
    public Pair<String, List<String>> parseCommand(String inputUser) throws SyntaxException {
        checkBasicRegex(inputUser);
        String commandValue = inputUser.split(" ")[0];
        String modifiedInput = inputUser.substring(commandValue.length());
        return new Pair<>(checkCommand(commandValue, modifiedInput), createParameters(modifiedInput));

    }

    /**
     * Creates the parameters and putts them in a specific String
     * @param modifiedInput Input which consists out of Parameter which are separated by a white space.
     * @return List of Parameters
     */
    private List<String> createParameters(String modifiedInput) {
        if (modifiedInput.equals("")) {
            return null;
        }
        String[] outputAsStringArray = modifiedInput.split(" ");
        return new LinkedList<>(Arrays.asList(outputAsStringArray));

    }

    /**
     * Checks the basic validation of an inputted String
     * @param inputUser The String which needs to be checked
     * @throws SyntaxException throws an error if the syntax is not valid
     */
    private void checkBasicRegex(String inputUser) throws SyntaxException {
        if (inputUser.isEmpty()) throw new SyntaxException(Errors.SYNTAX_ERROR);
        if (inputUser.charAt(0) == ' ') {
            throw new SyntaxException(Errors.SYNTAX_ERROR);
        }
    }
    /**
     *
     * @param command The first String which should be checked if its an valid command
     * @param modifiedInput the rest of the inputted String except the command
     * @return the valid command
     * @throws SyntaxException if command does not match the Regex or is not implemented it throws an Error
     */
    private String checkCommand(String command, String modifiedInput) throws SyntaxException {

        switch (command) {
            case ADD:
                if (modifiedInput.matches(REGEX_ADD)) {
                    return ADD;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case PRINT:
                if (modifiedInput.matches(REGEX_PRINT)) {
                    return PRINT;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case FLOW:
                if (modifiedInput.matches(REGEX_FLOW)) {
                    return FLOW;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case LIST:
                if (modifiedInput.equals("")) {
                    return LIST_ONE;
                } else if (modifiedInput.matches(REGEX_LIST)) {
                    return LIST_TWO;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            case QUIT:
                if (modifiedInput.equals("")) {
                    return QUIT;
                }
                throw new SyntaxException(Errors.SYNTAX_ERROR);

            default:
                throw new SyntaxException(Errors.COMMAND_DOES_NOT_EXIST);
        }
    }
}
