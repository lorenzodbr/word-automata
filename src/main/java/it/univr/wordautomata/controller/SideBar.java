package it.univr.wordautomata.controller;

import it.univr.wordautomata.utils.Utils;
import javafx.scene.layout.GridPane;

/**
 *
 */
public class SideBar extends GridPane {

    public SideBar() {
        Utils.loadAndSetController(Utils.SIDE_BAR_FXML_FILENAME, this);
    }
}
