package edu.kit.stephan.escaperoutes.graphs;


import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class manages all EscapeNetworks and executes the Methods
 * @author Johannes Stephan
 * @version 1.0
 * @see EscapeNetwork
 */
public class EscapeNetworkDatabase {
    private static final String ADD_WAS_VALID = "Added new escape network with identifier %s.";
    private static final String ADD_SECTION_WAS_VALID = "Added new section %s to escape network %s.";
    private static final String FLOW_NETWORK_EMPTY = "EMPTY";
    private static final int INDEX_JUMP = 1;
    private final Set<EscapeNetwork> escapeNetworkSet;

    /**
     * Constructor
     */
    public EscapeNetworkDatabase() {
        this.escapeNetworkSet = new TreeSet<>();
    }

    /**
     * This Method gets the String to Print an EscapeNetwork
     * @param uniqueIdentifier the identifier from the expected
     * @return the String which should be printed
     * @throws SemanticsException if the Network which should be printed does not exist
     */
    public String printEscapeNetwork(String uniqueIdentifier) throws SemanticsException {
        for (EscapeNetwork escapeNetwork : escapeNetworkSet) {
            if (escapeNetwork.getUniqueIdentifier().equals(uniqueIdentifier)) {
                return escapeNetwork.getGraphAsString();
            }
        }
        throw new SemanticsException(Errors.GRAPH_NOT_EXIST);
    }

    /**
     * Adds a totally new EscapeNetwork
     * @param escapeNetwork the escapeNetwork which should be added
     * @return a String which states that the EscapeNetwork was added
     * @throws SemanticsException if the UniqueIdentifier already exist
     */
    public String addNewEscapeNetwork(EscapeNetwork escapeNetwork) throws SemanticsException {
        for (EscapeNetwork escapeNet : escapeNetworkSet) {
            if (escapeNet.getUniqueIdentifier().equals(escapeNetwork.getUniqueIdentifier())) {
                throw new SemanticsException(Errors.GRAPH_ALREADY_EXIST);
            }
        }
        escapeNetworkSet.add(escapeNetwork);
        return String.format(ADD_WAS_VALID, escapeNetwork.getUniqueIdentifier());
    }

    /**
     * Adds a new Section to an existent EscapeNetwork
     * @param uniqueIdentifier the identifier of the EscapeNetwork
     * @param edge the Edge which should be added
     * @return a String which states that the section add was valid
     * @throws SemanticsException if the add was not successful
     */
    public String addNewSection(String uniqueIdentifier, Edge edge) throws SemanticsException {
        for (EscapeNetwork escapeNetwork : escapeNetworkSet) {
            if (escapeNetwork.getUniqueIdentifier().equals(uniqueIdentifier)) {
                escapeNetwork.addEdge(edge, false);
                return String.format(ADD_SECTION_WAS_VALID, edge.toString(), uniqueIdentifier);
            }
        }
        throw new SemanticsException(Errors.GRAPH_SECTION_ADD_IS_INVALID);
    }

    /**
     * Makes a List of all EscapeNetworks
     * @return String of the EscapeNetworks sorted by number of Nodes, if equal then they are sorted by their name.
     */
    public String listNetworks() {
        StringBuilder output = new StringBuilder();
        if (escapeNetworkSet.isEmpty()) {
            return FLOW_NETWORK_EMPTY;
        }
        for (EscapeNetwork escapeNetwork : escapeNetworkSet) {
            output.append(escapeNetwork.toString()).append(System.lineSeparator());
        }
        return output.deleteCharAt(output.length() - INDEX_JUMP).toString();
    }

    /**
     * Lists the calculated flow on the specific EscapeNetwork
     * @param uniqueIdentifier the identifier of the listed EscapeNetwork
     * @return the calculated flow values
     * @throws SemanticsException if the Graph does not exist
     */
    public String listFlowOfSpecificNetwork(String uniqueIdentifier) throws SemanticsException {
        for (EscapeNetwork escapeNetwork : escapeNetworkSet) {
            if (escapeNetwork.getUniqueIdentifier().equals(uniqueIdentifier)) {
                return escapeNetwork.getFlowList();
            }
        }
        throw new SemanticsException(Errors.GRAPH_DOES_NOT_EXIST);
    }

    /**
     * This Method gets the Flow between two Points, or if necessary calculates them.
     * @param uniqueIdentifier the Graph on which the Calculation is executed
     * @param start the start Vertex
     * @param end the end Vertex
     * @return the calculated flow Value
     * @throws SemanticsException if an error occurred during the Calculation
     */
    public String calculateOrGetFlow(String uniqueIdentifier, Vertex start, Vertex end) throws SemanticsException {
        for (EscapeNetwork escapeNetwork : escapeNetworkSet) {
            if (escapeNetwork.getUniqueIdentifier().equals(uniqueIdentifier)) {
                return String.valueOf(escapeNetwork.calculateFlow(start, end));
            }
        }
        throw new SemanticsException(Errors.GRAPH_DOES_NOT_EXIST);
    }
}
