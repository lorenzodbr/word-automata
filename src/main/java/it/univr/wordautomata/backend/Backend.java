package it.univr.wordautomata.backend;

import java.util.ArrayList;
import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;

public class Backend {
    private Vertex<State> begin;
    private DigraphEdgeList<State, Transition> graph;

    public Backend(DigraphEdgeList<State, Transition> graph, Vertex<State> begin) {
        this.graph = graph;
        this.begin = begin;
    }
    
    public ArrayList<Edge<Transition, State>> getPath(String word) {
        /*
        for (Edge<Transition, State> e : p) {
            System.out.print(e.vertices()[0].element());
            System.out.print(" => " + e.element() + " =>");
            System.out.println(e.vertices()[1].element());
        }
         */
        ArrayList<Edge<Transition, State>> p = new ArrayList<>();
        if (search(begin, word, p))
            return p;
        return null;
    }

    private boolean search(Vertex<State> v, String word, Collection<Edge<Transition, State>> c) {
        if (v.element().isFinal().get() && word.isEmpty())
            return true;

        for (Edge<Transition, State> e : graph.outboundEdges(v)) {
            String s = e.element().getLabel();
            if (word.startsWith(s)) {
                Vertex<State> next = e.vertices()[1];
                c.add(e);
                if (search(next, word.substring(s.length()), c)) {
                    // il PDF dice che fra tutte le transizioni possibili, ritornare quella con la parola più lunga e non la prima che trovi come qua
                    return true;
                } else
                    c.remove(e);
            }
        }
        return false;
    }
}
