package edu.kit.stephan.escaperoutes.graphs;

import edu.kit.stephan.escaperoutes.errors.Errors;
import edu.kit.stephan.escaperoutes.errors.SemanticsException;
import edu.kit.stephan.escaperoutes.utilities.Pair;


import java.util.*;

/**
 * The Class describes a Graph of an EscapeNetwork and manages the Graph
 * @see Flow
 * @see Vertex
 * @author Johannes Stephan
 * @version 1.0
 */
public class Graph {
    private static final String EMPTY_FLOW_LIST = "EMPTY";

    private final Map<Vertex, Set<Pair<Vertex, Integer>>> graph;

    private int numberOfVertices;
    private List<Flow> flowList;

    /**
     * Constructor of the Graph
     */
    public Graph() {
        graph = new TreeMap<>();
        numberOfVertices = 0;
        flowList = new LinkedList<>();
    }

    /**
     * Constructor of Graph with pre set Graph Map
     * @param graph the map which should be preset
     */
    public Graph(Map<Vertex, Set<Pair<Vertex, Integer>>> graph) {
        this.graph = graph;
    }

    /**
     * This Method adds a Vertex to a Graph
     * @param vertex to be added
     */
    private void addVertex(Vertex vertex) {
        graph.put(vertex, new TreeSet<>());
        numberOfVertices++;
    }

    /**
     * This Method is responsible to add an Edge to the Map
     * @param fromVertex the origin to be added
     * @param toVertex the destination to be added
     * @param capacity the capacity between the two
     * @throws SemanticsException throws an Exception if the Adding is not possible
     */
    public void addEdge(Vertex fromVertex, Vertex toVertex, int capacity) throws SemanticsException {

        if (graph.containsKey(fromVertex) && graph.containsKey(toVertex)) {
            for (Pair<Vertex, Integer> pair : graph.get(toVertex)) {
                if (pair.getFirstElement().equals(fromVertex)) {
                    throw new SemanticsException(Errors.CANT_ADD_OPPOSITE_GRAPH_DIRECTION);
                }
            }

            for (Pair<Vertex, Integer> pair : graph.get(fromVertex)) {
                if (pair.getFirstElement().equals(toVertex)) {
                    pair.setSecondElement(capacity);
                    flowList = new LinkedList<>();
                    return;
                }
            }
        }
        if (!graph.containsKey(fromVertex)) addVertex(fromVertex);
        if (!graph.containsKey(toVertex)) addVertex(toVertex);
        graph.get(fromVertex).add(new Pair<>(toVertex, capacity));
        flowList = new LinkedList<>();
    }

    /**
     * Getter Method
     * @return number of Vertices of a Graph
     */
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * Gets the flow between two Vertices, if the flow has to be calculated it executes the method
     * calculateMaxFlowBetweenTwoPoints
     * @param start the start Vertex
     * @param end the end Vertex
     * @return the Flow Value
     * @throws SemanticsException if start or end point are invalid
     */
    public long getMaxFlowBetweenTwoPoints(Vertex start, Vertex end) throws SemanticsException {
        for (Flow flow : flowList) {
            if (flow.getStartVertex().equals(start) && flow.getEndVertex().equals(end)) {
                return flow.getFlow();
            }
        }
        long estimatedOutput = calculateMaxFlowBetweenTwoPoints(start, end);
        Flow addFlow = new Flow(estimatedOutput, start, end);
        flowList.add(addFlow);
        return estimatedOutput;
    }


    /**
     * Calculates the Max Flow between to given Vertices using the EdmondsKarp Algorithm
     * @param start the start Vertex
     * @param end the end Vertex
     * @return the calculated Flow Value
     * @throws SemanticsException if start or end point are invalid
     */
    private long calculateMaxFlowBetweenTwoPoints(Vertex start, Vertex end) throws SemanticsException {
        checkIfStartAndEndPointIsAllowed(start, end);
        Map<Vertex, Set<Pair<Vertex, Integer>>> flowGraph = copyGraph();
        Queue<Vertex> vertexQueue = new ArrayDeque<>();
        Map<Vertex, Vertex> parentMap = new LinkedHashMap<>();
        Set<Vertex> visitedSet = new LinkedHashSet<>();


        boolean terminated = false;
        long output = 0;


        while (!terminated) {
            vertexQueue.add(start);
            while (true) {
                Vertex current = vertexQueue.peek();

                if (current == null) terminated = true;
                if (current == null) break;
                if (current.equals(end)) break;
                current = vertexQueue.poll();
                visitedSet.add(current);

                for (Pair<Vertex, Integer> pair : flowGraph.get(current)) {
                    if (!visitedSet.contains(pair.getFirstElement())) {
                        if (pair.getSecondElement() > 0) {
                            vertexQueue.add(pair.getFirstElement());
                            visitedSet.add(pair.getFirstElement());
                            parentMap.put(pair.getFirstElement(), current);
                        }
                    }
                }
            }
            if (!terminated) {
                int flow = findFlow(parentMap, flowGraph, start, end);
                output += flow;
                updateFlowGraph(flowGraph, start, end, flow, parentMap);
                visitedSet.clear();
                parentMap.clear();
                vertexQueue.clear();
            }
        }
        return output;
    }

