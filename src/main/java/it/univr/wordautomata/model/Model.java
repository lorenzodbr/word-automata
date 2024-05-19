package it.univr.wordautomata.model;

import java.io.File;
import java.util.ListIterator;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.wordautomata.controller.Components;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackSpeed;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import it.univr.wordautomata.utils.Constants.Theme;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The `Model` class represents the model component of the Word Automata
 * application. It stores the {@link State} of the application, including the
 * theme, graph, initial state, placement strategies, playback speed and state,
 * and various boolean properties. * The `Model` class follows the Singleton
 * design pattern to ensure that only one instance of the class exists
 * throughout the application.
 */
public class Model {

    private static Model instance;

    private SimpleObjectProperty<Theme> theme;
    private DigraphEdgeList<State, Transition> graph;
    private SimpleObjectProperty<State> initialState;

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private SimpleObjectProperty<File> openedFile;
    private SimpleBooleanProperty saved;

    private SimpleObjectProperty<PlayBackSpeed> playBackSpeed;
    private SimpleObjectProperty<PlayBackState> playBackState;
    private SimpleBooleanProperty isPlayNextPressed;
    private SimpleBooleanProperty isPlayPrevPressed;
    private SimpleBooleanProperty areButtonsEnabled;

    private SimpleBooleanProperty atLeastOneVertex;
    private SimpleBooleanProperty atLeastOneEdge;
    private SimpleBooleanProperty autoPosition;
    private SimpleBooleanProperty pathFound;

    private ListIterator<Edge<Transition, State>> edgeToColor;
    private final Timeline timeline;

    /**
     * Constructs a new instance of the `Model` class.
     */
    private Model() {
        this.graph = new DigraphEdgeList<>();

        this.atLeastOneVertex = new SimpleBooleanProperty(false);
        this.atLeastOneEdge = new SimpleBooleanProperty(false);
        this.autoPosition = new SimpleBooleanProperty(Constants.DEFAULT_AUTO_POSITION);
        this.pathFound = new SimpleBooleanProperty(false);
        this.saved = new SimpleBooleanProperty(true);
        this.openedFile = new SimpleObjectProperty<>(null);
        this.initialState = new SimpleObjectProperty<>(null);
        this.theme = new SimpleObjectProperty<>(Constants.Theme.DEFAULT);
        this.playBackSpeed = new SimpleObjectProperty<>(Constants.PlayBackSpeed.DEFAULT);
        this.playBackState = new SimpleObjectProperty<>(Constants.PlayBackState.PAUSED);
        this.isPlayNextPressed = new SimpleBooleanProperty(false);
        this.isPlayPrevPressed = new SimpleBooleanProperty(false);
        this.areButtonsEnabled = new SimpleBooleanProperty(false);

        this.timeline = new Timeline();

        updateGraphProperties();
    }

    /**
     * Returns the single instance of the `Model` class.
     *
     * @return the single instance of the `Model` class
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    /**
     * Returns the theme of the application.
     *
     * @return the theme of the application
     */
    public SimpleObjectProperty<Theme> themeProperty() {
        return theme;
    }

    /**
     * Returns the theme of the application.
     *
     * @return the theme of the application
     */
    public Theme getTheme() {
        return theme.get();
    }

    /**
     * Sets the theme of the application.
     *
     * @param theme the theme to set
     */
    public void setTheme(Theme theme) {
        this.theme.set(theme);
    }

    /**
     * Cycles the theme of the application.
     *
     * @return the next theme
     */
    public Theme cycleTheme() {
        Theme next = theme.get().next();
        instance.setTheme(next);
        return next;
    }

    /**
     * Returns the graph of the application.
     *
     * @return the graph of the application
     */
    public DigraphEdgeList<State, Transition> getGraph() {
        return graph;
    }

