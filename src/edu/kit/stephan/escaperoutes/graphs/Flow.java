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
    private final long flowValue;

    /**
     * Constructor
     * @param flowValue the flow Value
     * @param startVertex the start Vertex
     * @param endVertex the end Vertex
     */
    public Flow(long flowValue, Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.flowValue = flowValue;
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
    public long getFlowValue() {
        return flowValue;
    }

    /**
     * To String Method
     * @return the String value of the Object Flow
     */
    @Override
    public String toString() {
        return String.format(OUTPUT_TO_STRING, flowValue, startVertex.toString(), endVertex.toString());

    }

    /**
     * Comparable
     * @param flow the flow to be compared
     * @return the sortable int Value
     */
    @Override
    public int compareTo(Flow flow) {
        if (this.flowValue != flow.getFlowValue()) return Long.compare(this.flowValue, flow.getFlowValue());
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
        return flowValue == flow1.flowValue && startVertex.toString().equals(((Flow) o).getStartVertex().toString())
                && endVertex.toString().equals(((Flow) o).getEndVertex().toString());
    }

    /**
     * HashCode of the Object
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(startVertex, endVertex, flowValue);
    }
}
