package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import atlantafx.base.controls.ModalPane;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
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

    private ModalPane modalPane;

    private Graph<State, Transition> graph;
    private SmartGraphPanel<State, Transition> graphView;

    private Model model;
    private Controllers controllers;

    public GraphPanel() {
        Methods.loadAndSetController(Constants.GRAPH_PANEL_FXML_FILENAME, this);

        this.model = Model.getInstance();
        this.controllers = Controllers.getInstance();

        initGraph();
        initProperties();
        initModals();
    }

    private void initGraph() {
        this.graph = model.getGraph();

        graphView = new SmartGraphPanel<State, Transition>(graph, model.getInitialPlacement(),
                model.getAutomaticPlacementStrategy());
        getChildren().add(new ContentZoomScrollPane(graphView));

        graphView.setBackgroundDoubleClickAction(e -> {
            // only if auto positioning is disabled, place vertices in the clicked point
            if (!model.isAutoPositioningEnabled()) {
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
        graphView.automaticLayoutProperty().bind(model.autoPositionProperty());
        this.hintLabel.visibleProperty().bind(model.atLeastOneVertexProperty().not());
    }

    public boolean addVertex() {
        return addVertex(-1, -1);
    }

    private boolean addVertex(double x, double y) {
        State newState = new AddStateModal(getScene()).showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        model.atLeastOneVertexProperty().set(true);

        Vertex<State> v = graph.insertVertex(newState);
        graphView.updateAndWait();

        if (newState.isFinal()) {
            graphView.getStylableVertex(v).addStyleClass(Constants.FINAL_STATE_CLASS);
        }

        if (x >= 0 && y >= 0) {
            graphView.setVertexPosition(v, x, y - controllers.getMainPanel().getMenuBarHeight());
        } else {
            graphView.setVertexPosition(v, getWidth() / 2, getHeight() / 2);
        }

        return true;
    }

    public boolean addEdge(State startingState){
        return _addEdge(startingState);
    }

    @FXML
    public boolean addEdge() {
        return _addEdge(null);
    }

    private boolean _addEdge(State startingState) {
        TransitionWrapper newTransition = new AddTransitionModal(getScene(), startingState).showAndWait().orElse(null);

        if (newTransition == null) {
            return false;
        }

        graph.insertEdge(
                newTransition.getStartingState(),
                newTransition.getEndingState(),
                newTransition.getTransition());

        model.updateGraphProperties();
        graphView.updateAndWait();

        if (modalPane.isDisplay())
            showStateSideBar(graphView.getVertex(newTransition.getStartingState()));

        return true;
    }

    public void setInitialState() {
        SetInitialStateModal modal = new SetInitialStateModal(getScene());

        State newInitialState;
        if ((newInitialState = modal.showAndWait().orElse(null)) != null) {
            State oldInitialState = model.getInitialState();

            if (oldInitialState != null) {
                graphView.getStylableVertex(oldInitialState).removeStyleClass(Constants.INITIAL_STATE_CLASS);
            }

            model.setInitialState(newInitialState);
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

        model.updateGraphProperties();
        graphView.update();
    }

    public void update() {
        graphView.update();
    }

    public void updateAndWait() {
        graphView.updateAndWait();
    }

    public void removeVertex(Vertex<State> v) {
        graph.removeVertex(v);

        model.updateGraphProperties();
        graphView.update();
    }

    public void removeEdge(Edge<Transition, State> e) {
        graph.removeEdge(e);

        model.updateGraphProperties();
        graphView.update();
    }

    private void showStateSideBar(SmartGraphVertex<State> vertex) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        modalPane.show(getStateModal(vertex));
    }

    private StateModal getStateModal(SmartGraphVertex<State> vertex) {
        StateModal dialog = new StateModal(modalPane, vertex);

        return dialog;
    }

    private void showTransitionSideBar(SmartGraphEdge<Transition, State> edge) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        TransitionModal dialog = new TransitionModal(modalPane, edge);
        modalPane.show(dialog);
    }

    public void play() {
        Platform.runLater(() -> {
            if (model.getInitialState() == null) {
                setInitialState();
            }

            controllers.getBottomBar().cyclePlayPause();
        });
    }
}
