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
import it.univr.wordautomata.utils.Constants.Orientation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

    @FXML
    private Label zoomLabel;

    private ModalPane modalPane;

    private Graph<State, Transition> graph;
    private SmartGraphPanel<State, Transition> graphView;

    private Timeline timeline;
    private Timeline colorTimeline;

    private Model model;
    private Components components;

    public GraphPanel() {
        Methods.loadAndSetController(Constants.GRAPH_PANEL_FXML_FILENAME, this);

        this.model = Model.getInstance();
        this.components = Components.getInstance();
        this.colorTimeline = new Timeline();
        this.zoomLabel = new Label();
        this.timeline = new Timeline();

        initZoomLabel();
        initGraph();
        initModals();
        initProperties();
        initEdgeColoring();
    }

    private boolean colorNextEdge() {
        if (model.getEdgeToColor().hasNext()) {
            final boolean alreadyColored;

            Edge<Transition, State> e = model.getEdgeToColor().next();

            colorTimeline = new Timeline();
            colorTimeline.rateProperty().bind(timeline.rateProperty());

            final SmartGraphEdge<Transition, State> stylableEdge = graphView.getStylableEdge(e);

            // comment this to see the edge getting recolored when it is already colored
            if (stylableEdge.hasStyleClass(Constants.ACTIVE_EDGE_CLASS)) {
                alreadyColored = true;
            } else {
                alreadyColored = false;
            }

            String cssHorizontal = alreadyColored
                    ? "-fx-stroke: linear-gradient(to %s, -color-danger-4 %d%%, -color-neutral-emphasis %d%%, -color-danger-4 %d%%, -color-danger-4);"
                    : "-fx-stroke: linear-gradient(from %d%% 0%% to %d%% 0%%, -color-danger-4, -color-neutral-emphasis);";

            // add 99 keyframes (percentages of the total duration)
            for (int i = 0; i < 99; i++) {
                int innerIndex = i;
                colorTimeline.getKeyFrames().add(new KeyFrame(
                        // each keyframe is 1% of the total duration
                        Duration.millis((Constants.DEFAULT_PLAYBACK_DURATION_MILLIS / 100) * i), evt -> {
                            String css = alreadyColored
                                    ? "-fx-stroke: linear-gradient(to %s,-color-danger-4 %d%%, -color-neutral-emphasis %d%%, -color-danger-4 %d%%, -color-danger-4);"
                                    : "-fx-stroke: linear-gradient(from 0%% %d%% to 0%% %d%%, -color-danger-4, -color-neutral-emphasis);";

                            // needed because css applies top to bottom, left to right by default
                            Orientation orientation = stylableEdge.getOrientation();
                            switch (orientation) {
                                case Orientation.WEST:
                                    css = cssHorizontal;
                                case Orientation.NORTH:
                                case Orientation.NORTH_EAST:
                                case Orientation.NORTH_WEST:
                                    if (alreadyColored)
                                        css = String.format(css,
                                                orientation.getCssOrientation(),
                                                innerIndex,
                                                innerIndex + 15,
                                                innerIndex + 30);
                                    else
                                        css = String.format(css, 100 - innerIndex, 100 - innerIndex - 1);
                                    break;
                                case Orientation.EAST:
                                    css = cssHorizontal;
                                case Orientation.SOUTH:
                                case Orientation.SOUTH_EAST:
                                case Orientation.SOUTH_WEST:
                                    if (alreadyColored)
                                        css = String.format(css,
                                                orientation.getCssOrientation(),
                                                innerIndex,
                                                innerIndex + 15,
                                                innerIndex + 30);
                                    else
                                        css = String.format(css, innerIndex, innerIndex + 1);
                                    break;
                            }

                            stylableEdge.setStyleInline(css);

                            if (innerIndex == 90) {
                                stylableEdge.addStyleClass(Constants.ACTIVE_EDGE_CLASS);
                                // the children follow the pattern: state - edge - state,
                                // we jump directly to the edge we need to
                                components.getBottomBar()
                                        .colorTransitionButtonAt(model.getEdgeToColor().previousIndex() * 2 + 1);
                            }
                        }));
            }

            // add the last percentage keyframe to add the style class
            colorTimeline.getKeyFrames()
                    .add(new KeyFrame(Duration.millis(Constants.DEFAULT_PLAYBACK_DURATION_MILLIS), e2 -> {
                        stylableEdge.setStyleInline(null);
                    }));

            colorTimeline.play();

            return true;
        }
        return false;
    }

    private void initZoomLabel() {
        zoomLabel.getStyleClass().addAll(Constants.ZOOM_LABEL_CLASS, Constants.ROUNDED_CORNERS_CLASS);
        zoomLabel.visibleProperty().bind(model.atLeastOneVertexProperty());

        getChildren().add(zoomLabel);
        StackPane.setAlignment(zoomLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(zoomLabel, new Insets(7, 7, 0, 0));
    }

    private boolean clearPrevEdge() {
        if (model.getEdgeToColor().hasPrevious()) {
            colorTimeline.stop();

            Edge<Transition, State> e = model.getEdgeToColor().previous();
            SmartGraphEdge<Transition, State> stylableEdge = graphView.getStylableEdge(e);

            components.getBottomBar().clearTransitionButtonAt(model.getEdgeToColor().nextIndex() * 2 + 1);

            stylableEdge.removeStyleClass(Constants.ACTIVE_EDGE_CLASS);
            stylableEdge.setStyleInline(null);

            return true;
        }
        return false;
    }

    private void clearAllEdges() {
        colorTimeline.stop();

        components.getBottomBar().clearTransitionsButtons();

        for (Edge<Transition, State> e : model.getGraph().edges()) {
            SmartGraphEdge<Transition, State> stylableEdge = graphView.getStylableEdge(e);
            while (stylableEdge.hasStyleClass(Constants.ACTIVE_EDGE_CLASS))
                stylableEdge.removeStyleClass(Constants.ACTIVE_EDGE_CLASS);
            stylableEdge.setStyleInline(null);
        }
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

        timeline.rateProperty().bind(Bindings.createDoubleBinding(() -> {
            return model.getSpeed().getValue();
        }, model.playBackSpeedProperty()));

        model.playBackStateProperty().addListener((o, oldVal, newVal) -> {
            if (newVal.equals(Constants.PlayBackState.PAUSED)) {
                timeline.pause();
            } else if (newVal.equals(Constants.PlayBackState.PLAYING)) {
                if (timeline.getKeyFrames().isEmpty()) {
                    timeline.getKeyFrames()
                            .add(new KeyFrame(Duration.millis(Constants.DEFAULT_PLAYBACK_DURATION_MILLIS), e -> {
                                if (!colorNextEdge()) {
                                    timeline.stop();
                                    model.playBackStateProperty().set(Constants.PlayBackState.PAUSED);
                                }
                            }));
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.play();
                } else {
                    // reset everything if:
                    // a) we completed a whole animation, or
                    // b) we are starting a new one
                    if ((timeline.getStatus().equals(Animation.Status.STOPPED) && !model.getEdgeToColor().hasNext())
                            || !model.getEdgeToColor().hasPrevious())
                        resetColoring();
                    timeline.play();
                }
            }
        });

    }

    public void initGraph() {
        timeline.stop();
        colorTimeline.stop();

        this.graph = model.getGraph();

        getChildren().retainAll(hintLabel, zoomLabel, modalPane);
        this.graphView = new SmartGraphPanel<State, Transition>(graph,
                model.getInitialPlacement(),
                model.getAutomaticPlacementStrategy());

        graphView.setVisible(false);

        components.setContentZoomScrollPane(new ContentZoomScrollPane(graphView));
        getChildren().add(1, components.getContentZoomScrollPane());

        graphView.setBackgroundDoubleClickAction(e -> addVertex(e.getSceneX(), e.getSceneY()));
        graphView.setVertexDoubleClickAction(this::showStateSideBar);
        graphView.setEdgeDoubleClickAction(this::showTransitionSideBar);

        initGraphProperties();

        zoomLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            return String.format("%.0f%%", components.getContentZoomScrollPane().scaleFactorProperty().get() * 50 + 50);
        }, components.getContentZoomScrollPane().scaleFactorProperty()));

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
