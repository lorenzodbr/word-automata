package it.univr.wordautomata.components;

import java.util.Collection;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.PlayBackSpeed;
import it.univr.wordautomata.utils.Utils.PlayBackState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Panel in which Graph is displayed
 */
public class GraphPanel extends StackPane {

    @FXML
    private Label hintLabel;

    private Graph<State, Transition> graph;
    private SmartGraphPanel<State, Transition> graphView;
    private ContentZoomScrollPane graphViewWrapper;

    private MainPanel mainPanel;

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private PlayBackSpeed playBackSpeed = PlayBackSpeed.DEFAULT;
    private PlayBackState playBackState = PlayBackState.DEFAULT;

    private boolean wasGraphAdded = false;
    private boolean autoLayout = true;

    public GraphPanel(MainPanel mainPanel) {
        Utils.loadAndSetController(Utils.GRAPH_PANEL_FXML_FILENAME, this);
        initGraph();

        this.mainPanel = mainPanel;
    }

    // Sample graph building method
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

    @FXML
    public boolean addVertex() {
        State newState = Utils.showAddStateModal(getScene());

        if (newState == null) {
            return false;
        }

        mainPanel.setClearGraphMenuItemEnabled(true);
        mainPanel.setAddTransitionMenuItemEnabled(true);
        hintLabel.setVisible(false);
        graphViewWrapper.setVisible(true);

        Vertex<State> v = graph.insertVertex(newState);

        graphView.updateAndWait();

        if (newState.isFinal()) {
            graphView.getStylableVertex(v).addStyleClass("final-state");
        }

        return true;
    }

    @FXML
    public boolean addEdge() {
        Collection<State> states = graph.objectsInVertices();

        TransitionWrapper newTransition = Utils.showAddTransitionModal(getScene(), states);

        if (newTransition == null) {
            return false;
        }

        graph.insertEdge(newTransition.getStartingState(), newTransition.getEndingState(), newTransition.getTransition());
        graphView.update();
        
        return true;
    }

    public void setAutoPositioning(boolean value) {
        graphView.setAutomaticLayout((autoLayout = value));
    }

    public boolean getAutoPositioningEnabled() {
        return autoLayout;
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

    private void initGraph() {
        graph = new DigraphEdgeList<>();
        graphView = new SmartGraphPanel<State, Transition>(graph, initialPlacement, automaticPlacementStrategy);
        getChildren().add(graphViewWrapper = new ContentZoomScrollPane(graphView));

        graphView.setBackgroundDoubleClickAction(p -> addVertex());

        Platform.runLater(() -> {
            graphView.init();
            setAutoPositioning(autoLayout);

            graphView.setVertexDoubleClickAction(mainPanel::showVertexDetails);
        });
    }

    public void clearGraph() {
        for (var e : graph.edges()) {
            graph.removeEdge(e);
        }
        for (var v : graph.vertices()) {
            graph.removeVertex(v);
        }

        graphView.update();
        hintLabel.setVisible(true);
    }
}
