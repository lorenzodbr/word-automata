package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import atlantafx.base.controls.ModalPane;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.backend.AutomataSaver;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.model.TransitionWrapper;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
        initModals();
        initProperties();
    }

    public void initGraph() {
        this.graph = model.getGraph();

        getChildren().remove(graphView);
        graphView = new SmartGraphPanel<State, Transition>(graph,
                model.getInitialPlacement(),
                model.getAutomaticPlacementStrategy());
        getChildren().add(new ContentZoomScrollPane(graphView));

        graphView.setBackgroundDoubleClickAction(e -> addVertex(e.getSceneX(), e.getSceneY()));
        graphView.setVertexDoubleClickAction(this::showStateSideBar);
        graphView.setEdgeDoubleClickAction(this::showTransitionSideBar);

        Platform.runLater(() -> {
            graphView.init();
        });

        initGraphProperties();
    }

    private void initModals() {
        modalPane = new ModalPane();
        modalPane.setAlignment(Pos.TOP_LEFT);
        getChildren().add(modalPane);
    }

    private void initProperties() {
        initGraphProperties();

        this.hintLabel.visibleProperty().bind(model.atLeastOneVertexProperty().not());

        // enable drag-and-drop
        // "this" because we want to drop anywhere in the panel
        this.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });
        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                String filename = db.getFiles().get(0).getPath();
                if (filename.endsWith(Constants.AUTOMATA_EXTENSION)) {
                    model.updateGraph(AutomataSaver.read(filename));
                }
            }
            event.consume();
        });
    }

    private void initGraphProperties() {
        graphView.automaticLayoutProperty().bind(model.autoPositionProperty());
    }

    public boolean addVertex() {
        return addVertex(-1, -1);
    }

    private boolean addVertex(double x, double y) {
        State newState = new AddStateModal(getScene()).showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        Vertex<State> v = graph.insertVertex(newState);
        graphView.updateAndWait();
        model.updateGraphProperties();

        if (newState.isFinal()) {
            graphView.getStylableVertex(v).addStyleClass(Constants.FINAL_STATE_CLASS);
        }

        if (graph.numVertices() == 1) {
            graphView.setVertexPosition(v, getWidth() / 2, getHeight() / 2);
            setInitialState(newState);
        } else if (x >= 0 && y >= 0) {
            graphView.setVertexPosition(v, x, y - controllers.getMainPanel().getMenuBarHeight());
        } else if (!model.isAutoPositioningEnabled()) {
            double xRand = Math.random() * (graphView.getWidth() * 0.8) + graphView.getWidth() * 0.1;
            double yRand = Math.random() * (graphView.getHeight() * 0.8) + graphView.getHeight() * 0.1;

            graphView.setVertexPosition(v, xRand, yRand);
        }

        return true;
    }

    @FXML
    public boolean addEdge() {
        return addEdge(null);
    }

    public boolean addEdge(State startingState) {
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

    public void chooseInitialState() {
        SetInitialStateModal modal = new SetInitialStateModal(getScene());

        State newInitialState;
        if ((newInitialState = modal.showAndWait().orElse(null)) != null) {
            setInitialState(newInitialState);
            controllers.getBottomBar().computePath();
        }
    }

    public void setInitialState(State newInitialState) {
        State oldInitialState = model.getInitialState();

        if (oldInitialState != null) {
            graphView.getStylableVertex(oldInitialState).removeStyleClass(Constants.INITIAL_STATE_CLASS);
        }

        model.setInitialState(newInitialState);
        graphView.getStylableVertex(newInitialState).addStyleClass(Constants.INITIAL_STATE_CLASS);
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
        modalPane.hide();
    }

    public void update() {
        graphView.update();
        controllers.getBottomBar().computePath();
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

    public boolean queryRemoveVertex(Vertex<State> v) {
        if (Alerts.showConfirmationDialog(getScene(), "Delete",
                "Do you really want to delete this state?")) {
            removeVertex(v);
            return true;
        }

        return false;
    }

    public boolean queryRemoveEdge(Edge<Transition, State> e) {
        if (Alerts.showConfirmationDialog(getScene(), "Delete",
                "Do you really want to delete this transition?")) {
            removeEdge(e);
            return true;
        }

        return false;
    }

    public void showStateSideBar(SmartGraphVertex<State> vertex) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        modalPane.show(getStateModal(vertex));
    }

    private StateModal getStateModal(SmartGraphVertex<State> vertex) {
        StateModal dialog = new StateModal(modalPane, vertex);

        return dialog;
    }

    public void showTransitionSideBar(SmartGraphEdge<Transition, State> edge) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        TransitionModal dialog = new TransitionModal(modalPane, edge);
        modalPane.show(dialog);
    }
}
