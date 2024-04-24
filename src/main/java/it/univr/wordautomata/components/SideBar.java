package it.univr.wordautomata.components;

import it.univr.wordautomata.utils.Utils;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Lorenzo
 */
public class SideBar extends GridPane {

    public SideBar() {
        Utils.loadAndSetController(Utils.SIDE_BAR_FXML_FILENAME, this);
    }
}
