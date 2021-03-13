package edu.kit.stephan.escaperoutes.main;

import edu.kit.informatik.Terminal;
import edu.kit.stephan.escaperoutes.commands.Command;
import edu.kit.stephan.escaperoutes.commands.CommandParser;

import edu.kit.stephan.escaperoutes.commands.Result;
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
    private boolean isCodeRunning;

    /**
     * Constructs a new instance.
     */
    public Session() {
        isCodeRunning = true;
        escapeNetworkDatabase = new EscapeNetworkDatabase();
    }


    /**
     * Starts the interactive session by reading the Terminal Input
     */
    public void interactive() {
        while (isCodeRunning) {
            String inputUser = Terminal.readLine();
            Result result = processSingleCommand(inputUser);

            if (result.getType().equals(Result.ResultType.FAILURE)) {
                Terminal.printError(result.getMessage());
            } else {

                if (result.getType().equals(Result.ResultType.SUCCESS) && result.getMessage() != null) {
                    Terminal.printLine(result.getMessage());
                    continue;
                }
                isCodeRunning = false;
            }
        }
    }

    /**
     * processes a single input
     *
     * @param inputUser the String of the user input;
     * @return an Return Object which has the ResultType and its corresponding message
     */
    private Result processSingleCommand(String inputUser) {
        CommandParser commandParser = new CommandParser();
        Pair<String, List<String>> parsedArguments;
        try {
            parsedArguments = commandParser.parseCommand(inputUser);
        } catch (SyntaxException e) {
            return new Result(Result.ResultType.FAILURE, e.getMessage());
        }
        final String command = parsedArguments.getFirstElement();
        final List<String> parameters = parsedArguments.getSecondElement();
        return executeSingleCommand(command, parameters);
    }

    /**
     * Method which execute the inputted Command with the Parameters
     *
     * @param command    the parsed Command String
     * @param parameters the parameters to the associated Command
     * @return an Return Object which has the ResultType and its corresponding message
     */
    private Result executeSingleCommand(String command, List<String> parameters) {
        return Command.getCommand(command).executeCommand(parameters, escapeNetworkDatabase);
    }

}