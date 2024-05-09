package it.univr.wordautomata.controller;

import atlantafx.base.theme.Styles;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 */
public class AddStateModalBody extends Pane {

    @FXML
    private TextField stateLabelTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox markAsFinalCheckbox;

    private SimpleBooleanProperty emptyTextfieldProperty;

    public AddStateModalBody() {
        Methods.loadAndSetController(Constants.ADD_STATE_MODAL_BODY_FXML_FILENAME, this);
        emptyTextfieldProperty = new SimpleBooleanProperty(true);

        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean invalid = newValue.isBlank() || Methods.existsState(newValue);

            if (!errorLabel.visibleProperty().isBound()) {
                errorLabel.visibleProperty().bind(emptyTextfieldProperty);
            }

            emptyTextfieldProperty.set(invalid);
            stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, invalid);
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
