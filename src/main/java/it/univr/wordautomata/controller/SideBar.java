package it.univr.wordautomata.controller;

import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.scene.layout.GridPane;

/**
 *
 */
public class SideBar extends GridPane {

    public SideBar() {
        Methods.loadAndSetController(Constants.SIDE_BAR_FXML_FILENAME, this);
    }
}
