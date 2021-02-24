package edu.kit.stephan.escaperoutes.main;

import edu.kit.informatik.Terminal;
import edu.kit.stephan.escaperoutes.commands.CommandParser;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;
import edu.kit.stephan.escaperoutes.errors.SyntaxException;
import edu.kit.stephan.escaperoutes.graphs.EscapeNetwork;
import edu.kit.stephan.escaperoutes.graphs.EscapeNetworksDatabase;
import edu.kit.stephan.escaperoutes.graphs.Vertex;
import edu.kit.stephan.escaperoutes.graphs.Edge;
import edu.kit.stephan.escaperoutes.utilities.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class describes a session of command execution
 * @author Johannes Stephan
 * @version 1.0
 */
public class Session {
    private boolean isCodeRunning;
    private final EscapeNetworksDatabase escapeNetworksDatabase;

    /**
     * Constructs a new instance.
     */
    public Session() {
        isCodeRunning = true;
        escapeNetworksDatabase = new EscapeNetworksDatabase();
    }


    /**
     * Starts the interactive session by reading the Terminal Input
     */
    public void interactive() {
        while (isCodeRunning) {
            String inputUser = Terminal.readLine();
            String output;
            try {
                output = processSingleCommand(inputUser);
            }
            catch (SyntaxException | SemanticsException exception) {
                Terminal.printError(exception.getMessage());
                continue;
            }
            if (!output.equals(CommandParser.QUIT)) {
                Terminal.printLine(output);
            } else {
                isCodeRunning = false;
            }
        }
    }

    /**
     * processes a single input
     * @param inputUser the String of the user input;
     * @return valid return Message
     * @throws SyntaxException throws an Exception if Syntax is wrong
     * @throws SemanticsException throws an Exception if Semantics are wrong
     */
    private String processSingleCommand(String inputUser) throws SyntaxException, SemanticsException {
        CommandParser commandParser = new CommandParser();
        Pair<String, List<String>> parsedArguments;

        parsedArguments = commandParser.parseCommand(inputUser);

        final String command = parsedArguments.getFirstElement();
        final List<String> parameters = parsedArguments.getSecondElement();
        return executeSingleCommand(command, parameters);

    }

    /**
     * Method which execute the inputted Command with the Parameters
     * @param command the parsed Command String
     * @param parameters the parameters to the associated Command
     * @return the valid return Message
     * @throws SemanticsException throws an Error if Semantics of the Parameters are wrong
     */
    private String executeSingleCommand(String command, List<String> parameters) throws SemanticsException {
        switch (command) {
            case CommandParser.QUIT:
                return CommandParser.QUIT;
            case CommandParser.ADD:
                if (parameters.get(2).contains(";")) {
                    return escapeNetworksDatabase.addNewEscapeNetwork(createEscapeNetwork(parameters));
                }
                return escapeNetworksDatabase.addNewSection(parameters.get(1)
                        , createEdgesOutOfInput(parameters.get(2)));

            case CommandParser.PRINT:
                return escapeNetworksDatabase.printEscapeNetwork(parameters.get(1));

            case CommandParser.LIST_ONE:
                return escapeNetworksDatabase.listNetworks();
            case CommandParser.LIST_TWO:
                return escapeNetworksDatabase.listFlowOfSpecificNetwork(parameters.get(1));

            case CommandParser.FLOW:
                return escapeNetworksDatabase.calculateOrGetFlow(parameters.get(1)
                        , new Vertex(parameters.get(2)), new Vertex(parameters.get(3)));
            default:
                throw new SemanticsException(Errors.COMMAND_NOT_IMPLEMENTED);
        }
    }

    /**
     *
     * a,b,10;c,b,100;
     * Method which creates Edges
     * @param parameterEdges Edges represented as String
     * @return the parsed Edge
     * @throws SemanticsException throws an Error if Edge is not valid
     */
    private Edge createEdgesOutOfInput(String parameterEdges) throws SemanticsException {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(parameterEdges);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();
        String nameOfFirstVertex = parameterEdges.substring(0, matcher.start());
        String nameOfSecondVertex = parameterEdges.substring(matcher.end());
        if (nameOfFirstVertex.equals(nameOfSecondVertex)) throw new SemanticsException(Errors.LOOPS_NOT_ALLOWED);
        int capacity;
        try {
            capacity = Integer.parseInt(matcher.group());
        } catch (NumberFormatException e) {
            throw new SemanticsException(Errors.CAPACITY_IS_NOT_A_INT);
        }
        return new Edge(new Vertex(nameOfFirstVertex), new Vertex(nameOfSecondVertex), capacity);
    }

    /**
     * Method which creates an EscapeNetwork
     * @param parameters the parameters needed to create an Escape Network
     * @return the finished EscapeNetwork
     * @throws SemanticsException if the semantics are wrong, which are needed to construct the EscapeNetwork
     */
    private EscapeNetwork createEscapeNetwork(List<String> parameters) throws SemanticsException {
        String[] edgesString = parameters.get(2).split(";");
        List<Edge> edges = new LinkedList<>();

        for (String s : edgesString) {
            Edge edge = createEdgesOutOfInput(s);
            if (edges.contains(edge)) {
                throw new SemanticsException(Errors.CANT_ADD_EDGE_WHICH_EXIST);
            }
            edges.add(edge);
        }
        return new EscapeNetwork(parameters.get(1), edges);
    }
}
