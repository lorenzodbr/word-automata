package it.univr.wordautomata.controller;

import java.io.File;
import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import atlantafx.base.controls.ModalPane;
import it.univr.wordautomata.alerts.Alerts;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.model.TransitionWrapper;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
    private Components components;

    public GraphPanel() {
        Methods.loadAndSetController(Constants.GRAPH_PANEL_FXML_FILENAME, this);

        this.model = Model.getInstance();
        this.components = Components.getInstance();

        initGraph();
        initModals();
        initProperties();
        initEdgeColoring();
    }

    private boolean colorNextEdge() {
        if (model.getEdgeToColor().hasNext()) {
            Edge<Transition, State> e = model.getEdgeToColor().next();

            graphView.getStylableEdge(e).addStyleClass(Constants.ACTIVE_EDGE_CLASS);
            return true;
        }
        return false;
    }

    private boolean clearPrevEdge() {
        if (model.getEdgeToColor().hasPrevious()) {
            Edge<Transition, State> e = model.getEdgeToColor().previous();
            graphView.getStylableEdge(e).removeStyleClass(Constants.ACTIVE_EDGE_CLASS);
            return true;
        }
        return false;
    }

    private void clearAllEdges() {
        for (Edge<Transition, State> e : model.getGraph().edges())
            graphView.getStylableEdge(e).removeStyleClass(Constants.ACTIVE_EDGE_CLASS);
    }

    private void resetColoring() {
        clearAllEdges();
        while (model.getEdgeToColor().hasPrevious())
            model.getEdgeToColor().previous();
    }

    public void initEdgeColoring() {
        model.isPlayNextPressed().addListener((o, oldVal, newVal) -> colorNextEdge());
        model.isPlayPrevPressed().addListener((o, oldVal, newVal) -> clearPrevEdge());
        model.areButtonsEnabled().addListener((o, oldVal, newVal) -> clearAllEdges());
        components.getBottomBar().resetButtonProperty().addListener((o, oldVal, newVal) -> resetColoring());

        model.getTimeline().rateProperty().bind(Bindings.createDoubleBinding(() -> {
            return model.getSpeed().getValue();
        }, model.playBackSpeedProperty()));

        model.playBackStateProperty().addListener((o, oldVal, newVal) -> {
            final Timeline t = model.getTimeline();

            if (newVal.equals(Constants.PlayBackState.PAUSED)) {
                t.pause();
            } else if (newVal.equals(Constants.PlayBackState.PLAYING)) {
                if (t.getKeyFrames().isEmpty()) {
                    t.getKeyFrames().add(new KeyFrame(Duration.millis(500), e -> {
                        if (!colorNextEdge()) {
                            t.stop();
                            model.playBackStateProperty().set(Constants.PlayBackState.PAUSED);
                        }
                    }));
                    t.setCycleCount(Animation.INDEFINITE);
                    t.play();
                } else {
                    if (t.getStatus().equals(Animation.Status.STOPPED) && !model.getEdgeToColor().hasNext())
                        resetColoring();
                    t.play();
                }
            }
        });

    }

    public void initGraph() {
        this.graph = model.getGraph();

        getChildren().retainAll(hintLabel, modalPane);
        this.graphView = new SmartGraphPanel<State, Transition>(graph,
                model.getInitialPlacement(),
                model.getAutomaticPlacementStrategy());

        graphView.setVisible(false);

        getChildren().add(new ContentZoomScrollPane(graphView));

        graphView.setBackgroundDoubleClickAction(e -> addVertex(e.getSceneX(), e.getSceneY()));
        graphView.setVertexDoubleClickAction(this::showStateSideBar);
        graphView.setEdgeDoubleClickAction(this::showTransitionSideBar);

        initGraphProperties();

        Platform.runLater(() -> {
            graphView.init(this);
            graphView.setVisible(true);
            requestFocus();
        });
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
                File file = db.getFiles().get(0);
                if (file.getPath().endsWith(Constants.AUTOMATA_EXTENSION)) {
                    event.consume();

                    Platform.runLater(() -> components.getMainPanel().loadAutomata(file));
                } else {
                    Alerts.showErrorDialog(getScene(), "Error", "Invalid file format",
                            "Only .automata files are accepted.", true);
                }
            }
        });
    }

    private void initGraphProperties() {
        graphView.automaticLayoutProperty().bind(model.autoPositionProperty());
    }

    public boolean addVertex() {
        return addVertex(-1, -1);
    }

    public boolean addVertex(double x, double y) {
        State newState = new AddStateModal(getScene()).showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        Vertex<State> v = graph.insertVertex(newState);

        model.setSaved(false);
        graphView.updateAndWait();
        model.updateGraphProperties();

        if (newState.isFinal()) {
            graphView.getStylableVertex(v).addStyleClass(Constants.FINAL_STATE_CLASS);
        }

        if (graph.numVertices() == 1) {
            graphView.setVertexPosition(v, getWidth() / 2, getHeight() / 2);
            setInitialState(newState);
        } else if (x >= 0 && y >= 0) {
            graphView.setVertexPosition(v, x, y - components.getMainPanel().getMenuBarHeight());
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

        model.setSaved(false);
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
            components.getBottomBar().computePath();
        }
    }

    public void setInitialState(State newInitialState) {
        State oldInitialState = model.getInitialState();

        if (oldInitialState != null) {
            graphView.getStylableVertex(oldInitialState).removeStyleClass(Constants.INITIAL_STATE_CLASS);
        }

        model.setInitialState(newInitialState);
        model.setSaved(false);
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

    public void clear() {
        model.clear();
        graphView.update();
        modalPane.hide();
    }

    public void update() {
        graphView.update();
        components.getBottomBar().computePath();
    }

    public void updateAndWait() {
        graphView.updateAndWait();
    }

    public void removeVertex(Vertex<State> v) {
        graph.removeVertex(v);

        model.setSaved(false);
        model.updateGraphProperties();
        graphView.update();
    }

    public void removeEdge(Edge<Transition, State> e) {
        graph.removeEdge(e);

        model.setSaved(false);
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
