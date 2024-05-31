package it.univr.wordautomata.controller;

import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;

import javafx.scene.layout.GridPane;

/**
 * The body of the dialog that explains the states' colors.
 */
public class LegendModalBody extends GridPane {

    /**
     * Creates a new {@code LegendModalBody}.
     */
    public LegendModalBody() {
        Methods.loadAndSetController(Constants.LEGEND_MODAL_BODY_FXML_FILENAME, this);
    }
}
