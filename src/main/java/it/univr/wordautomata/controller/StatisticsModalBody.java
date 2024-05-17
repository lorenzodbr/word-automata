package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.GraphStatistics;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 */
public class StatisticsModalBody extends Pane {

    @FXML
    private TextField possiblePathsTextField;

    @FXML
    private TextField statesSearchedTextField;

    @FXML
    private TextField transitionsSearchedTextField;

    @FXML
    private TextField longestPathTextField;

    @FXML
    private TextField shortestPathTextField;

    public StatisticsModalBody() {
        Methods.loadAndSetController(Constants.STATISTICS_MODAL_BODY_FXML_FILENAME, this);

        loadFields();
    }

    private void loadFields() {
        GraphStatistics statistics = new GraphStatistics();

        Platform.runLater(() -> {
            statistics.getStats();

            possiblePathsTextField.setText(statistics.getPossiblePaths() + "");
            longestPathTextField.setText(statistics.getLongestPath() + "");
            shortestPathTextField.setText(statistics.getShortestPath() + "");
            statesSearchedTextField.setText(statistics.getVerticesSearched() + "");
            transitionsSearchedTextField.setText(statistics.getEdgesSearched() + "");
        });
    }
}
