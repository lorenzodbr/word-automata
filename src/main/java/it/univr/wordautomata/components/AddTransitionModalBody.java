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
    private ChoiceBox<Vertex<State>> startingVertexChoiceBox;

    @FXML
    private ChoiceBox<Vertex<State>> endingVertexChoiceBox;

    public AddTransitionModalBody(Collection<Vertex<State>> vertices) {
        Utils.loadAndSetController(Utils.ADD_TRANSITION_MODAL_BODY_FXML_FILENAME, this);

        loadChoiceBoxes(vertices);
    }

    public TransitionWrapper buildTransitionWrapper() {
        String label = transitionLabelTextField.getText();

        if (label != null && !label.isBlank()) {
            return new TransitionWrapper(
                    startingVertexChoiceBox.getSelectionModel().getSelectedItem().element(), 
                    endingVertexChoiceBox.getSelectionModel().getSelectedItem().element(), 
                    new Transition(label));
        }
        
        return null;
    }

    private void loadChoiceBoxes(Collection<Vertex<State>> vertices) {
        startingVertexChoiceBox.setItems(FXCollections.observableArrayList(vertices));
        endingVertexChoiceBox.setItems(FXCollections.observableArrayList(vertices));

        startingVertexChoiceBox.getSelectionModel().selectFirst();
        endingVertexChoiceBox.getSelectionModel().selectFirst();
        
        startingVertexChoiceBox.setConverter(new StringConverter<Vertex<State>>() {
            @Override
            public String toString(Vertex<State> t) {
                return t.element().toString();
            }

            @Override
            public Vertex<State> fromString(String string) {
                return null;
            }
        });
        
        endingVertexChoiceBox.setConverter(new StringConverter<Vertex<State>>() {
            @Override
            public String toString(Vertex<State> t) {
                return t.element().toString();
            }

            @Override
            public Vertex<State> fromString(String string) {
                return null;
            }
        });
    }
}
