package edu.kit.stephan.escaperoutes.main;

import edu.kit.informatik.Terminal;
import edu.kit.stephan.escaperoutes.commands.Command;
import edu.kit.stephan.escaperoutes.commands.CommandParser;

import edu.kit.stephan.escaperoutes.commands.Result;
import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SyntaxException;
import edu.kit.stephan.escaperoutes.graphs.EscapeNetworkDatabase;
import edu.kit.stephan.escaperoutes.utilities.Pair;
import java.util.List;

/**
 * This class describes a session of command execution
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class Session {
    private final EscapeNetworkDatabase escapeNetworkDatabase;
    private boolean isRunning;

    /**
     * Constructs a new instance.
     */
    public Session() {
        isRunning = true;
        escapeNetworkDatabase = new EscapeNetworkDatabase();
    }


    /**
     * Starts the interactive session by reading the Terminal Input
     */
    public void interactive() {
        isRunning = true;
        while (isRunning) {
            processSingleCommand();
        }
    }

    /**
     * processes a single input
     */
    private void processSingleCommand() {
        String inputUser = Terminal.readLine();
        CommandParser commandParser = new CommandParser();
        Pair<String, List<String>> parsedArguments;
        try {
            parsedArguments = commandParser.parseCommand(inputUser);
        } catch (SyntaxException e) {
            printError(e.getMessage());
            return;
        }

        final String command = parsedArguments.getFirstElement();
        final List<String> parameters = parsedArguments.getSecondElement();
        executeSingleCommand(command, parameters);
    }

    /**
     * Method which execute the inputted Command with the Parameters
     * (Inspired by the solution of Lukas Alber (Santorini))
     * @param command    the parsed Command String
     * @param parameters the parameters to the associated Command
     */
    private void executeSingleCommand(String command, List<String> parameters) {
        Result result = Command.getCommand(command).executeCommand(parameters, escapeNetworkDatabase);
        switch (result.getType()) {
            case SUCCESS:
                if (result.getMessage() != null) {
                    Terminal.printLine(result.getMessage());
                } else {
                    isRunning = false;
                }
                break;
            case FAILURE:
                if (result.getMessage() != null) {
                    printError(result.getMessage());
                } else {
                    printError(Errors.COMMAND_ENDED_ERROR);
                }
                break;

            default:
                throw new IllegalStateException(Errors.NOT_IMPLEMENTED);
        }
    }

    /**
     * This Method is used to simplify the Error Output
     * @param errorMessage the Error Message to be printed
     */
    private void printError(String errorMessage) {
        Terminal.printError(errorMessage);
    }
}