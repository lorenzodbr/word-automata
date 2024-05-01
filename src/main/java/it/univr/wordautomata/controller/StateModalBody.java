package it.univr.wordautomata.controller;

import atlantafx.base.theme.Styles;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import it.univr.wordautomata.State;
import it.univr.wordautomata.utils.Utils;
import java.util.function.Consumer;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 *
 */
public class StateModalBody extends GridPane {

    @FXML
    private TextField stateLabelTextField;

    @FXML
    private CheckBox markAsFinalCheckbox;
    
    @FXML
    private Button deleteButton;

    private Consumer<String> onTextChange;
    private Consumer<Vertex<State>> onDeleteButtonClicked;
    
    private SmartGraphVertex<State> vertex;

    public StateModalBody(SmartGraphVertex<State> vertex) {
        Utils.loadAndSetController(Utils.STATE_MODAL_BODY_FXML_FILENAME, this);
        
        this.vertex = vertex;
       
        setFields();
    }

    private void setFields() {
        State state = vertex.getUnderlyingVertex().element();

        stateLabelTextField.setText(state.toString());
        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);

                state.setLabel(stateLabelTextField.getText());

                if (onTextChange != null) {
                    onTextChange.accept(newValue);
                }
            } else {
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        markAsFinalCheckbox.setSelected(state.isFinal().get());
        state.isFinal().bind(markAsFinalCheckbox.selectedProperty());
        state.isFinal().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertex.addStyleClass("final-state");
            } else {
                vertex.removeStyleClass("final-state");
            }
        });
        
        deleteButton.setOnAction(e -> {
            if(onDeleteButtonClicked != null){
                onDeleteButtonClicked.accept(vertex.getUnderlyingVertex());
            }
        });
    }

    public void onTextChange(Consumer<String> onTextChange) {
        this.onTextChange = onTextChange;
    }

    public void onDeleteButtonClicked(Consumer<Vertex<State>> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }
}
