package edu.kit.stephan.escaperoutes.useless;

import edu.kit.stephan.escaperoutes.utilities.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphPattern<T, S> {

    private Map<T, List<Pair<T, S>>> graph = new HashMap<>();


    public void addVertex(T vertex) {
        graph.put(vertex, new LinkedList<Pair<T, S>>());
    }

    public void addEdge(T fromVertex, T toVertex, S weight) {
        if (!graph.containsKey(fromVertex)) addVertex(fromVertex);
        if (!graph.containsKey(toVertex)) addVertex(toVertex);
        graph.get(fromVertex).add(new Pair<>(toVertex, weight));
    }


    private boolean hasEdge(T fromVertex, Pair<S, T> toVertex) {
        return graph.get(fromVertex).contains(toVertex);
    }

    private boolean hasVertex(T inputVertex) {
        return graph.containsKey(inputVertex);
    }

    public void deleteVertex(T inputVertex) {
        if (hasVertex(inputVertex)) {
            graph.remove(inputVertex);

            for (T vertex: graph.keySet()) {
                for (Pair<T, S> pair: graph.get(vertex)) {
                    if (pair.getFirstElement().equals(inputVertex)) {
                        graph.get(vertex).remove(pair);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (T vertex : graph.keySet()) {
            output.append(vertex.toString()).append(":");

            for (Pair<T, S> pair : graph.get(vertex)) {
                output.append("\n");
                output.append("\t").append(pair.getSecondElement().toString()).append("->").
                        append(pair.getFirstElement().toString());
            }
            output.append("\n");
        }

        return output.toString();

    }

    /*
    V = {A,B,C,....}
    E = {(A,B), (B,C), (A,C), (A,D)....}
    E = {edge, edge1, edge2, edge3}



    g = {A -> {(B,cap), (C, cap), (D, cap)}, {B -> {(A, cap), (D, cap),...}

    (0 0 0
    1 0 1
    2 2 0)

     */


}
