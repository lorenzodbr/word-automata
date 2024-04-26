package it.univr.wordautomata.components;

import it.univr.wordautomata.State;
import it.univr.wordautomata.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 */
public class AddStateModalBody extends Pane {

    @FXML
    private TextField stateLabelTextField;
    
    @FXML
    private CheckBox markAsFinalCheckbox;

    public AddStateModalBody() {
        Utils.loadAndSetController(Utils.ADD_STATE_MODAL_BODY_FXML_FILENAME, this);
    }

    public State buildState() {
        String label = stateLabelTextField.getText();

        if (label != null && !label.isBlank()) {
            return new State(label, markAsFinalCheckbox.isSelected());
        }

        return null;
    }
}
