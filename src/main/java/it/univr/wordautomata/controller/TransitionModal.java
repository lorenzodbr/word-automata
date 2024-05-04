package it.univr.wordautomata.controller;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import it.univr.wordautomata.State;
import it.univr.wordautomata.Transition;
import it.univr.wordautomata.utils.Constants;

/**
 *
 */
public class TransitionModal extends ModalBox {
    public TransitionModal(ModalPane modalPane, SmartGraphEdge<Transition, State> edge) {
        super(modalPane);
        
        addContent(new TransitionModalBody(this, edge));
        setMaxWidth(Constants.SIDEBAR_MAX_WIDTH);

        setOnClose((e) -> {
            Controllers.getInstance().getGraphPanel().update();
            e.consume();
        });
    }
}
