package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;

import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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

    private SmartGraphEdge<Transition, State> edge;

    private ModalBox dialog;

    private Controllers controllers;

    public TransitionModalBody(ModalBox dialog, SmartGraphEdge<Transition, State> edge) {
        Methods.loadAndSetController(Constants.TRANSITION_MODAL_BODY_FXML_FILENAME, this);

        this.edge = edge;
        this.dialog = dialog;
        this.controllers = Controllers.getInstance();
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

                controllers.getGraphPanel().update();
            } else {
                transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        deleteButton.setOnAction(e -> {
            if (controllers.getGraphPanel().queryRemoveEdge(underlyingEdge)) {
                dialog.close();
            }

            requestFocus();
        });
    }
}
