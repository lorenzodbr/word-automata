package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.PathFinder;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Methods;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import com.brunomnsilva.smartgraph.graph.Edge;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;

/**
 * BottomBar: contains a textfield to input words, buttons that control
 * automata playback capabilities and a list of transitions.
 */
public class BottomBar extends GridPane {

    @FXML
    private Button playPauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button speedButton;

    @FXML
    private HBox circlesContainer;

    @FXML
    private Label speedLabel;

    @FXML
    private VBox speedButtonVBox;

    @FXML
    private Button previousStateButton;

    @FXML
    private Button nextStateButton;

    @FXML
    private ScrollPane transitionsPanel;

    @FXML
    private HBox transitionsPanelHBox;

    public HBox getTransitionsPanelHBox() {
        return transitionsPanelHBox;
    }


    @FXML
    private CustomTextField wordInput;

    @FXML
    private Label transitionsHint;

    @FXML
    private Circle firstSpeedCircle;

    @FXML
    private Circle secondSpeedCircle;

    @FXML
    private Circle thirdSpeedCircle;

    @FXML
    private Button whyButton;

    @FXML
    private Button mockWhyButton;

    private WhyModal whyModal;

    private Circle[] speedCircles;

    private BooleanBinding buttonsEnabledBinding;
    private SimpleBooleanProperty isTransitionInProgress;

    private Model model;

    /**
     * Creates a new {@code BottomBar}.
     */
    public BottomBar() {
        Methods.loadAndSetController(Constants.BOTTOM_BAR_FXML_FILENAME, this);

        this.model = Model.getInstance();
        this.isTransitionInProgress = new SimpleBooleanProperty(false);

        wordInput.disableProperty().bind(model.atLeastOneEdgeProperty().not());
        styleButtons();
        styleTransitionsPanel();
        bindWidth();

        // ugly
        this.model.areButtonsEnabled().bind(buttonsEnabledBinding);
    }

    /**
     * Styles the transitions panel.
     */
    private void styleTransitionsPanel() {
        transitionsPanel.setFitToWidth(true);
    }

    /**
     * Styles the buttons.
     */
    private void styleButtons() {
        buttonsEnabledBinding = model.atLeastOneEdgeProperty().not()
                .or(wordInput.textProperty().isEmpty()).or(model.pathFoundProperty().not());

        initPlayPauseButton();
        initResetButton();
        initPreviousNextStateButtons();

        initSpeedButton();
    }

    /**
     * Set the width of the textfield and the transitions panel equal to half of
     * the width of the parent.
     */
    private void bindWidth() {
        double extraSizes = playPauseButton.getWidth() + // widest button
                resetButton.getWidth() * 4 + // 4 normal sized buttons
                this.getHgap() * 6 + // 6 gaps between buttons
                15 * 2; // 15 of padding on both sides
        DoubleBinding width = this.widthProperty().subtract(extraSizes).divide(2);

        wordInput.prefWidthProperty().bind(width);
        transitionsPanel.prefWidthProperty().bind(width);
    }

    /**
     * Initializes the play/pause button.
     */
    private void initPlayPauseButton() {
        playPauseButton.disableProperty()
                .bind(buttonsEnabledBinding);
        playPauseButton.graphicProperty()
                .bind(Bindings.when(model.playBackStateProperty().isEqualTo(PlayBackState.PLAYING))
                        .then(new FontIcon(BoxiconsRegular.PAUSE)).otherwise(new FontIcon(BoxiconsRegular.PLAY)));
    }

    /**
     * Initializes the speed button.
     */
    private void initSpeedButton() {
        speedButtonVBox.disableProperty().bind(speedButton.disableProperty());
        speedButton.disableProperty().bind(buttonsEnabledBinding);
        speedLabel.textProperty().bind(model.playBackSpeedProperty().asString());

        speedCircles = new Circle[] { firstSpeedCircle, secondSpeedCircle, thirdSpeedCircle };
        styleSpeedButton();
    }

