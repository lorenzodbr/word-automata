package it.univr.wordautomata.backend;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;

public class GraphStatistics {
    private int possiblePaths;
    private int verticesSearched;
    private int edgesSearched;

    public GraphStatistics() {
        this.possiblePaths = 0;
        this.verticesSearched = 0;
        this.edgesSearched = 0;
    }

    public void getStats() {
        getStats(Components.getInstance().getBottomBar().getWord());
    }

    public void getStats(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        DigraphEdgeList<State, Transition> graph = Model.getInstance().getGraph();
        Vertex<State> begin = Model.getInstance().getInitialVertex();
        searchFrom(graph, begin, word);
    }

    private void searchFrom(DigraphEdgeList<State, Transition> graph, Vertex<State> v, String word) {
        verticesSearched++;

        if (v.element().isFinal() && word.isEmpty()) {
            possiblePaths++;
        } else {
            for (Edge<Transition, State> e : graph.outboundEdges(v)) {
                edgesSearched++;

                String s = e.element().getLabel();
                if (word.startsWith(s)) {
                    Vertex<State> next = e.vertices()[1];
                    searchFrom(graph, next, word.substring(s.length()));
                }
            }
        }
    }

    public int getPossiblePaths() {
        return possiblePaths;
    }

    public int getVerticesSearched() {
        return verticesSearched;
    }

    public int getEdgesSearched() {
        return edgesSearched;
    }
}