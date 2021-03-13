package edu.kit.stephan.escaperoutes.graphs;

import edu.kit.stephan.escaperoutes.errors.SemanticsException;

import java.util.List;
import java.util.Objects;

/**
 * This class describes an EscapeNetwork and contains the Method to handle the Graph
 * @author Johannes Stephan
 * @version 1.0
 * @see Graph
 */
public class EscapeNetwork implements Comparable<EscapeNetwork> {
    private static final String VALID_LIST_OUTPUT = "%s %s";
    private final Graph graph;
    private final String uniqueIdentifier;


    /**
     * Constructor
     * @param uniqueIdentifier the name of the Network
     * @param edges The list of edges which are needed to construct the EscapeNetworkGraph
     * @throws SemanticsException throws an Error if the Creation of the Network has failed
     */
    public EscapeNetwork(String uniqueIdentifier, List<Edge> edges) throws SemanticsException {
        this.graph = new Graph();
        this.uniqueIdentifier = uniqueIdentifier;
        this.createGraph(edges);
    }

    /**
     * Method to create the Graph using Edges
     * @param edges Edges which are needed to create the Graph
     * @throws SemanticsException throws an Exception if the Graph does not match the expectations
     *                            of the assignments sheet
     */
    private void createGraph(List<Edge> edges) throws SemanticsException {
        for (Edge edge : edges) {
            addEdge(edge, true);
        }
        graph.checkIfGraphIsValid();
    }

    /**
     * Method to create an Edge in the Graph
     * @param edge the edge which should be added
     * @param initialization true if the graph is created, false if an Edge is inserted in an existent Graph
     * @throws SemanticsException if the creation of an Graph is not allowed
     */
    public void addEdge(Edge edge, boolean initialization) throws SemanticsException {
        Graph deepCopyOfGraph = new Graph(graph.copyGraph());
        deepCopyOfGraph.addEdge(edge.getFromVertex(), edge.getToVertex(), edge.getCapacityFlow());
        if (!initialization) deepCopyOfGraph.checkIfGraphIsValid();
        graph.addEdge(edge.getFromVertex(), edge.getToVertex(), edge.getCapacityFlow());
    }

    /**
     * Method to find the MaxFlow between to Points
     * @param start the origin of the Flow
     * @param end the destination of the Flow
     * @return a long value which is greater than 0, or 0 if there is no flow between the two Points
     * @throws SemanticsException if the Points are not valid to find a flow
     */
    public long calculateFlow(Vertex start, Vertex end) throws SemanticsException {
        return graph.getMaxFlowBetweenTwoPoints(start, end);
    }

    /**
     * Getter-Method to get the Flow List
     * @return the Flow List
     */
    public String getFlowList() {
        return graph.getFlowList();
    }

    /**
     * Getter-Method
     * @return the number of Vertices of the Graph
     */
    public int getNumberOfVertices() {
        return graph.getNumberOfVertices();
    }

    /**
     * Getter Method
     * @return the name of the Graph
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * GetterMethod
     * @return the Graph represented as String
     */
    public String getGraphAsString() {
        return graph.toString();
    }

    /**
     * toString Method of the EscapeNetwork
     * @return the EscapeNetworkRepresentedAsString
     */
    @Override
    public String toString() {
        return String.format(VALID_LIST_OUTPUT, uniqueIdentifier, graph.getNumberOfVertices());
    }

    /**
     * CompareTo Method
     * @param escapeNetwork the EscapeNetworkWhichShouldBeCompared
     * @return a number which describes which EscapeNetwork is bigger
     */
    @Override
    public int compareTo(EscapeNetwork escapeNetwork) {
        if (this.graph.getNumberOfVertices() == escapeNetwork.getNumberOfVertices()) {
            return this.uniqueIdentifier.compareTo(escapeNetwork.getUniqueIdentifier());
        }
        return Integer.compare(escapeNetwork.getNumberOfVertices(), this.graph.getNumberOfVertices());
    }

    /**
     * Equals-Method
     * @param o the Object which should be compared
     * @return true, if objects are equal; false, if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EscapeNetwork that = (EscapeNetwork) o;
        return Objects.equals(uniqueIdentifier, that.uniqueIdentifier);
    }

    /**
     * HashCode-Method
     * @return the HashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier);
    }
}
