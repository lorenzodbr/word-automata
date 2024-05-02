package it.univr.wordautomata.model;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Utils.Theme;

/**
 *
 */
public class Model {

    private static Model instance;

    private Theme theme;
    private Graph<State, Transition> graph;
    private State initialState;

    private Model() {
        theme = Theme.DEFAULT;
        graph = new DigraphEdgeList<>();
        initialState = null;
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Theme cycleTheme() {
        Theme next = theme.next();
        instance.setTheme(next);
        return next;
    }

    public Graph<State, Transition> getGraph() {
        return graph;
    }

    private Graph<State, Transition> initSampleGraph() {
        Digraph<State, Transition> g = new DigraphEdgeList<>();

        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        State q4 = new State("q4");

        g.insertVertex(q0);
        g.insertVertex(q1);
        g.insertVertex(q2);
        g.insertVertex(q3);
        g.insertVertex(q4);

        g.insertEdge(q0, q1, new Transition("aab"));
        g.insertEdge(q0, q2, new Transition("abbb"));
        g.insertEdge(q2, q1, new Transition("aba"));
        g.insertEdge(q1, q2, new Transition("b"));
        g.insertEdge(q2, q3, new Transition("b"));
        g.insertEdge(q3, q3, new Transition("abb"));
        g.insertEdge(q3, q3, new Transition("abb2"));

        g.insertEdge(q1, q3, new Transition("a"));
        g.insertEdge(q1, q3, new Transition("a2"));

        g.insertEdge(q3, q4, new Transition("b"));
        g.insertEdge(q4, q1, new Transition("a"));
        g.insertEdge(q4, q2, new Transition("b"));

        return g;
    }

    public void setInitialState(State s) {
        this.initialState = s;
    }

    public State getInitialState() {
        return initialState;
    }
}
