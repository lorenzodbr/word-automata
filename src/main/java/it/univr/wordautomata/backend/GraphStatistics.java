package it.univr.wordautomata.backend;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;

/**
 * The GraphStatistics class represents the statistics of a graph in a word
 * automaton.
 * It provides methods to compute various statistics such as the longest path,
 * shortest path,
 * possible paths, vertices searched, and edges searched.
 */
public class GraphStatistics {
    private int longestPath;
    private int shortestPath;
    private int possiblePaths;
    private int verticesSearched;
    private int edgesSearched;

    /**
     * Initializes the statistics with default values.
     */
    public GraphStatistics() {
        this.longestPath = 0;
        this.shortestPath = Integer.MAX_VALUE;
        this.possiblePaths = 0;
        this.verticesSearched = 0;
        this.edgesSearched = 0;
    }

    /**
     * Computes the statistics for the given word.
     * This method delegates the computation to the {@link Components} class,
     * retrieving the word from the bottom bar and passing it as a parameter.
     */
    public void computeStats() {
        computeStats(Components.getInstance().getBottomBar().getWord());
    }

    /**
     * Computes statistics for a given word in the graph.
     * 
     * @param word the word for which to compute statistics
     */
    public void computeStats(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();
        Vertex<State> begin = Model.getInstance().getInitialVertex();
        searchFrom(graph, begin, word, 0);
    }

    /**
     * Recursively searches for a word in a directed graph starting from a given vertex.
     *
     * @param graph The directed graph to search in.
     * @param v The starting vertex for the search.
     * @param word The word to search for.
     * @param pathLength The length of the current path.
     */
    private void searchFrom(DigraphEdgeList<State, Transition> graph, Vertex<State> v, String word, int pathLength) {
        verticesSearched++;

        if (word.isEmpty()) {
            if (v.element().isFinal()) {
                longestPath = Math.max(longestPath, pathLength);
                shortestPath = Math.min(shortestPath, pathLength);
                possiblePaths++;
            }
        } else {
            for (Edge<Transition, State> e : graph.outboundEdges(v)) {
                edgesSearched++;

                String s = e.element().getLabel();
                if (word.startsWith(s)) {
                    Vertex<State> next = e.vertices()[1];
                    searchFrom(graph, next, word.substring(s.length()), pathLength + 1);
                }
            }
        }
    }

    /**
     * Returns the number of possible paths in the graph.
     *
     * @return the number of possible paths
     */
    public int getPossiblePaths() {
        return possiblePaths;
    }

    /**
     * Returns the number of vertices searched.
     *
     * @return the number of vertices searched
     */
    public int getVerticesSearched() {
        return verticesSearched;
    }

    /**
     * Returns the number of edges searched.
     *
     * @return the number of edges searched
     */
    public int getEdgesSearched() {
        return edgesSearched;
    }

    /**
     * Returns the length of the longest path in the graph.
     *
     * @return the length of the longest path
     */
    public int getLongestPath() {
        return longestPath;
    }

    /**
     * Returns the shortest path value.
     *
     * @return the shortest path value
     */
    public int getShortestPath() {
        if (shortestPath == Integer.MAX_VALUE)
            return 0;

        return shortestPath;
    }
}