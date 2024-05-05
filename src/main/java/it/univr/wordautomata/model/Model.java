package it.univr.wordautomata.model;

import java.io.File;
import java.util.Iterator;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackSpeed;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import it.univr.wordautomata.utils.Constants.Theme;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The `Model` class represents the model component of the Word Automata
 * application.
 * It stores the {@link State} of the application, including the theme, graph,
 * initial state,
 * placement strategies, playback speed and state, and various boolean
 * properties.
 * The `Model` class follows the Singleton design pattern to ensure that only
 * one instance
 * of the class exists throughout the application.
 */
public class Model {

    private static Model instance;

    private SimpleObjectProperty<Theme> theme;
    private DigraphEdgeList<State, Transition> graph;
    private State initialState = null;

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private SimpleObjectProperty<File> openedFile;
    private SimpleBooleanProperty saved;

    private SimpleObjectProperty<PlayBackSpeed> playBackSpeed;
    private SimpleObjectProperty<PlayBackState> playBackState;
    private SimpleBooleanProperty isPlayNextPressed;
    private SimpleBooleanProperty areButtonsEnabled;

    private SimpleBooleanProperty atLeastOneVertex;
    private SimpleBooleanProperty atLeastOneEdge;
    private SimpleBooleanProperty autoPosition;
    private SimpleBooleanProperty pathFound;

    private Iterator<Edge<Transition, State>> edgeToColor;

    private Model() {
        // this.graph = initSampleGraph();
        this.graph = new DigraphEdgeList<>();

        this.atLeastOneVertex = new SimpleBooleanProperty(false);
        this.atLeastOneEdge = new SimpleBooleanProperty(false);
        this.autoPosition = new SimpleBooleanProperty(Constants.DEFAULT_AUTO_POSITION);
        this.pathFound = new SimpleBooleanProperty(false);
        this.saved = new SimpleBooleanProperty(true);
        this.openedFile = new SimpleObjectProperty<>(null);
        this.theme = new SimpleObjectProperty<>(Constants.Theme.DEFAULT);
        this.playBackSpeed = new SimpleObjectProperty<>(Constants.PlayBackSpeed.DEFAULT);
        this.playBackState = new SimpleObjectProperty<>(Constants.PlayBackState.PAUSED);
        this.isPlayNextPressed = new SimpleBooleanProperty(false);
        this.areButtonsEnabled = new SimpleBooleanProperty(false);

        updateGraphProperties();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    public SimpleObjectProperty<Theme> themeProperty() {
        return theme;
    }

    public Theme getTheme() {
        return theme.get();
    }

    public void setTheme(Theme theme) {
        this.theme.set(theme);
    }

    public Theme cycleTheme() {
        Theme next = theme.get().next();
        instance.setTheme(next);
        return next;
    }

    public DigraphEdgeList<State, Transition> getGraph() {
        return graph;
    }

    @SuppressWarnings("unused")
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
        return playBackSpeed.get();
    }

    public PlayBackSpeed cycleSpeed() {
        playBackSpeed.set(getSpeed().next());
        return playBackSpeed.get();
    }

    public PlayBackState cyclePlayBackState() {
        playBackState.set(getPlayBackState().next());
        return playBackState.get();
    }

    public PlayBackState getPlayBackState() {
        return playBackState.get();
    }

    public SimpleObjectProperty<PlayBackSpeed> playBackSpeedProperty() {
        return playBackSpeed;
    }

    public SimpleObjectProperty<PlayBackState> playBackStateProperty() {
        return playBackState;
    }

    public SimpleBooleanProperty isPlayNextPressed() {
        return isPlayNextPressed;
    }

    public SimpleBooleanProperty areButtonsEnabled() {
        return areButtonsEnabled;
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
        if(graph == null) {
            return;
        }

        // we must not change the reference of the graph
        this.graph = new DigraphEdgeList<>(graph);
        // for hintLabel
        updateGraphProperties();
        Components.getInstance().getGraphPanel().initGraph();
    }

    public void setOpenedFile(File file) {
        openedFile.set(file);
    }

    public File getOpenedFile() {
        return openedFile.get();
    }

    public SimpleObjectProperty<File> openedFileProperty() {
        return openedFile;
    }

    public void setSaved(boolean value) {
        saved.set(value);
    }

    public SimpleBooleanProperty savedProperty() {
        return saved;
    }

    public boolean isSaved() {
        return saved.get();
    }

    public void clear() {
        for (var e : graph.edges()) {
            graph.removeEdge(e);
        }
        for (var v : graph.vertices()) {
            graph.removeVertex(v);
        }
        
        updateGraphProperties();
        setInitialState(null);
        setSaved(false);
    }

    public Iterator<Edge<Transition, State>> getEdgeToColor() {
        return edgeToColor;
    }

    public void setEdgeToColor(Iterator<Edge<Transition, State>> edgeToColor) {
        this.edgeToColor = edgeToColor;
    }
}
