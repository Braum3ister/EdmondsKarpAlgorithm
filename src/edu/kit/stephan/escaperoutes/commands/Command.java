package edu.kit.stephan.escaperoutes.commands;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;
import edu.kit.stephan.escaperoutes.graphs.Edge;
import edu.kit.stephan.escaperoutes.graphs.EscapeNetwork;
import edu.kit.stephan.escaperoutes.graphs.EscapeNetworkDatabase;
import edu.kit.stephan.escaperoutes.graphs.Vertex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class describes a Command
 * @author Johannes Stephan
 * @version 1.0
 * @see EscapeNetworkDatabase
 */
public enum Command {
    /**
     * Executes the Add-Network Command
     */
    ADD_NETWORK(CommandParser.ADD_NETWORK) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            String resultMessage;

            try {
                resultMessage = escapeNetworkDatabase.addNewEscapeNetwork(Command.createEscapeNetwork(parameters));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }

    },
    /**
     * Executes the Add Section Command
     */
    ADD_SECTION(CommandParser.ADD_SECTION) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            String resultMessage;

            try {
                resultMessage = escapeNetworkDatabase.addNewSection(parameters.get(INDEX_OF_NAME),
                        Command.createEdgeOutOfInput(parameters.get(INDEX_OF_PARAMETERS)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * Executes the list Command which shows all networks
     */
    LIST_ALL_NETWORKS(CommandParser.LIST_ONE) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            return new Result(Result.ResultType.SUCCESS, escapeNetworkDatabase.listNetworks());
        }
    },

    /**
     * Executes the list Command which shows the calculated flows of a specific network
     */
    LIST_SPECIFIC_NETWORK(CommandParser.LIST_TWO) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            String resultMessage;

            try {
                resultMessage = escapeNetworkDatabase.listFlowOfSpecificNetwork(parameters.get(INDEX_OF_NAME));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * Executes the flow Command, which calculates the flow between two given Points
     */
    FLOW(CommandParser.FLOW) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            String resultMessage;

            try {
                resultMessage = escapeNetworkDatabase.calculateOrGetFlow(parameters.get(INDEX_OF_NAME)
                        , new Vertex(parameters.get(INDEX_OF_FLOW_START_POINT))
                        , new Vertex(parameters.get(INDEX_OF_FLOW_END_POINT)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * Executes the print Command, which prints a Network which corresponds to a given identifier
     */
    PRINT(CommandParser.PRINT) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            String resultMessage;

            try {
                resultMessage = escapeNetworkDatabase.printEscapeNetwork(parameters.get(INDEX_OF_NAME));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * Executes the Quit Command
     */
    QUIT(CommandParser.QUIT) {
        @Override
        public Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase) {
            return new Result(Result.ResultType.SUCCESS);
        }
    };

    private static final int INDEX_OF_FLOW_START_POINT = 2;
    private static final int INDEX_OF_FLOW_END_POINT = 3;
    private static final int INDEX_OF_PARAMETERS = 2;
    private static final int INDEX_OF_NAME = 1;
    private static final int START_OF_NAME = 0;
    private static final String SPLIT_SECTION = ";";
    private static final String REGEX_NUMBER = "\\d+";
    private final String commandName;

    /**
     * Constructor of the Command Enum
     * @param commandName the corresponding String name of a command
     */
    Command(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Executes a specific Command
     * @param parameters Parameters which are needed to execute the Command
     * @param escapeNetworkDatabase the database on which the Command is executed
     * @return a result, which corresponds to a result type and a message
     */
    public abstract Result executeCommand(List<String> parameters, EscapeNetworkDatabase escapeNetworkDatabase);

    /**
     * Getter-Method for the get-Command
     * @return the commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Gets the corresponding Command to an inputted String
     * @param commandName the name of a command
     * @return the Command
     */
    public static Command getCommand(String commandName) {
        return Arrays.stream(Command.values())
                .filter(command -> command.getCommandName().equals(commandName))
                .findFirst().orElse(null);
    }

    /**
     *
     * Method which creates Edges
     * @param parameterEdges Edges represented as String
     * @return the parsed Edge
     * @throws SemanticsException throws an Error if Edge is not valid
     */
    private static Edge createEdgeOutOfInput(String parameterEdges) throws SemanticsException {
        Pattern pattern = Pattern.compile(REGEX_NUMBER);
        Matcher matcher = pattern.matcher(parameterEdges);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();
        String nameOfFirstVertex = parameterEdges.substring(START_OF_NAME, matcher.start());
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
    private static EscapeNetwork createEscapeNetwork(List<String> parameters) throws SemanticsException {
        String[] edgesString = parameters.get(INDEX_OF_PARAMETERS).split(SPLIT_SECTION);
        List<Edge> edges = new LinkedList<>();

        for (String s : edgesString) {
            Edge edge = createEdgeOutOfInput(s);
            if (edges.contains(edge)) {
                throw new SemanticsException(Errors.CANT_ADD_EDGE_WHICH_EXIST);
            }
            edges.add(edge);
        }
        return new EscapeNetwork(parameters.get(INDEX_OF_NAME), edges);
    }
}
