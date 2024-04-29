package it.univr.wordautomata.controller;

import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.utils.Utils;
import java.util.Collection;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 */
public class AddTransitionModalBody extends Pane {

    @FXML
    private TextField transitionLabelTextField;

    @FXML
    private ChoiceBox<State> startingStateChoiceBox;

    @FXML
    private ChoiceBox<State> endingStateChoiceBox;
    
    private SimpleBooleanProperty emptyTextfieldProperty;

    public AddTransitionModalBody(Collection<State> vertices) {
        Utils.loadAndSetController(Utils.ADD_TRANSITION_MODAL_BODY_FXML_FILENAME, this);
        loadChoiceBoxes(vertices);
        
        emptyTextfieldProperty = new SimpleBooleanProperty(true);
        transitionLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emptyTextfieldProperty.set(newValue.isBlank());
        });
    }

    public TransitionWrapper buildTransitionWrapper() {
        String label = transitionLabelTextField.getText();

        if (label != null && !label.isBlank()) {
            return new TransitionWrapper(
                    startingStateChoiceBox.getSelectionModel().getSelectedItem(),
                    endingStateChoiceBox.getSelectionModel().getSelectedItem(),
                    new Transition(label));
        }

        return null;
    }

    private void loadChoiceBoxes(Collection<State> vertices) {
        startingStateChoiceBox.setItems(FXCollections.observableArrayList(vertices));
        endingStateChoiceBox.setItems(FXCollections.observableArrayList(vertices));

        startingStateChoiceBox.getSelectionModel().selectFirst();

        if (vertices.size() > 1) {
            endingStateChoiceBox.getSelectionModel().select(1);
        } else {
            endingStateChoiceBox.getSelectionModel().selectFirst();
        }
    }
    
    public void requestTextFieldFocus() {
        Platform.runLater(() -> {
            transitionLabelTextField.requestFocus();
        });
    }
    
    public ObservableValue<Boolean> emptyTextfieldProperty(){
        return emptyTextfieldProperty;
    }
}
