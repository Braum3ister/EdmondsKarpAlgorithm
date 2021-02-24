package edu.kit.stephan.escaperoutes.graphs;

import java.util.Objects;

/**
 * This class describes a Edge in the Graph
 * @author Johannes Stephan
 * @version 1.0
 * @see Vertex
 */
public class Edge {
    private final Vertex fromVertex;
    private final Vertex toVertex;
    private final int capacityFlow;

    /**
     * Constructor of an Edge
     * @param fromVertex the origin of an Edge
     * @param toVertex the destination of an Edge
     * @param capacityFlow the capacity of the Edge
     */
    public Edge(Vertex fromVertex, Vertex toVertex, int capacityFlow) {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.capacityFlow = capacityFlow;
    }

    /**
     * Getter-Method
     * @return the origin Vertex
     */
    public Vertex getFromVertex() {
        return fromVertex;
    }

    /**
     * Getter-Method
     * @return the destination Vertex
     */
    public Vertex getToVertex() {
        return toVertex;
    }

    /**
     * Getter-Method
     * @return the capacity
     */
    public int getCapacityFlow() {
        return capacityFlow;
    }

    /**
     * Equals-Method
     * @param o the Object which should be compared
     * @return boolean: true, if Objects are equal, false if Objects are not equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(fromVertex, edge.fromVertex) && Objects.equals(toVertex, edge.toVertex);
    }

    /**
     * Hash-Code Method
     * @return the HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(fromVertex, toVertex);
    }

    /**
     * toString Method
     * @return the Object represented as String
     */
    @Override
    public String toString() {
        return fromVertex.toString() + capacityFlow + toVertex.toString();
    }
}