    /**
     * Styles the speed button.
     */
    private void styleSpeedButton() {
        int initialSpeedIndex = model.getSpeed().ordinal();

        speedCircles[initialSpeedIndex].getStyleClass().add(Constants.ACTIVE_SPEED_CIRCLE_CLASS);
        speedCircles[(initialSpeedIndex > 0 ? initialSpeedIndex : speedCircles.length) - 1].getStyleClass()
                .remove(Constants.ACTIVE_SPEED_CIRCLE_CLASS);
    }

    /**
     * Initializes the reset button.
     */
    private void initResetButton() {
        resetButton.setGraphic(new FontIcon(BoxiconsRegular.RESET));
        resetButton.disableProperty().bind(buttonsEnabledBinding);
        resetButton.pressedProperty().addListener((o, oldVal, newVal) -> clearTransitionsButtons());
    }

    /**
     * Initializes the previous and next state buttons.
     */
    private void initPreviousNextStateButtons() {
        previousStateButton.disableProperty()
                .bind(buttonsEnabledBinding
                        .or(model.playBackStateProperty().isEqualTo(PlayBackState.PLAYING))
                        .or(isTransitionInProgress));
        nextStateButton.disableProperty()
                .bind(buttonsEnabledBinding
                        .or(model.playBackStateProperty().isEqualTo(PlayBackState.PLAYING))
                        .or(isTransitionInProgress));
    }

    /**
     * Toggles the play/pause button.
     */
    @FXML
    private void togglePlayNext() {
        model.isPlayNextPressed().set(!model.isPlayNextPressed().get());
    }

    /**
     * Toggles the play/prev button.
     */
    @FXML
    private void togglePlayPrev() {
        model.isPlayPrevPressed().set(!model.isPlayPrevPressed().get());
    }

    /**
     * Cycles the speed.
     */
    @FXML
    private void cycleSpeed() {
        model.cycleSpeed();
        styleSpeedButton();
    }

    /**
     * Cycles the play/pause state.
     */
    @FXML
    public void cyclePlayPause() {
        model.cyclePlayBackState();
    }

    /**
     * Computes the path of the word.
     */
    @FXML
    public void computePath() {
        transitionsHint.setVisible(true);
        whyButton.setManaged(false);
        whyButton.setVisible(false);
        mockWhyButton.setManaged(false);
        transitionsHint.getStyleClass().remove(Constants.NO_PATH_FOUND_TEXT_CLASS);
        transitionsPanelHBox.getChildren().clear();
        transitionsPanel.getStyleClass().remove(Constants.NO_PATH_FOUND_PANEL_CLASS);
        PathFinder.clearProperties();

        if (wordInput.getText().isEmpty()) {
            transitionsHint.setText("Waiting for a word");
            return;
        }

        if (model.getInitialState() == null) {
            transitionsHint.setText("Set an initial state");
            return;
        }

        if (!model.getGraph().objectsInVertices().stream().anyMatch(v -> v.isFinal())) {
            transitionsHint.setText("Set at least one final state");
            return;
        }

        Platform.runLater(() -> {
            List<Edge<Transition, State>> path = PathFinder.getPath(wordInput.getText());

            if (path == null) {
                transitionsHint.setText("No path found");
                whyButton.setManaged(true);
                whyButton.setVisible(true);
                mockWhyButton.setManaged(true);
                transitionsHint.getStyleClass().add(Constants.NO_PATH_FOUND_TEXT_CLASS);
                transitionsPanel.getStyleClass().add(Constants.NO_PATH_FOUND_PANEL_CLASS);
                model.pathFoundProperty().set(false);
                return;
            }

            model.pathFoundProperty().set(true);
            model.setEdgeToColor(path.listIterator());

            transitionsHint.setVisible(false);
            transitionsPanelHBox.getChildren().add(getStateLabel(model.getInitialState().toString()));

            for (var e : path) {
                transitionsPanelHBox.getChildren().addAll(
                        getTransitionButton(e.element().toString()),
                        getStateLabel(((State) e.vertices()[1].element()).toString()));
            }
        });
    }

