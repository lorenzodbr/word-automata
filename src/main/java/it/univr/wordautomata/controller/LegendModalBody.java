package it.univr.wordautomata.controller;

import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;

import javafx.scene.layout.GridPane;

/**
 * The body of the dialog that shows the legend of the automaton.
 */
public class LegendModalBody extends GridPane {

    public LegendModalBody() {
        Methods.loadAndSetController(Constants.LEGEND_MODAL_BODY_FXML_FILENAME, this);
    }
}