    /**
     * Method to find the Flow Value on parentMap from Start to End
     * @param parentMap the associated parent map of the BFS
     * @param flowMap the Map of the currentFlow
     * @param start the start Point
     * @param end the end Point
     * @return the flow on the given Path
     */
    private int findFlow(Map<Vertex, Vertex> parentMap, Map<Vertex, Set<Pair<Vertex, Integer>>> flowMap
            , Vertex start, Vertex end) {
        int outputCapacity = Integer.MAX_VALUE;
        Vertex child = end;
        Vertex parent = parentMap.get(end);

        while (!child.equals(start))  {

            for (Pair<Vertex, Integer> pair : flowMap.get(parent)) {
                if (pair.getFirstElement().equals(child)) {
                    if (pair.getSecondElement() < outputCapacity) {
                        outputCapacity = pair.getSecondElement();
                    }
                }
            }
            Vertex safeParent = parent;
            parent = parentMap.get(parent);
            child = safeParent;
        }
        return outputCapacity;
    }

    /**
     * This Method is responsible to update the Flow Graph after a flow was found
     * @param flowMap the map which is updated
     * @param start the start vertex
     * @param end the end vertex
     * @param flow the flow which was found
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
     * @param start the start Vertex
     * @param end the end Vertex
     * @throws SemanticsException throws an Error if Point(s) is(are) not Valid
     */
    private void checkIfStartAndEndPointIsAllowed(Vertex start, Vertex end) throws SemanticsException {
        if (start.equals(end)) throw new SemanticsException(Errors.POINTS_CANNOT_BE_EQUAL);
        if (!graph.containsKey(start)) throw new SemanticsException(Errors.POINT_DOES_NOT_EXIST);
        if (!graph.containsKey(end)) throw new SemanticsException(Errors.POINT_DOES_NOT_EXIST);
        if (graph.get(start).isEmpty()) throw new SemanticsException(Errors.POINTS_ARE_UNREACHABLE);
        if (!graph.get(end).isEmpty()) throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);

        for (Vertex vertex : graph.keySet()) {
            for (Pair<Vertex, Integer> pair : graph.get(vertex)) {
                if (pair.getFirstElement().equals(start)) {
                    throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);
                }
            }
        }
        for (Vertex vertex : graph.keySet()) {
            for (Pair<Vertex, Integer> pair : graph.get(vertex)) {
                if (pair.getFirstElement().equals(end)) return;
            }
        }
        throw new SemanticsException(Errors.POINTS_DONT_MEET_REQUIREMENTS);
    }

    /**
     * Method which has to check if the Graph does match the requirements
     * @throws SemanticsException throws an Exception if Graph does not meet the requirements
     */
    public void checkIfGraphIsValid() throws SemanticsException {
        boolean validStartPoint = false;
        boolean validEndPoint = false;

        for (Vertex vertex : graph.keySet()) {
            //end vertex
            if (graph.get(vertex).isEmpty()) {

                for (Vertex vertex1 : graph.keySet()) {
                    for (Pair<Vertex, Integer> pair : graph.get(vertex1)) {
                        if (pair.getFirstElement().equals(vertex)) {
                            validEndPoint = true;
                            break;
                        }
                    }
                }
            }
            //start vertex
            if (!graph.get(vertex).isEmpty()) {
                boolean checker = true;
                for (Vertex vertex1 : graph.keySet()) {
                    for (Pair<Vertex, Integer> pair : graph.get(vertex1)) {
                        if (pair.getFirstElement().equals(vertex)) {
                            checker = false;
                            break;
                        }
                    }
                }
                if (checker) {
                    validStartPoint = true;
                }
            }
        }
        if (validEndPoint && validStartPoint) {
            return;
        }
        throw new SemanticsException(Errors.GRAPH_HAS_NO_START_OR_END_POINTS);
    }

    /**
     * To String Method
     * @return the Graph represented as String
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Vertex vertex : graph.keySet()) {
            for (Pair<Vertex, Integer> pair : graph.get(vertex)) {
                output.append(vertex.toString()).append(pair.getSecondElement())
                        .append(pair.getFirstElement().toString());
                output.append("\n");
            }
        }
        return output.deleteCharAt(output.length() - 1).toString();
    }

    /**
     * Method which is used to create a deepCopy of the GraphMap
     * @return the deepCopy of the Map
     */
    public Map<Vertex, Set<Pair<Vertex, Integer>>> copyGraph() {
        Map<Vertex, Set<Pair<Vertex, Integer>>> output = new TreeMap<>();
        for (Vertex v : graph.keySet()) {
            Vertex v1 = new Vertex(v.toString());
            output.put(v1, new TreeSet<>());
            for (Pair<Vertex, Integer> pair : graph.get(v)) {
                Pair<Vertex, Integer> pair1 = new Pair<>(new Vertex(pair.getFirstElement().toString())
                        , pair.getSecondElement());
                output.get(v1).add(pair1);
            }
        }
        return output;
    }

    /**
     * Getter Method of the calculated Flow List
     * @return The calculated Paths, if no exist: it returns "EMPTY"
     */
    public String getFlowList() {
        if (flowList.isEmpty()) {
            return EMPTY_FLOW_LIST;
        }
        Collections.sort(flowList);
        StringBuilder output = new StringBuilder();
        for (Flow flow : flowList) {
            output.append(flow.toString()).append("\n");
        }
        return output.deleteCharAt(output.length() - 1).toString();
    }
}