    /**
     * Initializes a sample graph for the application.
     *
     * @return the sample graph
     */
    public DigraphEdgeList<State, Transition> initSampleGraph() {
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

    /**
     * Sets the initial state of the application.
     *
     * @param s the initial state to set
     */
    public void setInitialState(State s) {
        this.initialState.set(s);
    }

    /**
     * Returns the initial state of the application.
     *
     * @return the initial state of the application
     */
    public State getInitialState() {
        return initialState.get();
    }

    /**
     * Returns the initial vertex of the application.
     *
     * @return the initial vertex of the application
     */
    public Vertex<State> getInitialVertex() {
        return graph.vertices()
                .stream()
                .filter(v -> getInitialState().equals(v.element()))
                .findFirst()
                .get();
    }

    /**
     * Returns the initial state property of the application.
     *
     * @return the initial state property of the application
     */
    public SimpleObjectProperty<State> initialStateProperty() {
        return initialState;
    }

    /**
     * Returns the initial placement strategy of the application.
     *
     * @return the initial placement strategy of the application
     */
    public SmartPlacementStrategy getInitialPlacement() {
        return initialPlacement;
    }

    /**
     * Returns the automatic placement strategy of the application.
     *
     * @return the automatic placement strategy of the application
     */
    public ForceDirectedLayoutStrategy<State> getAutomaticPlacementStrategy() {
        return automaticPlacementStrategy;
    }

    /**
     * Returns the playback speed of the application.
     *
     * @return the playback speed of the application
     */
    public PlayBackSpeed getSpeed() {
        return playBackSpeed.get();
    }

    /**
     * Returns the next playback speed of the application.
     *
     * @return the next playback speed of the application
     */
    public PlayBackSpeed cycleSpeed() {
        playBackSpeed.set(getSpeed().next());
        return playBackSpeed.get();
    }

    /**
     * Returns the next playback state of the application.
     *
     * @return the next playback state of the application
     */
    public PlayBackState cyclePlayBackState() {
        playBackState.set(getPlayBackState().next());
        return playBackState.get();
    }

    /**
     * Returns the playback state of the application.
     *
     * @return the playback state of the application
     */
    public PlayBackState getPlayBackState() {
        return playBackState.get();
    }

    /**
     * Returns the playback speed property of the application.
     *
     * @return the playback speed property of the application
     */
    public SimpleObjectProperty<PlayBackSpeed> playBackSpeedProperty() {
        return playBackSpeed;
    }

    /**
     * Returns the playback state property of the application.
     *
     * @return the playback state property of the application
     */
    public SimpleObjectProperty<PlayBackState> playBackStateProperty() {
        return playBackState;
    }

    /**
     * Returns the play next pressed property of the application.
     *
     * @return the play next pressed property of the application
     */
    public SimpleBooleanProperty isPlayNextPressed() {
        return isPlayNextPressed;
    }

    /**
     * Returns the play previous pressed property of the application.
     *
     * @return the play previous pressed property of the application
     */
    public SimpleBooleanProperty isPlayPrevPressed() {
        return isPlayPrevPressed;
    }

    /**
     * Returns the buttons enabled property of the application.
     *
     * @return the buttons enabled property of the application
     */
    public SimpleBooleanProperty areButtonsEnabled() {
        return areButtonsEnabled;
    }

    /**
     * Sets the auto position property of the application.
     * 
     * @param value the value to set
     */
    public void setAutoPosition(boolean value) {
        autoPosition.set(value);
    }

    /**
     * Returns the auto position property of the application.
     *
     * @return the auto position property of the application
     */
    public SimpleBooleanProperty autoPositionProperty() {
        return autoPosition;
    }

    /**
     * Check if the auto position property of the application is enabled.
     *
     * @return true if the auto position property is enabled, false otherwise
     */
    public boolean isAutoPositioningEnabled() {
        return autoPosition.get();
    }

    /**
     * Toggles the auto position property of the application.
     */
    public void toggleAutoPositioning() {
        autoPosition.set(!autoPosition.get());
    }

    /**
     * Returns the at least one vertex property of the application.
     *
     * @return the at least one vertex property of the application
     */
    public SimpleBooleanProperty atLeastOneVertexProperty() {
        return atLeastOneVertex;
    }

    /**
     * Returns the at least one edge property of the application.
     *
     * @return the at least one edge property of the application
     */
    public SimpleBooleanProperty atLeastOneEdgeProperty() {
        return atLeastOneEdge;
    }

    /**
     * Check if the application has at least one vertex.
     *
     * @return true if the application has at least one vertex, false otherwise
     */
    public boolean hasAtLeastOneVertex() {
        return atLeastOneVertex.get();
    }

    /**
     * Check if the application has at least one edge.
     *
     * @return true if the application has at least one edge, false otherwise
     */
    public boolean hasAtLeastOneEdge() {
        return atLeastOneEdge.get();
    }

    /**
     * Updates the graph properties of the application.
     */
    public void updateGraphProperties() {
        atLeastOneVertex.set(graph.numVertices() > 0);
        atLeastOneEdge.set(graph.numEdges() > 0);
    }

    /**
     * Returns the path found property of the application.
     *
     * @return the path found property of the application
     */
    public SimpleBooleanProperty pathFoundProperty() {
        return pathFound;
    }

    /**
     * Check if the application has found a path.
     *
     * @return true if the application has found a path, false otherwise
     */
    public boolean hasPathFound() {
        return pathFound.get();
    }

    /**
     * Sets the path found property of the application.
     *
     * @param value the value to set
     */
    public void setPathFound(boolean value) {
        pathFound.set(value);
    }

    /**
     * Updates the graph of the application.
     *
     * @param graph the graph to set
     */
    public void updateGraph(DigraphEdgeList<State, Transition> graph) {
        if (graph == null) {
            return;
        }

        this.graph = new DigraphEdgeList<>(graph);
        updateGraphProperties();
        Components.getInstance().getGraphPanel().initGraph();
        Model.getInstance().setSaved(true);
    }

    /**
     * Sets the opened file of the application.
     *
     * @param file the file to set
     */
    public void setOpenedFile(File file) {
        openedFile.set(file);
    }

    /**
     * Returns the opened file of the application.
     *
     * @return the opened file of the application
     */
    public File getOpenedFile() {
        return openedFile.get();
    }

    /**
     * Returns the opened file property of the application.
     *
     * @return the opened file property of the application
     */
    public SimpleObjectProperty<File> openedFileProperty() {
        return openedFile;
    }

    /**
     * Sets the saved property of the application.
     *
     * @param value the value to set
     */
    public void setSaved(boolean value) {
        saved.set(value);
    }

    /**
     * Returns the saved property of the application.
     *
     * @return the saved property of the application
     */
    public SimpleBooleanProperty savedProperty() {
        return saved;
    }

    /**
     * Check if the application has been saved.
     *
     * @return true if the application has been saved, false otherwise
     */
    public boolean isSaved() {
        return saved.get();
    }

    /**
     * Clears the graph of the application.
     */
    public void clear() {
        for (var e : graph.edges()) {
            graph.removeEdge(e);
        }
        for (var v : graph.vertices()) {
            graph.removeVertex(v);
        }

        updateGraphProperties();
        setInitialState(null);

        if (getOpenedFile() != null)
            setSaved(false);
    }

    /**
     * Returns the edge to color of the application.
     *
     * @return the edge to color of the application
     */
    public ListIterator<Edge<Transition, State>> getEdgeToColor() {
        return edgeToColor;
    }

    /**
     * Sets the edge to color of the application.
     *
     * @param edgeToColor set the edge to color
     */
    public void setEdgeToColor(ListIterator<Edge<Transition, State>> edgeToColor) {
        this.edgeToColor = edgeToColor;
    }

    /**
     * Returns the timeline of the application.
     *
     * @return the timeline of the application
     */
    public Timeline getTimeline() {
        return timeline;
    }
}
