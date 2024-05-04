package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.PathFinder;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Methods;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Constants.PlayBackSpeed;
import it.univr.wordautomata.utils.Constants.PlayBackState;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    private BooleanBinding buttonsEnabledBinding;

    private Model model;

    public BottomBar() {
        Methods.loadAndSetController(Constants.BOTTOM_BAR_FXML_FILENAME, this);
        
        this.model = Model.getInstance();

        wordInput.disableProperty().bind(model.atLeastOneEdgeProperty().not());
        styleButtons();
        styleTransitionsPanel();
    }

    private void styleTransitionsPanel() {
        transitionsPanel.setFitToWidth(true);
    }

    private void styleButtons() {
        buttonsEnabledBinding = model.atLeastOneEdgeProperty().not()
                .or(wordInput.textProperty().isEmpty()).or(model.pathFoundProperty().not());

        initPlayPauseButton();
        initResetButton();
        initSpeedButton();
        initPreviousNextStateButtons();
    }

    private void initPlayPauseButton() {
        PlayBackState state = model.getPlayBackState();
        playPauseButton.disableProperty()
                .bind(buttonsEnabledBinding);
        playPauseButton
                .setGraphic(new FontIcon(state == PlayBackState.PAUSED ? BoxiconsRegular.PLAY : BoxiconsRegular.PAUSE));
    }

    private void initSpeedButton() {
        speedButtonVBox.disableProperty().bind(speedButton.disableProperty());
        speedButton.disableProperty().bind(buttonsEnabledBinding);
        styleSpeedButton();
    }

    private void styleSpeedButton() {
        PlayBackSpeed initialSpeed = model.getSpeed();

        speedLabel.setText(initialSpeed.toString());

        int i = 0;
        for (Node circle : circlesContainer.getChildren()) {
            circle.getStyleClass().remove(Constants.ACTIVE_SPEED_CIRCLE_CLASS);

            if (initialSpeed.ordinal() == i++) {
                circle.getStyleClass().add(Constants.ACTIVE_SPEED_CIRCLE_CLASS);
            }
        }
    }

    private void initResetButton() {
        resetButton.setGraphic(new FontIcon(BoxiconsRegular.RESET));
        resetButton.disableProperty().bind(buttonsEnabledBinding);
    }

    private void initPreviousNextStateButtons() {
        previousStateButton.disableProperty().bind(buttonsEnabledBinding);
        nextStateButton.disableProperty().bind(buttonsEnabledBinding);
        stylePreviousStateButton();
        styleNextStateButton();
    }

    private void stylePreviousStateButton() {
        previousStateButton.setGraphic(new FontIcon(BoxiconsRegular.SKIP_PREVIOUS));
    }

    private void styleNextStateButton() {
        nextStateButton.setGraphic(new FontIcon(BoxiconsRegular.SKIP_NEXT));
    }

    @FXML
    private void cycleSpeed() {
        model.cycleSpeed();
        styleSpeedButton();
    }

    @FXML
    public void cyclePlayPause() {
        model.cyclePlayBackState();
        initPlayPauseButton();
    }

    @FXML
    private void checkWord() {
        Platform.runLater(() -> {
            if (model.getInitialState() == null) {
                Controllers.getInstance().getGraphPanel().chooseInitialState();
            }

            computePath();
            cyclePlayPause();
        });
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
            transitionsHint.setText("Select an initial state first");
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

    public void clear(){
        wordInput.clear();
        requestFocus();
        computePath();
        transitionsHint.setVisible(true);
    }
}
