package it.univr.wordautomata.controller;

import atlantafx.base.theme.Styles;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Utils;
import java.util.function.Consumer;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 */
public class TransitionModalBody extends GridPane {

    @FXML
    private TextField transitionLabelTextField;

    @FXML
    private TextField startingState;

    @FXML
    private TextField endingState;

    @FXML
    private Button deleteButton;

    private Consumer<String> onTextChange;
    private Consumer<Edge<Transition, State>> onDeleteButtonClicked;

    private SmartGraphEdge<Transition, State> edge;

    public TransitionModalBody(SmartGraphEdge<Transition, State> edge) {
        Utils.loadAndSetController(Utils.TRANSITION_MODAL_BODY_FXML_FILENAME, this);

        this.edge = edge;
        setFields();
    }

    private void setFields() {

        Edge<Transition, State> underlyingEdge = edge.getUnderlyingEdge();
        Transition transition = underlyingEdge.element();

        startingState.setText(underlyingEdge.vertices()[0].element().toString());
        endingState.setText(underlyingEdge.vertices()[1].element().toString());

        transitionLabelTextField.setText(transition.toString());
        transitionLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);

                transition.setLabel(transitionLabelTextField.getText());

                if (onTextChange != null) {
                    onTextChange.accept(newValue);
                }
            } else {
                transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        deleteButton.setOnAction(e -> {
            if (onDeleteButtonClicked != null) {
                onDeleteButtonClicked.accept(edge.getUnderlyingEdge());
            }
        });
    }

    public void onTextChange(Consumer<String> onTextChange) {
        this.onTextChange = onTextChange;
    }

    public void onDeleteButtonClicked(Consumer<Edge<Transition, State>> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }
}
