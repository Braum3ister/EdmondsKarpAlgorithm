package edu.kit.stephan.escaperoutes.graphs;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;
import edu.kit.stephan.escaperoutes.utilities.Pair;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
    private static final String EMPTY_FLOW_LIST = "EMPTY";

    private final Map<Vertex, Set<Pair<Vertex, Integer>>> graphMap;

    private int numberOfVertices;
    private List<Flow> flowList;

    /**
     * Constructor of the Graph
     */
    public Graph() {
        graphMap = new TreeMap<>();
        numberOfVertices = 0;
        flowList = new LinkedList<>();
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
        numberOfVertices++;
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
                    flowList = new LinkedList<>();
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
        flowList = new LinkedList<>();
    }

    /**
     * Getter Method
     *
     * @return number of Vertices of a Graph
     */
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * Gets the flow between two Vertices, if the flow has to be calculated it executes the method
     * calculateMaxFlowBetweenTwoPoints, if not it uses the stored value
     *
     * @param start the start Vertex
     * @param end   the end Vertex
     * @return the Flow Value
     * @throws SemanticsException if start or end point are invalid
     */
    public long getMaxFlowBetweenTwoPoints(Vertex start, Vertex end) throws SemanticsException {
        for (Flow flow : flowList) {
            if (flow.getStartVertex().equals(start) && flow.getEndVertex().equals(end)) {
                return flow.getFlowValue();
            }
        }
        long estimatedOutput = calculateMaxFlowBetweenTwoPoints(start, end);
        Flow flow = new Flow(estimatedOutput, start, end);
        flowList.add(flow);
        return estimatedOutput;
    }


    /**
     * Calculates the Max Flow between to given Vertices using the EdmondsKarp Algorithm
     *
     * @param start the start Vertex
     * @param end   the end Vertex
     * @return the calculated Flow Value
     * @throws SemanticsException if start or end point are invalid
     */
    private long calculateMaxFlowBetweenTwoPoints(Vertex start, Vertex end) throws SemanticsException {
        checkIfStartAndEndPointIsAllowed(start, end);
        Map<Vertex, Set<Pair<Vertex, Integer>>> flowGraph = copyGraph();
        Map<Vertex, Vertex> parentMap;
        long output = 0;

        while (true) {
            parentMap = breathForSearch(flowGraph, start, end);
            if (parentMap == null) break;
            int flow = findFlow(parentMap, flowGraph, start, end);
            output += flow;
            updateFlowGraph(flowGraph, start, end, flow, parentMap);
        }
        return output;
    }


    /**
     * Finds a path between two Points, if there is none it returns null
     *
     * @param flowGraph the Graph, in which the algorithm searches the shortest path
     * @param start     the start point
     * @param end       the end point
     * @return a Map which describes the shortest Path between two Points
     */
    private Map<Vertex, Vertex> breathForSearch(Map<Vertex, Set<Pair<Vertex, Integer>>> flowGraph
            , Vertex start, Vertex end) {
        Queue<Vertex> vertexQueue = new ArrayDeque<>();
        Map<Vertex, Vertex> parentMap = new HashMap<>();
        Set<Vertex> visitedSet = new HashSet<>();

        vertexQueue.add(start);
        visitedSet.add(start);
        while (true) {
            Vertex current = vertexQueue.poll();
            if (current == null) return null;
            if (current.equals(end)) return parentMap;

            for (Pair<Vertex, Integer> pair : flowGraph.get(current)) {
                if (!visitedSet.contains(pair.getFirstElement()) && pair.getSecondElement() > 0) {
                    vertexQueue.add(pair.getFirstElement());
                    visitedSet.add(pair.getFirstElement());
                    parentMap.put(pair.getFirstElement(), current);
                }
            }
        }

    }

    /**
     * Method to find the Flow Value on parentMap from Start to End
     *
     * @param parentMap the associated parent map of the BFS
     * @param flowMap   the Map of the currentFlow
     * @param start     the start Point
     * @param end       the end Point
     * @return the flow on the given Path
     */
    private int findFlow(Map<Vertex, Vertex> parentMap, Map<Vertex, Set<Pair<Vertex, Integer>>> flowMap
            , Vertex start, Vertex end) {
        int bottleNeckCapacity = Integer.MAX_VALUE;
        Vertex child = end;
        Vertex parent = parentMap.get(end);

        while (!child.equals(start)) {

            for (Pair<Vertex, Integer> pair : flowMap.get(parent)) {
                if (pair.getFirstElement().equals(child) && pair.getSecondElement() < bottleNeckCapacity) {
                    bottleNeckCapacity = pair.getSecondElement();

                }
            }
            Vertex safeParent = parent;
            parent = parentMap.get(parent);
            child = safeParent;
        }
        return bottleNeckCapacity;
    }

    /**
     * This Method is responsible to update the Flow Graph after a flow was found
     *
     * @param flowMap   the map which is updated
     * @param start     the start vertex
     * @param end       the end vertex
     * @param flow      the flow which was found
     * @param parentMap the associated parent map of the BFS
     */
    private void updateFlowGraph(Map<Vertex, Set<Pair<Vertex, Integer>>> flowMap, Vertex start, Vertex end
            , Integer flow, Map<Vertex, Vertex> parentMap) {
        Vertex current = end;
        while (!current.equals(start)) {

            boolean checker = false;
            Vertex parent = parentMap.get(current);
            //Reduce the flow on the way
            for (Pair<Vertex, Integer> pair : flowMap.get(parent)) {
                if (pair.getFirstElement().equals(current)) {
                    pair.setSecondElement(pair.getSecondElement() - flow);
                }
            }
            //Adding the BackFlow
            for (Pair<Vertex, Integer> pair : flowMap.get(current)) {
                if (pair.getFirstElement().equals(parent)) {
                    pair.setSecondElement(pair.getSecondElement() + flow);
                    checker = true;
                }
            }
            if (!checker) flowMap.get(current).add(new Pair<>(parent, flow));
            current = parent;
        }
    }

    /**
     * An Auxiliary method to determine if start and end Vertices are allowed
     *
     * @param start the start Vertex
     * @param end   the end Vertex
     * @throws SemanticsException throws an Error if Point(s) is(are) not Valid
     */
    private void checkIfStartAndEndPointIsAllowed(Vertex start, Vertex end) throws SemanticsException {
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

    /**
     * Getter Method of the calculated Flow List
     *
     * @return The calculated Paths, if no exist: it returns "EMPTY"
     */
    public String getFlowList() {
        if (flowList.isEmpty()) {
            return EMPTY_FLOW_LIST;
        }
        Collections.sort(flowList);
        StringBuilder output = new StringBuilder();
        for (Flow flow : flowList) {
            output.append(flow.toString()).append(System.lineSeparator());
        }
        return output.deleteCharAt(output.length() - INDEX_JUMP).toString();
    }
}
