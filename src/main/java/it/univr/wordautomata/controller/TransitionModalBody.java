package it.univr.wordautomata.controller;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;

import atlantafx.base.controls.Popover;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The body of the dialog that shows the details of a transition.
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

    private Components components;

    private Popover popover;

    /**
     * Creates a new transition modal body.
     *
     * @param dialog the dialog
     * @param edge   the edge to show
     */
    public TransitionModalBody(ModalBox dialog, SmartGraphEdge<Transition, State> edge) {
        Methods.loadAndSetController(Constants.TRANSITION_MODAL_BODY_FXML_FILENAME, this);

        this.edge = edge;
        this.dialog = dialog;
        this.components = Components.getInstance();
        setFields();

        prefHeightProperty().bind(Components.getInstance().getGraphPanel().heightProperty());
    }

    /**
     * Sets the fields of the dialog.
     */
    private void setFields() {
        TextFlow body = new TextFlow(
                new Text("Label must be unique (relative to the starting and ending states) and not blank."));
        body.setTextAlignment(javafx.scene.text.TextAlignment.JUSTIFY);
        body.setMaxWidth(200);
        this.popover = new Popover(body);

        Edge<Transition, State> underlyingEdge = edge.getUnderlyingEdge();
        Transition transition = underlyingEdge.element();

        startingState.setText(underlyingEdge.vertices()[0].element().toString());
        endingState.setText(underlyingEdge.vertices()[1].element().toString());

        transitionLabelTextField.setText(transition.toString());
        transitionLabelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean invalid = newValue == null || newValue.isBlank()
                    || Methods.existsTransitionFromVertex(startingState.getText(), newValue)
                    || Methods.existsTransitionToVertex(endingState.getText(), newValue);

            if (!invalid) {
                popover.hide();
                transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                transition.setLabel(transitionLabelTextField.getText());
                components.getGraphPanel().update();
            } else {
                transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
                popover.show(transitionLabelTextField);
            }
        });

        deleteButton.setOnAction(e -> {
            if (components.getGraphPanel().queryRemoveEdge(underlyingEdge)) {
                dialog.close();
            }

            requestFocus();
        });
    }
}
