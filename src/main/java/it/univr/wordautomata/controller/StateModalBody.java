package it.univr.wordautomata.controller;

import atlantafx.base.theme.Styles;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import it.univr.wordautomata.State;
import it.univr.wordautomata.utils.Utils;
import java.util.function.Consumer;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
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
    
    private Consumer<String> onTextChange;

    public StateModalBody(SmartGraphVertex<State> vertex) {
        Utils.loadAndSetController(Utils.STATE_MODAL_BODY_FXML_FILENAME, this);
        
        setFields(vertex);
    }

    private void setFields(SmartGraphVertex<State> vertex) {
        State state = vertex.getUnderlyingVertex().element();

        stateLabelTextField.setText(state.toString());
        stateLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isBlank()){
                stateLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                
                state.setLabel(stateLabelTextField.getText());
                
                if(onTextChange != null){
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
    }
    
    public void onTextChange(Consumer<String> onTextChange){
        this.onTextChange = onTextChange;
    }
}
