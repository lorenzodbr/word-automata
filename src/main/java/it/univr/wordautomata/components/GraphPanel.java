package it.univr.wordautomata.components;

import atlantafx.base.controls.ModalPane;
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
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.PlayBackSpeed;
import it.univr.wordautomata.utils.Utils.PlayBackState;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Panel in which Graph is displayed
 */
public class GraphPanel extends StackPane {

    @FXML
    private Label hintLabel;

    private MainPanel mainPanel;
    private ModalPane modalPane;

    private Graph<State, Transition> graph;
    private SmartGraphPanel<State, Transition> graphView;
    private ContentZoomScrollPane graphViewWrapper;

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private PlayBackSpeed playBackSpeed = PlayBackSpeed.DEFAULT;
    private PlayBackState playBackState = PlayBackState.DEFAULT;

    private SimpleBooleanProperty atLeastOneVertex;
    private SimpleBooleanProperty autoLayout;

    public GraphPanel(MainPanel mainPanel) {
        Utils.loadAndSetController(Utils.GRAPH_PANEL_FXML_FILENAME, this);

        this.mainPanel = mainPanel;
        initGraph();
        initModals();
        initProperties();
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
        State newState = new AddStateModal(getScene()).showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        atLeastOneVertex.set(true);

        hintLabel.setVisible(false);
        graphViewWrapper.setVisible(true);

        Vertex<State> v = graph.insertVertex(newState);

        graphView.updateAndWait();

        if (newState.isFinal().get()) {
            graphView.getStylableVertex(v).addStyleClass("final-state");
        }

        return true;
    }

    @FXML
    public boolean addEdge() {
        Collection<State> states = graph.objectsInVertices();

        TransitionWrapper newTransition = new AddTransitionModal(getScene(), states).showAndWait().orElse(null);

        if (newTransition == null) {
            return false;
        }

        graph.insertEdge(newTransition.getStartingState(), newTransition.getEndingState(), newTransition.getTransition());
        graphView.update();

        return true;
    }

    public void setAutoPositioning(boolean value) {
        autoLayout.set(value);
    }

    public void toggleAutoPositioning() {
        autoLayout.setValue(!autoLayout.get());
    }

    public SimpleBooleanProperty autoPositionProperty() {
        return autoLayout;
    }

    public SimpleBooleanProperty atLeastOneVertexProperty() {
        return atLeastOneVertex;
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
        graphView.setVertexDoubleClickAction(this::showStateSideBar);

        Platform.runLater(() -> {
            graphView.init();
        });
    }

    private void initModals() {
        modalPane = new ModalPane();
        modalPane.setAlignment(Pos.TOP_LEFT);
        getChildren().add(modalPane);
    }

    private void initProperties() {
        this.autoLayout = new SimpleBooleanProperty(Utils.DEFAULT_AUTO_LAYOUT);
        graphView.automaticLayoutProperty().bind(autoLayout);

        this.atLeastOneVertex = new SimpleBooleanProperty(false);
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
        atLeastOneVertex.set(false);
    }

    private void showStateSideBar(SmartGraphVertex<State> vertex) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        StateModal dialog = new StateModal(modalPane, vertex);

        dialog.onTextChange(s -> {
            graphView.update();
        });
        dialog.setOnClose(p -> {
            graphView.update();
            p.consume();
        });

        modalPane.show(dialog);
    }
}
