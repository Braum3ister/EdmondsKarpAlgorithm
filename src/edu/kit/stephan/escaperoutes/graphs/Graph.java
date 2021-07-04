package edu.kit.stephan.escaperoutes.graphs;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;
import edu.kit.stephan.escaperoutes.utilities.Pair;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * The Class describes a Graph of an EscapeNetwork and manages the Graph
 *
 * @author Johannes Stephan
 * @version 1.0
 * @see Flow
 * @see Vertex
 */
public class Graph {
    private static final int INDEX_JUMP = 1;

    private final Map<Vertex, Set<Pair<Vertex, Integer>>> graphMap;

    /**
     * Constructor of the Graph
     */
    public Graph() {
        graphMap = new TreeMap<>();
    }

    /**
     * Constructor of Graph with pre set Graph Map
     *
     * @param graphMap the map which should be preset
     */
    public Graph(Map<Vertex, Set<Pair<Vertex, Integer>>> graphMap) {
        this.graphMap = graphMap;
    }

    /**
     * This Method adds a Vertex to a Graph
     *
     * @param vertex to be added
     */
    private void addVertex(Vertex vertex) {
        graphMap.put(vertex, new TreeSet<>());
    }

    /**
     * This Method is responsible to add an Edge to the Map
     *
     * @param fromVertex the origin to be added
     * @param toVertex   the destination to be added
     * @param capacity   the capacity between the two
     * @throws SemanticsException throws an Exception if the Adding is not possible
     */
    public void addEdge(Vertex fromVertex, Vertex toVertex, int capacity) throws SemanticsException {

        if (graphMap.containsKey(fromVertex) && graphMap.containsKey(toVertex)) {
            /*
            CHECK OPPOSITE EDGES
             */
            for (Pair<Vertex, Integer> pair : graphMap.get(toVertex)) {
                if (pair.getFirstElement().equals(fromVertex)) {
                    throw new SemanticsException(Errors.CANT_ADD_OPPOSITE_GRAPH_DIRECTION);
                }
            }
            /*
            UPDATE FLOW
             */
            for (Pair<Vertex, Integer> pair : graphMap.get(fromVertex)) {
                if (pair.getFirstElement().equals(toVertex)) {
                    pair.setSecondElement(capacity);

                    return;
                }
            }
        }
        /*
        Add Vertices if necessary
         */
        if (!graphMap.containsKey(fromVertex)) addVertex(fromVertex);
        if (!graphMap.containsKey(toVertex)) addVertex(toVertex);
        /*
        Create new Edge
         */
        graphMap.get(fromVertex).add(new Pair<>(toVertex, capacity));
    }

    /**
     * Getter Method
     *
     * @return number of Vertices of a Graph
     */
    public int getNumberOfVertices() {
        return graphMap.keySet().size();
    }

    /**
     * An Auxiliary method to determine if start and end Vertices are allowed
     *
     * @param start the start Vertex
     * @param end   the end Vertex
     * @throws SemanticsException throws an Error if Point(s) is(are) not Valid
     */
    void checkIfStartAndEndPointIsAllowed(Vertex start, Vertex end) throws SemanticsException {
        if (start.equals(end)) throw new SemanticsException(Errors.POINTS_CANNOT_BE_EQUAL);
        if (!graphMap.containsKey(start)) throw new SemanticsException(Errors.POINT_DOES_NOT_EXIST);
        if (!graphMap.containsKey(end)) throw new SemanticsException(Errors.POINT_DOES_NOT_EXIST);
        if (graphMap.get(start).isEmpty()) throw new SemanticsException(Errors.POINTS_ARE_UNREACHABLE);
        if (!graphMap.get(end).isEmpty()) throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);
        boolean validEndPoint = false;

        for (Set<Pair<Vertex, Integer>> vertexValues : graphMap.values()) {
            for (Pair<Vertex, Integer> pair : vertexValues) {
                if (pair.getFirstElement().equals(start)) {
                    throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);
                }
                if (pair.getFirstElement().equals(end)) validEndPoint = true;
            }
        }

        if (!validEndPoint) throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);
    }

    /**
     * Method which has to check if the Graph does match the requirements
     *
     * @throws SemanticsException throws an Exception if Graph does not meet the requirements
     */
    public void checkIfGraphIsValid() throws SemanticsException {
        boolean validStartPoint = false;
        boolean validEndPoint = false;

        for (Map.Entry<Vertex, Set<Pair<Vertex, Integer>>> vertexSetEntry : graphMap.entrySet()) {
            if (vertexSetEntry.getValue().isEmpty()) {
                validEndPoint = checkIfEndPointIsValid(vertexSetEntry.getKey(), validEndPoint);
            }
            //start vertex
            if (!vertexSetEntry.getValue().isEmpty() && checkIfStartPointIsValid(vertexSetEntry.getKey())) {
                validStartPoint = true;
            }
        }
        if (validEndPoint && validStartPoint) {
            return;
        }
        throw new SemanticsException(Errors.GRAPH_HAS_NO_START_OR_END_POINTS);
    }

    private boolean checkIfStartPointIsValid(Vertex vertex) {
        boolean checkerValidEndPoint = true;
        for (Set<Pair<Vertex, Integer>> vertexValues : graphMap.values()) {
            for (Pair<Vertex, Integer> pair : vertexValues) {
                if (pair.getFirstElement().equals(vertex)) {
                    checkerValidEndPoint = false;
                    break;
                }
            }
        }
        return checkerValidEndPoint;
    }


    private boolean checkIfEndPointIsValid(Vertex vertex, boolean validEndPoint) {
        boolean validationOfEndPoint = validEndPoint;
        if (validationOfEndPoint) return true;
        for (Map.Entry<Vertex, Set<Pair<Vertex, Integer>>> vertexSetEntry : graphMap.entrySet()) {
            for (Pair<Vertex, Integer> pair : vertexSetEntry.getValue()) {
                if (pair.getFirstElement().equals(vertex)) {
                    validationOfEndPoint = true;
                    break;
                }
            }
        }
        return validationOfEndPoint;

    }

    /**
     * To String Method
     *
     * @return the Graph represented as String
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Vertex, Set<Pair<Vertex, Integer>>> vertexSetEntry : graphMap.entrySet()) {
            for (Pair<Vertex, Integer> pair : vertexSetEntry.getValue()) {
                output.append(vertexSetEntry.getKey().toString()).append(pair.getSecondElement())
                        .append(pair.getFirstElement().toString());
                output.append(System.lineSeparator());
            }
        }
        return output.deleteCharAt(output.length() - INDEX_JUMP).toString();
    }

    /**
     * Method which is used to create a deepCopy of the GraphMap
     *
     * @return the deepCopy of the Map
     */
    public Map<Vertex, Set<Pair<Vertex, Integer>>> copyGraph() {
        Map<Vertex, Set<Pair<Vertex, Integer>>> output = new TreeMap<>();
        for (Map.Entry<Vertex, Set<Pair<Vertex, Integer>>> vertexSetEntry : graphMap.entrySet()) {
            Vertex vertex1 = new Vertex(vertexSetEntry.getKey().toString());
            output.put(vertex1, new TreeSet<>());
            for (Pair<Vertex, Integer> pair : vertexSetEntry.getValue()) {
                Pair<Vertex, Integer> pair1 = new Pair<>(new Vertex(pair.getFirstElement().toString())
                        , pair.getSecondElement());
                output.get(vertex1).add(pair1);
            }
        }
        return output;
    }
}
