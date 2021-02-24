package edu.kit.stephan.escaperoutes.graphs;

import java.util.Objects;

/**
 * This class describes a Flow between two Points
 * @author Johannes Stephan
 * @version 1.0
 */
public class Flow implements Comparable<Flow> {
    private static final String OUTPUT_TO_STRING = "%s %s %s";
    private final Vertex startVertex;
    private final Vertex endVertex;
    private final long flow;

    /**
     * Constructor
     * @param flow the flow Value
     * @param startVertex the start Vertex
     * @param endVertex the end Vertex
     */
    public Flow(long flow, Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.flow = flow;
    }

    /**
     * Returns a Copy of the Start Vertex
     * @return the copied start Vertex
     */
    public Vertex getStartVertex() {
        return new Vertex(startVertex.toString());
    }

    /**
     * Returns a Copy of the EndVertex
     * @return the copied start Vertex
     */
    public Vertex getEndVertex() {
        return new Vertex(endVertex.toString());
    }

    /**
     * Returns the Flow
     * @return theFlow
     */
    public long getFlow() {
        return flow;
    }

    /**
     * To String Method
     * @return the String value of the Object Flow
     */
    @Override
    public String toString() {
        return String.format(OUTPUT_TO_STRING, flow, startVertex.toString(), endVertex.toString());

    }

    /**
     * Comparable
     * @param flow the flow to be compared
     * @return the sortable int Value
     */
    @Override
    public int compareTo(Flow flow) {
        if (this.flow != flow.getFlow()) return Long.compare(this.flow, flow.getFlow());
        if (!startVertex.toString().equals(flow.startVertex.toString())) {
            return startVertex.toString().compareTo(flow.getStartVertex().toString());
        }
        return endVertex.toString().compareTo(flow.getEndVertex().toString());
    }

    /**
     * Equals Method
     * @param o the objectToCheck
     * @return true, if they are equal; false, if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flow flow1 = (Flow) o;
        return flow == flow1.flow && startVertex.toString().equals(((Flow) o).getStartVertex().toString())
                && endVertex.toString().equals(((Flow) o).getEndVertex().toString());
    }

    /**
     * HashCode of the Object
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(startVertex, endVertex, flow);
    }
}
