package it.univr.wordautomata.components;

import com.brunomnsilva.smartgraph.graph.Vertex;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.utils.Utils;
import java.util.Collection;
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

    public AddTransitionModalBody(Collection<State> vertices) {
        Utils.loadAndSetController(Utils.ADD_TRANSITION_MODAL_BODY_FXML_FILENAME, this);

        loadChoiceBoxes(vertices);
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
        endingVertexChoiceBox.getSelectionModel().selectFirst();
    }
}
