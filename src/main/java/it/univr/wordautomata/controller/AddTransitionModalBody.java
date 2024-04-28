package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.graph.Vertex;
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
import javafx.util.StringConverter;

/**
 *
 */
public class AddTransitionModalBody extends Pane {

    @FXML
    private TextField transitionLabelTextField;

    @FXML
    private ChoiceBox<State> startingVertexChoiceBox;

    @FXML
    private ChoiceBox<State> endingVertexChoiceBox;
    
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
                    startingVertexChoiceBox.getSelectionModel().getSelectedItem(),
                    endingVertexChoiceBox.getSelectionModel().getSelectedItem(),
                    new Transition(label));
        }

        return null;
    }

    private void loadChoiceBoxes(Collection<State> vertices) {
        startingVertexChoiceBox.setItems(FXCollections.observableArrayList(vertices));
        endingVertexChoiceBox.setItems(FXCollections.observableArrayList(vertices));

        startingVertexChoiceBox.getSelectionModel().selectFirst();

        if (vertices.size() > 1) {
            endingVertexChoiceBox.getSelectionModel().select(1);
        } else {
            endingVertexChoiceBox.getSelectionModel().selectFirst();
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
