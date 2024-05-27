package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.GraphStatistics;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * The body of the dialog that shows the statistics of the automaton.
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

    /**
     * Creates a new statistics modal body.
     */
    public StatisticsModalBody() {
        Methods.loadAndSetController(Constants.STATISTICS_MODAL_BODY_FXML_FILENAME, this);

        loadFields();
    }

    /**
     * Loads the fields with the statistics of the automaton.
     */
    private void loadFields() {
        GraphStatistics statistics = new GraphStatistics();

        Platform.runLater(() -> {
            statistics.computeStats();

            possiblePathsTextField.setText(statistics.getPossiblePaths() + "");
            longestPathTextField.setText(statistics.getLongestPath() + "");
            shortestPathTextField.setText(statistics.getShortestPath() + "");
            statesSearchedTextField.setText(statistics.getVerticesSearched() + "");
            transitionsSearchedTextField.setText(statistics.getEdgesSearched() + "");
        });
    }
}
