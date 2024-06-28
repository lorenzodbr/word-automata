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
import it.univr.wordautomata.utils.Constants.Orientation;
import it.univr.wordautomata.utils.Methods;
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
    private Timeline zoomTimeline;

    private Model model;
    private Components components;

    /**
     * Constructs a new instance of the GraphPanel class.
     * Initializes the GraphPanel by loading the controller, initializing the model,
     * components, timelines,
     * zoom label, graph, modals, properties, and edge coloring.
     */
    public GraphPanel() {
        Methods.loadAndSetController(Constants.GRAPH_PANEL_FXML_FILENAME, this);

        this.model = Model.getInstance();
        this.components = Components.getInstance();
        this.timeline = new Timeline();
        this.colorTimeline = new Timeline();
        this.zoomTimeline = new Timeline();
        this.zoomLabel = new Label();

        initZoomLabel();
        initGraph();
        initModals();
        initProperties();
        initEdgeColoring();
    }

    /**
     * Initializes the timeline for coloring the edges.
     * 
     * @return true if there is an edge to color, false otherwise
     */
    private boolean colorNextEdge() {
        if (model.getEdgesToColor().hasNext()) {
            Edge<Transition, State> e = model.getEdgesToColor().next();

            colorTimeline = new Timeline();
            colorTimeline.rateProperty().bind(timeline.rateProperty());

            components.getBottomBar().isTransitionInProgressProperty()
                    .bind(colorTimeline.statusProperty().isEqualTo(Animation.Status.RUNNING));

            final SmartGraphEdge<Transition, State> stylableEdge = graphView.getStylableEdge(e);

            // comment this to see the edge getting recolored when it is already colored
            final boolean alreadyColored = stylableEdge.hasStyleClass(Constants.ACTIVE_EDGE_CLASS);

            String cssHorizontal = alreadyColored ? Constants.TRANSITION_CSS_HORIZONTAL_ALREADY_COLORED
                    : Constants.TRANSITION_CSS_HORIZONTAL;

            // add 99 keyframes (percentages of the total duration)
            for (int i = 0; i < 99; i++) {
                int innerIndex = i;
                colorTimeline.getKeyFrames().add(new KeyFrame(
                        // each keyframe is 1% of the total duration
                        Duration.millis((Constants.DEFAULT_PLAYBACK_DURATION_MILLIS / 100) * i), evt -> {
                            String css = alreadyColored ? Constants.TRANSITION_CSS_ALREADY_COLORED
                                    : Constants.TRANSITION_CSS;

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
                                        .colorTransitionButtonAt(model.getEdgesToColor().previousIndex() * 2 + 1);
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

    /**
     * Initializes the zoom label.
     */
    private void initZoomLabel() {
        zoomLabel.getStyleClass().addAll(Constants.ZOOM_LABEL_CLASS, Constants.ROUNDED_CORNERS_CLASS);
        zoomLabel.visibleProperty().bind(model.atLeastOneVertexProperty());

        getChildren().add(zoomLabel);
        StackPane.setAlignment(zoomLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(zoomLabel, new Insets(7, 7, 0, 0));
    }

    /**
     * Shows and hides the zoom label.
     */
    private void showAndHideZoomLabel() {
        zoomLabel.setOpacity(0.75);

        if (zoomTimeline != null) {
            zoomTimeline.stop();
            zoomTimeline.getKeyFrames().clear();
        }

        for (int i = 0; i < 100; i++) {
            int innerIndex = i;
            zoomTimeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis((Constants.DEFAULT_PLAYBACK_DURATION_MILLIS / 200) * i + 1000), e -> {
                        zoomLabel.setOpacity(0.75 * (1 - innerIndex / 100.0));
                    }));
        }
        zoomTimeline.play();
    }

    /**
     * Clears the previous edge.
     * 
     * @return true if there is a previous edge to clear, false otherwise
     */
    private boolean clearPrevEdge() {
        if (model.getEdgesToColor().hasPrevious()) {
            colorTimeline.stop();

            Edge<Transition, State> e = model.getEdgesToColor().previous();
            SmartGraphEdge<Transition, State> stylableEdge = graphView.getStylableEdge(e);

            components.getBottomBar().clearTransitionButtonAt(model.getEdgesToColor().nextIndex() * 2 + 1);

            stylableEdge.removeStyleClass(Constants.ACTIVE_EDGE_CLASS);
            stylableEdge.setStyleInline(null);

            return true;
        }
        return false;
    }

    /**
     * Clears all edges.
     */
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

    /**
     * Resets the coloring.
     */
    private void resetColoring() {
        clearAllEdges();
        while (model.getEdgesToColor().hasPrevious())
            model.getEdgesToColor().previous();
    }

    /**
     * Initializes the edge coloring.
     */
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
                    if ((timeline.getStatus().equals(Animation.Status.STOPPED) && !model.getEdgesToColor().hasNext())
                            || !model.getEdgesToColor().hasPrevious())
                        resetColoring();
                    timeline.play();
                }
            }
        });

    }

    /**
     * Initializes the graph.
     */
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

        zoomLabel.setOnMouseEntered(e -> {
            if (zoomLabel.getOpacity() > 0.5) {
                zoomTimeline.stop();
                zoomLabel.setOpacity(0.75);
            }
        });
        zoomLabel.setOnMouseExited(e -> {
            if (zoomLabel.getOpacity() > 0.5)
                zoomTimeline.play();
        });
        zoomLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            return String.format("%.0f%%", components.getContentZoomScrollPane().scaleFactorProperty().get() * 50 + 50);
        }, components.getContentZoomScrollPane().scaleFactorProperty()));

        components.getContentZoomScrollPane().scaleFactorProperty()
                .addListener((o, oldVal, newVal) -> showAndHideZoomLabel());

        showAndHideZoomLabel();

        Platform.runLater(() -> {
            graphView.init(this);
            graphView.setVisible(true);
            requestFocus();
        });
    }

    /**
     * Initializes the modals.
     */
    private void initModals() {
        modalPane = new ModalPane();
        modalPane.setAlignment(Pos.TOP_LEFT);
        getChildren().add(modalPane);
    }

    /**
     * Initializes the properties.
     */
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

                    model.playBackStateProperty().set(Constants.PlayBackState.PAUSED);
                    timeline.getKeyFrames().clear();

                    Platform.runLater(() -> components.getMainPanel().loadAutomata(file));
                } else {
                    Alerts.showErrorDialog(getScene(), "Error", "Invalid file format",
                            "Only .automata files are accepted.", true);
                }
            }
        });
    }

    /**
     * Initializes the graph properties.
     */
    private void initGraphProperties() {
        graphView.automaticLayoutProperty().bind(model.autoPositionProperty());
    }

    /**
     * Adds a new vertex.
     * 
     * @return true if the vertex was added, false otherwise
     */
    public boolean addVertex() {
        return addVertex(-1, -1);
    }

    /**
     * Adds a new vertex.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the vertex was added, false otherwise
     */
    public boolean addVertex(double x, double y) {
        State newState = new AddStateModal().showAndWait().orElse(null);

        if (newState == null) {
            return false;
        }

        Vertex<State> v = graph.insertVertex(newState);

        model.setSaved(false);
        graphView.updateAndWait();
        model.updateGraphProperties();

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

    /**
     * Adds a new edge.
     * 
     * @return true if the edge was added, false otherwise
     */
    @FXML
    public boolean addEdge() {
        return addEdge(null, null);
    }

    /**
     * Adds a new edge.
     * 
     * @param startingState the starting state
     * @return true if the edge was added, false otherwise
     */
    public boolean addEdge(State startingState) {
        return addEdge(startingState, null);
    }

    public boolean addEdge(State startingState, State endingState) {
        TransitionWrapper newTransition = new AddTransitionModal(startingState, endingState).showAndWait()
                .orElse(null);

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

    /**
     * Chooses the initial state.
     */
    public void chooseInitialState() {
        SetInitialStateModal modal = new SetInitialStateModal();

        State newInitialState;
        if ((newInitialState = modal.showAndWait().orElse(null)) != null) {
            setInitialState(newInitialState);
            components.getBottomBar().computePath();
        }
    }

    /**
     * Sets the initial state.
     * 
     * @param newInitialState the new initial state
     */
    public void setInitialState(State newInitialState) {
        State oldInitialState = model.getInitialState();

        if (oldInitialState != null) {
            graphView.getStylableVertex(oldInitialState).removeStyleClass(Constants.INITIAL_STATE_CLASS);
        }

        model.setInitialState(newInitialState);
        model.setSaved(false);
        graphView.getStylableVertex(newInitialState).addStyleClass(Constants.INITIAL_STATE_CLASS);
    }

    /**
     * Selects the state.
     */
    public void selectState() {
        SelectStateModal modal = new SelectStateModal();

        State s;
        if ((s = modal.showAndWait().orElse(null)) != null) {
            showStateSideBar(graphView.getVertex(s));
        }
    }

    /**
     * Selects the transition.
     */
    public void selectTransition() {
        SelectTransitionModal modal = new SelectTransitionModal();

        Transition t;
        if ((t = modal.showAndWait().orElse(null)) != null) {
            showTransitionSideBar(graphView.getEdge(t));
        }
    }

    /**
     * Clears the graph.
     */
    public void clear() {
        model.clear();
        graphView.update();
        closeSideBar();
    }

    /**
     * Closes the side bar.
     */
    public void closeSideBar() {
        modalPane.hide();
    }

    /**
     * Updates the graph.
     */
    public void update() {
        graphView.update();
        components.getBottomBar().computePath();
    }

    /**
     * Updates the graph and waits.
     */
    public void updateAndWait() {
        graphView.updateAndWait();
    }

    /**
     * Removes the vertex.
     * 
     * @param v the vertex
     */
    public void removeVertex(Vertex<State> v) {
        graph.removeVertex(v);

        if(model.getInitialState() == v.element())
            model.setInitialState(null);

        model.setSaved(false);
        model.updateGraphProperties();
        graphView.update();
    }

    /**
     * Removes the edge.
     * 
     * @param e the edge
     */
    public void removeEdge(Edge<Transition, State> e) {
        graph.removeEdge(e);

        model.setSaved(false);
        model.updateGraphProperties();
        graphView.update();
    }

    /**
     * Queries the removal of the vertex.
     * 
     * @param v the vertex
     * @return true if the vertex was removed, false otherwise
     */
    public boolean queryRemoveVertex(Vertex<State> v) {
        if (Alerts.showConfirmationDialog(getScene(), "Delete",
                "Do you really want to delete this state?")) {
            removeVertex(v);
            return true;
        }

        return false;
    }

    /**
     * Queries the removal of the edge.
     * 
     * @param e the edge
     * @return true if the edge was removed, false otherwise
     */
    public boolean queryRemoveEdge(Edge<Transition, State> e) {
        if (Alerts.showConfirmationDialog(getScene(), "Delete",
                "Do you really want to delete this transition?")) {
            removeEdge(e);
            return true;
        }

        return false;
    }

    /**
     * Shows the state side bar.
     * 
     * @param vertex the vertex
     */
    public void showStateSideBar(SmartGraphVertex<State> vertex) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        StateModal dialog = new StateModal(modalPane, vertex);
        modalPane.show(dialog);
    }

    /**
     * Shows the transition side bar.
     * 
     * @param edge the edge
     */
    public void showTransitionSideBar(SmartGraphEdge<Transition, State> edge) {
        modalPane.usePredefinedTransitionFactories(Side.LEFT);
        TransitionModal dialog = new TransitionModal(modalPane, edge);
        modalPane.show(dialog);
    }
}
