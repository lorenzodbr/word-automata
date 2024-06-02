package it.univr.wordautomata.backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The PathFinder class is responsible for finding a path in a graph based on a
 * given word.
 * It uses a depth-first search algorithm to traverse the graph and find a path
 * that matches the word.
 * The class provides methods to retrieve the path, check if the entire word was
 * consumed, and check if the path ended on a final state.
 */
public class PathFinder {
    private final static SimpleBooleanProperty consumedAllWord = new SimpleBooleanProperty(false);
    private final static SimpleBooleanProperty endedOnFinalState = new SimpleBooleanProperty(false);

    /**
     * Finds a path in the graph that matches the given word.
     *
     * @param word  The word to find a path for.
     * @param graph The graph to search
     * @return A list of edges representing the path if found,
     *         or null if no path is found.
     */
    public static List<Edge<Transition, State>> getPath(String word, DigraphEdgeList<State, Transition> graph) {
        List<Edge<Transition, State>> path = new ArrayList<>();
        Vertex<State> begin = Model.getInstance().getInitialVertex();

        clearProperties();

        if (findPath(graph, begin, word, path)) {
            Model.getInstance().setPathFound(true);
            return path;
        } else {
            Model.getInstance().setPathFound(false);
            return null;
        }
    }

    /**
     * Finds a path in the graph that matches the given word
     * 
     * @param word he word to find a path for.
     * @return A list of edges representing the path if found,
     *         or null if no path is found.
     */
    public static List<Edge<Transition, State>> getPath(String word) {
        return getPath(word, Model.getInstance().getGraph());
    }

    /**
     * Finds a path in the given graph starting from the specified vertex and
     * consuming the given word.
     *
     * @param graph The graph representing the automaton.
     * @param v     The starting vertex.
     * @param word  The word to be consumed.
     * @param path  The list to store the edges of the path.
     * @return {@code true} if a path is found and the word is
     *         consumed completely,
     *         {@code false} otherwise.
     */
    private static boolean findPath(
            DigraphEdgeList<State, Transition> graph, Vertex<State> v,
            String word, List<Edge<Transition, State>> path) {

        if (word.isEmpty()) {
            consumedAllWord.set(true);
            endedOnFinalState.set(v.element().isFinal());
            return endedOnFinalState.get();
        }

        // sort the edges in descending order by length
        List<Edge<Transition, State>> outboundEdges = new LinkedList<>(graph.outboundEdges(v));
        outboundEdges.sort((a, b) -> b.element().compareTo(a.element()));

        for (Edge<Transition, State> e : outboundEdges) {
            String s = e.element().getLabel();
            if (word.startsWith(s)) {
                Vertex<State> next = e.vertices()[1];
                path.add(e);
                if (findPath(graph, next, word.substring(s.length()), path))
                    return true;
                path.removeLast();
            }
        }
        return false;
    }

    /**
     * Tells if the entire word was consumed.
     */
    public static SimpleBooleanProperty consumedAllWordProperty() {
        return consumedAllWord;
    }

    /**
     * Tells if the path ended on a final state.
     */
    public static SimpleBooleanProperty endedOnFinalStateProperty() {
        return endedOnFinalState;
    }

    /**
     * Clears the properties of the PathFinder.
     */
    public static void clearProperties() {
        consumedAllWord.set(false);
        endedOnFinalState.set(false);
    }
}
