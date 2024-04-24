package it.univr.wordautomata.components;

import it.univr.wordautomata.utils.Utils;
import javafx.scene.layout.GridPane;

/**
 * BottomBar wrapper
 */
public class BottomBar extends GridPane {
    public BottomBar(){
        Utils.loadAndSetController(Utils.BOTTOM_BAR_FXML_FILENAME, this);
    }
}
