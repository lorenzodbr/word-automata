package it.univr.wordautomata.backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.model.Model;

public class Backend {
   public static List<Edge<Transition, State>> getPath(String word) {
        List<Edge<Transition, State>> path = new ArrayList<>();
        DigraphEdgeList<State, Transition> graph = (DigraphEdgeList<State, Transition>)Model.getInstance().getGraph();
        Vertex<State> begin = getInitialState(graph);

        return findPath(graph, begin, word, path) ? path : null;
    }

    private static boolean findPath(
        DigraphEdgeList<State, Transition> graph, Vertex<State> v,
        String word, List<Edge<Transition, State>> path) {
        if (v.element().isFinal().get() && word.isEmpty())
            return true;

        // sort the edges in descending order by length
        List<Edge<Transition, State>> outboundEdges = new LinkedList<>(graph.outboundEdges(v));
        outboundEdges.sort((a, b) -> {
            return b.element().getLabel().length() - a.element().getLabel().length();
        });

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

    private static Vertex<State> getInitialState(DigraphEdgeList<State, Transition> graph) {
        return graph.vertices()
                    .stream()
                    .filter(v -> v.element().isInitial().get())
                    .findFirst()
                    .get();
    }
}
