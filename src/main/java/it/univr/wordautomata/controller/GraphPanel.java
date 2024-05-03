package it.univr.wordautomata.controller;

import atlantafx.base.controls.ModalPane;
import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackSpeed;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import it.univr.wordautomata.utils.Methods;
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

    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private ForceDirectedLayoutStrategy<State> automaticPlacementStrategy = new ForceDirectedSpringGravityLayoutStrategy<>();

    private PlayBackSpeed playBackSpeed = PlayBackSpeed.DEFAULT;
    private PlayBackState playBackState = PlayBackState.DEFAULT;

    private SimpleBooleanProperty atLeastOneVertex;
    private SimpleBooleanProperty atLeastOneEdge;
    private SimpleBooleanProperty autoLayout;

    public GraphPanel(MainPanel mainPanel) {
        Methods.loadAndSetController(Constants.GRAPH_PANEL_FXML_FILENAME, this);

        this.mainPanel = mainPanel;
        this.graph = Model.getInstance().getGraph();
        initGraph();
        initProperties();
        initModals();
    }

    private void initGraph() {
        graphView = new SmartGraphPanel<State, Transition>(graph, initialPlacement, automaticPlacementStrategy);
        getChildren().add(new ContentZoomScrollPane(graphView));

        graphView.setBackgroundDoubleClickAction(e -> {
            // only if auto positioning is disabled, place vertices in the clicked point
            if (!autoLayout.get()) {
                addVertex(e.getSceneX(), e.getSceneY());
            } else {
                addVertex();
            }
        });
        graphView.setVertexDoubleClickAction(this::showStateSideBar);
        graphView.setEdgeDoubleClickAction(this::showTransitionSideBar);

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
        this.autoLayout = new SimpleBooleanProperty(Constants.DEFAULT_AUTO_LAYOUT);
        graphView.automaticLayoutProperty().bind(autoLayout);

        this.atLeastOneVertex = new SimpleBooleanProperty(graph.numVertices() > 0);
        this.atLeastOneEdge = new SimpleBooleanProperty(graph.numEdges() > 0);
        this.hintLabel.visibleProperty().bind(atLeastOneVertex.not());
    }

    public boolean addVertex() {
        return addVertex(-1, -1);
    }

    private boolean addVertex(double x, double y) {
        State newState = new AddStateModal(getScene()).showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        atLeastOneVertex.set(true);

        Vertex<State> v = graph.insertVertex(newState);
        graphView.updateAndWait();

        if (newState.isFinal()) {
            graphView.getStylableVertex(v).addStyleClass(Constants.FINAL_STATE_CLASS);
        }

        if (x >= 0 && y >= 0) {
            graphView.setVertexPosition(v, x, y - mainPanel.getMenuBarHeight());
        }

        return true;
    }

    @FXML
    public boolean addEdge() {
        TransitionWrapper newTransition = new AddTransitionModal(getScene()).showAndWait().orElse(null);

        if (newTransition == null) {
            return false;
        }

        atLeastOneEdge.set(true);

        graph.insertEdge(
                newTransition.getStartingState(),
                newTransition.getEndingState(),
                newTransition.getTransition());
        graphView.updateAndWait();
        if (modalPane.isDisplay())
            showStateSideBar(graphView.getVertex(newTransition.getStartingState()));

        return true;
    }

    public void setAutoPositioning(boolean value) {
        autoLayout.set(value);
    }

    public void toggleAutoPositioning() {
        setAutoPositioning(!autoLayout.get());
    }

    public SimpleBooleanProperty autoPositionProperty() {
        return autoLayout;
    }

    public SimpleBooleanProperty atLeastOneVertexProperty() {
        return atLeastOneVertex;
    }

    public SimpleBooleanProperty atLeastOneEdgeProperty() {
        return atLeastOneEdge;
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

    public void setInitialState() {
        SetInitialStateModal modal = new SetInitialStateModal(getScene());

        State newInitialState;
        if ((newInitialState = modal.showAndWait().orElse(null)) != null) {
            State oldInitialState = Model.getInstance().getInitialState();

            if (oldInitialState != null) {
                graphView.getStylableVertex(oldInitialState).removeStyleClass(Constants.INITIAL_STATE_CLASS);
            }

            Model.getInstance().setInitialState(newInitialState);
            graphView.getStylableVertex(newInitialState).addStyleClass(Constants.INITIAL_STATE_CLASS);
        }
    }

    public void selectState() {
        SelectStateModal modal = new SelectStateModal(getScene());

        State s;
        if ((s = modal.showAndWait().orElse(null)) != null) {
            showStateSideBar(graphView.getVertex(s));
        }
    }

    public void selectTransition() {
        SelectTransitionModal modal = new SelectTransitionModal(getScene());

        Transition t;
        if ((t = modal.showAndWait().orElse(null)) != null) {
            showTransitionSideBar(graphView.getEdge(t));
        }
    }

    public void clearGraph() {
        for (var e : graph.edges()) {
            graph.removeEdge(e);
        }
        for (var v : graph.vertices()) {
            graph.removeVertex(v);
        }

        atLeastOneVertex.set(false);
        atLeastOneEdge.set(false);

        graphView.update();
    }

    public void update() {
        graphView.update();
    }

    public void updateAndWait() {
        graphView.updateAndWait();
    }

    private void removeVertex(Vertex<State> v) {
        graph.removeVertex(v);

        if (graph.numVertices() == 0) {
            atLeastOneVertex.set(false);
        }

        if (graph.numEdges() == 0) {
            atLeastOneEdge.set(false);
        }

        graphView.update();
    }

    private void removeEdge(Edge<Transition, State> e) {
        graph.removeEdge(e);

        if (graph.numEdges() == 0) {
            atLeastOneEdge.set(false);
        }

        graphView.update();
    }

    private void showStateSideBar(SmartGraphVertex<State> vertex) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        modalPane.show(getStateModal(vertex));
    }

    private StateModal getStateModal(SmartGraphVertex<State> vertex) {
        StateModal dialog = new StateModal(modalPane, vertex);

        dialog.onTextChange(newString -> {
            graphView.update();
        });

        dialog.setOnClose(e -> {
            graphView.update();
            e.consume();
        });

        dialog.onDeleteButtonClicked(v -> {
            if (Alerts.showConfirmationDialog(getScene(), "Delete", "Do you really want to delete this state?")) {
                dialog.close();
                removeVertex(v);
            }
        });

        return dialog;
    }

    private void showTransitionSideBar(SmartGraphEdge<Transition, State> edge) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        TransitionModal dialog = new TransitionModal(modalPane, edge);

        dialog.onTextChange(newString -> {
            graphView.update();
        });

        dialog.setOnClose(e -> {
            graphView.update();
            e.consume();
        });

        dialog.onDeleteButtonClicked(e -> {
            if (Alerts.showConfirmationDialog(getScene(), "Delete", "Do you really want to delete this transition?")) {
                dialog.close();
                removeEdge(e);
            }
        });

        modalPane.show(dialog);
    }

    public void play() {
        Platform.runLater(() -> {
            if (Model.getInstance().getInitialState() == null) {
                setInitialState();
            }

            mainPanel.getBottomBar().cyclePlayPause();
        });
    }
}
