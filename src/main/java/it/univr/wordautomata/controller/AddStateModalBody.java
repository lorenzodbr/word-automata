package it.univr.wordautomata.controller;

import it.univr.wordautomata.State;
import it.univr.wordautomata.utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 */
public class AddStateModalBody extends Pane {

    @FXML
    private TextField stateLabelTextField;

    @FXML
    private CheckBox markAsFinalCheckbox;

    private SimpleBooleanProperty emptyTextfieldProperty;

    public AddStateModalBody() {
        Utils.loadAndSetController(Utils.ADD_STATE_MODAL_BODY_FXML_FILENAME, this);
        emptyTextfieldProperty = new SimpleBooleanProperty(true);
        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emptyTextfieldProperty.set(newValue.isBlank());
        });
    }

    public State buildState() {
        String label = stateLabelTextField.getText();

        if (label != null && !label.isBlank()) {
            return new State(label, markAsFinalCheckbox.isSelected());
        }

        return null;
    }

    public void requestTextFieldFocus() {
        Platform.runLater(() -> {
            stateLabelTextField.requestFocus();
        });
    }

    public ObservableValue<Boolean> emptyTextfieldProperty() {
        return emptyTextfieldProperty;
    }
}
