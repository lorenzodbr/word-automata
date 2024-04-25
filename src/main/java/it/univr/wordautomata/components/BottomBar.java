package it.univr.wordautomata.components;

import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.PlayBackSpeed;
import it.univr.wordautomata.utils.Utils.PlayBackState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

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
    private Button previousStateButton;

    @FXML
    private Button nextStateButton;

    private MainPanel parent;

    public BottomBar(MainPanel parent) {
        Utils.loadAndSetController(Utils.BOTTOM_BAR_FXML_FILENAME, this);
        this.parent = parent;
        styleButtons();
    }

    private void styleButtons() {
        stylePlayPauseButton();
        styleResetButton();
        styleSpeedButton();
        stylePreviousStateButton();
        styleNextStateButton();
    }

    private void stylePlayPauseButton() {
        PlayBackState state = parent.getGraphPanel().getPlayBackState();
        
        playPauseButton.setGraphic(new FontIcon(state == PlayBackState.PAUSED ? BoxiconsRegular.PLAY : BoxiconsRegular.PAUSE));
    }

    private void styleSpeedButton() {
        PlayBackSpeed initialSpeed = parent.getGraphPanel().getSpeed();

        speedLabel.setText(initialSpeed.toString());

        int i = 0;
        for (Node circle : circlesContainer.getChildren()) {
            circle.getStyleClass().remove(Utils.ACTIVE_SPEED_CIRCLE_CLASS);

            if (initialSpeed.ordinal() == i++) {
                circle.getStyleClass().add(Utils.ACTIVE_SPEED_CIRCLE_CLASS);
            }
        }
    }

    private void styleResetButton() {
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
        parent.getGraphPanel().cycleSpeed();
        styleSpeedButton();
    }
    
    @FXML
    private void cyclePlayPause() {
        parent.getGraphPanel().cyclePlayBackState();
        stylePlayPauseButton();
    }
}
