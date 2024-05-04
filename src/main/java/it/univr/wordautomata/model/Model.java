package it.univr.wordautomata.model;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackSpeed;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import it.univr.wordautomata.utils.Constants.Theme;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The `Model` class represents the model component of the Word Automata application.
 * It stores the {@link State} of the application, including the theme, graph, initial state,
 * placement strategies, playback speed and state, and various boolean properties.
 * The `Model` class follows the Singleton design pattern to ensure that only one instance
 * of the class exists throughout the application.
 */
public class Model {

    private static Model instance;

    private Theme theme;
    private DigraphEdgeList<State, Transition> graph;
    private State initialState = null;

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private PlayBackSpeed playBackSpeed = PlayBackSpeed.DEFAULT;
    private PlayBackState playBackState = PlayBackState.DEFAULT;

    private SimpleBooleanProperty atLeastOneVertex;
    private SimpleBooleanProperty atLeastOneEdge;
    private SimpleBooleanProperty autoPosition;
    private SimpleBooleanProperty pathFound;

    private Model() {
        this.theme = Theme.DEFAULT;
        this.graph = initSampleGraph();

        this.atLeastOneVertex = new SimpleBooleanProperty(false);
        this.atLeastOneEdge = new SimpleBooleanProperty(false);
        this.autoPosition = new SimpleBooleanProperty(Constants.DEFAULT_AUTO_POSITION);
        this.pathFound = new SimpleBooleanProperty(false);

        updateGraphProperties();
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

    public DigraphEdgeList<State, Transition> getGraph() {
        return graph;
    }

    private DigraphEdgeList<State, Transition> initSampleGraph() {
        DigraphEdgeList<State, Transition> g = new DigraphEdgeList<>();

        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3", true);
        State q4 = new State("q4", true);

        setInitialState(q0);

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

        g.insertEdge(q1, q3, new Transition("a"));

        g.insertEdge(q3, q4, new Transition("bb"));
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

    public SmartPlacementStrategy getInitialPlacement() {
        return initialPlacement;
    }

    public ForceDirectedLayoutStrategy<State> getAutomaticPlacementStrategy() {
        return automaticPlacementStrategy;
    }

    public PlayBackSpeed getSpeed() {
        return playBackSpeed;
    }

    public PlayBackSpeed cycleSpeed() {
        return (playBackSpeed = playBackSpeed.next());
    }

    public PlayBackState cyclePlayBackState() {
        return (playBackState = playBackState.next());
    }

    public PlayBackState getPlayBackState() {
        return playBackState;
    }

    public void setAutoPosition(boolean value) {
        autoPosition.set(value);
    }

    public SimpleBooleanProperty autoPositionProperty() {
        return autoPosition;
    }

    public boolean isAutoPositioningEnabled() {
        return autoPosition.get();
    }

    public void toggleAutoPositioning() {
        autoPosition.set(!autoPosition.get());
    }

    public SimpleBooleanProperty atLeastOneVertexProperty() {
        return atLeastOneVertex;
    }

    public SimpleBooleanProperty atLeastOneEdgeProperty() {
        return atLeastOneEdge;
    }

    public boolean hasAtLeastOneVertex() {
        return atLeastOneVertex.get();
    }

    public boolean hasAtLeastOneEdge() {
        return atLeastOneEdge.get();
    }

    public void updateGraphProperties() {
        atLeastOneVertex.set(graph.numVertices() > 0);
        atLeastOneEdge.set(graph.numEdges() > 0);
    }

    public SimpleBooleanProperty pathFoundProperty() {
        return pathFound;
    }

    public boolean hasPathFound() {
        return pathFound.get();
    }

    public void setPathFound(boolean value) {
        pathFound.set(value);
    }

    public void updateGraph(DigraphEdgeList<State, Transition> graph) {
        // we must not change the reference of the graph
        this.graph.copy(graph);
        // for hintLabel
        this.atLeastOneVertex.set(this.graph.numVertices() > 0);
        this.atLeastOneEdge.set(this.graph.numEdges() > 0);
    }
}
