package it.univr.wordautomata.controller;

import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.PlayBackSpeed;
import it.univr.wordautomata.utils.Utils.PlayBackState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.controls.CustomTextField;

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
    private CustomTextField wordInput;

    private MainPanel mainPanel;

    public BottomBar(MainPanel mainPanel) {
        Utils.loadAndSetController(Utils.BOTTOM_BAR_FXML_FILENAME, this);
        this.mainPanel = mainPanel;
        styleButtons();
        styleTransitionsPanel();
    }

    private void styleTransitionsPanel() {
        transitionsPanel.setFitToWidth(true);
    }

    private void styleButtons() {
        initPlayPauseButton();
        initResetButton();
        initSpeedButton();
        stylePreviousStateButton();
        styleNextStateButton();
    }

    private void initPlayPauseButton() {
        PlayBackState state = mainPanel.getGraphPanel().getPlayBackState();
        playPauseButton.disableProperty()
                .bind(mainPanel.getGraphPanel().atLeastOneEdgeProperty().not().or(wordInput.textProperty().isEmpty()));
        playPauseButton
                .setGraphic(new FontIcon(state == PlayBackState.PAUSED ? BoxiconsRegular.PLAY : BoxiconsRegular.PAUSE));
    }

    private void initSpeedButton() {
        speedButtonVBox.disableProperty().bind(speedButton.disableProperty());
        styleSpeedButton();
    }

    private void styleSpeedButton() {
        PlayBackSpeed initialSpeed = mainPanel.getGraphPanel().getSpeed();

        speedLabel.setText(initialSpeed.toString());

        int i = 0;
        for (Node circle : circlesContainer.getChildren()) {
            circle.getStyleClass().remove(Utils.ACTIVE_SPEED_CIRCLE_CLASS);

            if (initialSpeed.ordinal() == i++) {
                circle.getStyleClass().add(Utils.ACTIVE_SPEED_CIRCLE_CLASS);
            }
        }
    }

    private void initResetButton() {
        resetButton.setGraphic(new FontIcon(BoxiconsRegular.RESET));
    }

    private void stylePreviousStateButton() {
        previousStateButton.setGraphic(new FontIcon(BoxiconsRegular.SKIP_PREVIOUS));
    }

    private void styleNextStateButton() {
        nextStateButton.setGraphic(new FontIcon(BoxiconsRegular.SKIP_NEXT));
    }

    @FXML
    private void cycleSpeed() {
        mainPanel.getGraphPanel().cycleSpeed();
        styleSpeedButton();
    }

    @FXML
    public void cyclePlayPause() {
        mainPanel.getGraphPanel().cyclePlayBackState();
        initPlayPauseButton();
    }

    @FXML
    private void checkWord(){
        mainPanel.getGraphPanel().play();
    }
}