    /**
     * Gets a state label.
     *
     * @param stateLabel the state label
     * @return the initial state label
     */
    private Label getStateLabel(String stateLabel) {
        Label initialState = new Label(stateLabel.toString());
        initialState.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        initialState.getStyleClass().add(Styles.TEXT_MUTED);

        return initialState;
    }

    /**
     * Gets a transition button.
     *
     * @param edgeLabel the edge label
     * @return the transition button
     */
    private Button getTransitionButton(String edgeLabel) {
        Button transitionButton = new Button(edgeLabel);
        transitionButton.getStyleClass().addAll(Styles.SMALL, Constants.ROUNDED_CORNERS_CLASS);
        transitionButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        transitionButton.setPadding(new Insets(5, 7, 5, 7));

        return transitionButton;
    }

    /**
     * Clears the word input.
     */
    public void clear() {
        wordInput.clear();
        requestFocus();
        computePath();
        transitionsHint.setVisible(true);
    }

    /**
     * Returns the property that indicates whether the reset button is pressed.
     * 
     * @return the property that indicates whether the reset button is pressed
     */
    public ReadOnlyBooleanProperty resetButtonProperty() {
        return resetButton.pressedProperty();
    }

    /**
     * Clears the transitions buttons.
     */
    public void clearTransitionsButtons() {
        transitionsPanelHBox.getChildren()
                .forEach(b -> b.getStyleClass().remove(Constants.ACTIVE_BUTTON_CLASS));

        centerNodeInScrollPane(null);
    }

    /**
     * Centers a node in the scroll pane.
     *
     * @param node the node to center
     */
    public void centerNodeInScrollPane(Node node) {
        double f = 0, w, x, v;

        if (node != null) {
            w = transitionsPanel.getContent().getBoundsInLocal().getWidth();
            x = (node.getBoundsInParent().getMaxX() +
                    node.getBoundsInParent().getMinX()) / 2.0;
            v = transitionsPanel.getViewportBounds().getWidth();

            f = transitionsPanel.getHmax() * ((x - 0.5 * v) / (w - v));
        }

        KeyValue keyValue = new KeyValue(transitionsPanel.hvalueProperty(), f);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(Constants.DEFAULT_PLAYBACK_DURATION_MILLIS / 2), keyValue);
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    /**
     * Clears the transition button at the specified index.
     *
     * @param index the index of the button
     */
    public void clearTransitionButtonAt(int index) {
        Node b = transitionsPanelHBox.getChildren().get(index);
        b.getStyleClass().remove(Constants.ACTIVE_BUTTON_CLASS);
        centerNodeInScrollPane(b);
    }

    /**
     * Colors the transition button at the specified index.
     *
     * @param index the index of the button
     */
    public void colorTransitionButtonAt(int index) {
        List<Node> children = transitionsPanelHBox.getChildren();

        if (index < 0 || index >= children.size()) {
            return;
        }

        Node b = children.get(index);
        b.getStyleClass().add(Constants.ACTIVE_BUTTON_CLASS);
        centerNodeInScrollPane(b);
    }

    /**
     * Returns the property that indicates if the transition is in progress.
     * 
     * @return the property that indicates if the transition is in progress
     */
    public SimpleBooleanProperty isTransitionInProgressProperty() {
        return isTransitionInProgress;
    }

    /**
     * Returns the previous state button.
     * 
     * @return the previous state button
     */
    public Button getPreviousStateButton() {
        return previousStateButton;
    }

    /**
     * Returns the next state button.
     * 
     * @return the next state button
     */
    public Button getNextStateButton() {
        return nextStateButton;
    }

    /**
     * Returns the word input.
     * 
     * @return the word input
     */
    public String getWord() {
        return wordInput.getText();
    }


    /**
     * Displays the "Why" modal window.
     * If the modal window has not been created yet, it creates a new instance of the WhyModal class.
     * The "Why" button is disabled while the modal window is being shown.
     */
    @FXML
    public void showWhy() {
        if (whyModal == null) {
            whyModal = new WhyModal();
            whyButton.disableProperty().bind(whyModal.showingProperty());
        }

        whyModal.show();
    }
}
