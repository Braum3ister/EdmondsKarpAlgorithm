package edu.kit.stephan.escaperoutes.graphs;

import java.util.Objects;

/**
 * This class describes a Vertex
 * @author Johannes Stephan
 * @version 1.0
 */
public class Vertex implements Comparable<Vertex> {
    private final String nameOfVertex;


    /**
     * Constructor
     * @param nameOfVertex the name of the Vertex
     */
    public Vertex(String nameOfVertex) {
        this.nameOfVertex = nameOfVertex;
    }

    /**
     * Equals Method
     * @param o the Object to be compared
     * @return true, if Objects are equal; false, if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(nameOfVertex, vertex.nameOfVertex);
    }

    /**
     * HashCodeMethod
     * @return the HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(nameOfVertex);
    }

    /**
     * To-StringMethod
     * @return the name of The Vertex
     */
    @Override
    public String toString() {
        return nameOfVertex;
    }

    /**
     * Comparable
     * @param vertex objectToCompare
     * @return the comparable Integer
     */
    @Override
    public int compareTo(Vertex vertex) {
        return this.nameOfVertex.compareTo(vertex.nameOfVertex);
    }

}

