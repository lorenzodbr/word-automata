package it.univr.wordautomata.controller;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;

import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.utils.Constants;
import javafx.application.Platform;

/**
 * A dialog that shows the details of a transition.
 */
public class TransitionModal extends ModalBox {

    /**
     * Creates a new transition modal.
     *
     * @param modalPane the modal pane
     * @param edge      the edge to show
     */
    public TransitionModal(ModalPane modalPane, SmartGraphEdge<Transition, State> edge) {
        super(modalPane);

        addContent(new TransitionModalBody(this, edge));
        setMaxWidth(Constants.SIDEBAR_MAX_WIDTH);

        setOnClose((e) -> {
            Components.getInstance().getGraphPanel().update();
            e.consume();
        });

        Platform.runLater(() -> requestFocus());
    }
}
