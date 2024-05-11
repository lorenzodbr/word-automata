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
import javafx.beans.property.ReadOnlyBooleanProperty;
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

    private Circle[] speedCircles;

    private BooleanBinding buttonsEnabledBinding;

    private Model model;

    public BottomBar() {
        Methods.loadAndSetController(Constants.BOTTOM_BAR_FXML_FILENAME, this);

        this.model = Model.getInstance();

        wordInput.disableProperty().bind(model.atLeastOneEdgeProperty().not());
        styleButtons();
        styleTransitionsPanel();

        // ugly
        this.model.areButtonsEnabled().bind(buttonsEnabledBinding);
    }

    private void styleTransitionsPanel() {
        transitionsPanel.setFitToWidth(true);
    }

    private void styleButtons() {
        buttonsEnabledBinding = model.atLeastOneEdgeProperty().not()
                .or(wordInput.textProperty().isEmpty()).or(model.pathFoundProperty().not());

        initPlayPauseButton();
        initResetButton();
        initPreviousNextStateButtons();

        initSpeedButton();
    }

    private void initPlayPauseButton() {
        playPauseButton.disableProperty()
                .bind(buttonsEnabledBinding);
        playPauseButton.graphicProperty()
                .bind(Bindings.when(model.playBackStateProperty().isEqualTo(PlayBackState.PLAYING))
                        .then(new FontIcon(BoxiconsRegular.PAUSE)).otherwise(new FontIcon(BoxiconsRegular.PLAY)));
    }

    private void initSpeedButton() {
        speedButtonVBox.disableProperty().bind(speedButton.disableProperty());
        speedButton.disableProperty().bind(buttonsEnabledBinding);
        speedLabel.textProperty().bind(model.playBackSpeedProperty().asString());

        speedCircles = new Circle[] { firstSpeedCircle, secondSpeedCircle, thirdSpeedCircle };
        styleSpeedButton();
    }

    private void styleSpeedButton() {
        int initialSpeedIndex = model.getSpeed().ordinal();

        speedCircles[initialSpeedIndex].getStyleClass().add(Constants.ACTIVE_SPEED_CIRCLE_CLASS);
        speedCircles[(initialSpeedIndex > 0 ? initialSpeedIndex : speedCircles.length) - 1].getStyleClass()
                .remove(Constants.ACTIVE_SPEED_CIRCLE_CLASS);
    }

    private void initResetButton() {
        resetButton.setGraphic(new FontIcon(BoxiconsRegular.RESET));
        resetButton.disableProperty().bind(buttonsEnabledBinding);
    }

    private void initPreviousNextStateButtons() {
        previousStateButton.disableProperty().bind(buttonsEnabledBinding);
        nextStateButton.disableProperty().bind(buttonsEnabledBinding);
    }

    @FXML
    private void togglePlayNext() {
        model.isPlayNextPressed().set(!model.isPlayNextPressed().get());
    }

    @FXML
    private void togglePlayPrev() {
        model.isPlayPrevPressed().set(!model.isPlayPrevPressed().get());
    }

    @FXML
    private void cycleSpeed() {
        model.cycleSpeed();
        styleSpeedButton();
    }

    @FXML
    public void cyclePlayPause() {
        model.cyclePlayBackState();
    }

    @FXML
    public void computePath() {
        transitionsHint.setVisible(true);
        transitionsPanelHBox.getChildren().clear();

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

    private Label getStateLabel(String stateLabel) {
        Label initialState = new Label(stateLabel.toString());
        initialState.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        initialState.getStyleClass().add(Styles.TEXT_MUTED);

        return initialState;
    }

    private Button getTransitionButton(String edgeLabel) {
        Button transitionButton = new Button(edgeLabel);
        transitionButton.getStyleClass().addAll(Styles.SMALL, Constants.ROUNDED_CORNERS_CLASS);
        transitionButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        transitionButton.setPadding(new Insets(5, 7, 5, 7));

        return transitionButton;
    }

    public void clear() {
        wordInput.clear();
        requestFocus();
        computePath();
        transitionsHint.setVisible(true);
    }

    public ReadOnlyBooleanProperty resetButtonProperty() {
        return resetButton.pressedProperty();
    }

    public void clearTransitionsButtons() {
        transitionsPanelHBox.getChildren()
                .forEach(b -> b.getStyleClass().remove(Constants.ACTIVE_BUTTON_CLASS));

        centerNodeInScrollPane(null);
    }

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

    public void clearTransitionButtonAt(int index) {
        Node b = transitionsPanelHBox.getChildren().get(index);
        b.getStyleClass().remove(Constants.ACTIVE_BUTTON_CLASS);
        centerNodeInScrollPane(b);
    }

    public void colorTransitionButtonAt(int index) {
        Node b = transitionsPanelHBox.getChildren().get(index);
        b.getStyleClass().add(Constants.ACTIVE_BUTTON_CLASS);
        centerNodeInScrollPane(b);
    }
}
