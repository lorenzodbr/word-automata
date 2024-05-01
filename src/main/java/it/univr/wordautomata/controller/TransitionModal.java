package it.univr.wordautomata.controller;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Utils;
import java.util.function.Consumer;

/**
 *
 */
public class TransitionModal extends ModalBox {

    private TransitionModalBody body;

    public TransitionModal(ModalPane modalPane, SmartGraphEdge<Transition, State> edge) {
        super(modalPane);
        addContent(body = new TransitionModalBody(edge));
        setMaxWidth(Utils.SIDEBAR_MAX_WIDTH);
    }

    public void onTextChange(Consumer<String> event) {
        body.onTextChange(event);
    }

    public void onDeleteButtonClicked(Consumer<Edge<Transition, State>> event) {
        body.onDeleteButtonClicked(event);
    }
}
