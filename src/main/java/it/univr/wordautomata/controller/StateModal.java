package it.univr.wordautomata.controller;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import it.univr.wordautomata.model.State;
import it.univr.wordautomata.utils.Constants;

/**
 *
 */
public class StateModal extends ModalBox {

    public StateModal(ModalPane modalPane, SmartGraphVertex<State> vertex) {
        super(modalPane);
        
        addContent(new StateModalBody(this, vertex));
        setMaxWidth(Constants.SIDEBAR_MAX_WIDTH);

        setOnClose((e) -> {
            Components.getInstance().getGraphPanel().update();
            e.consume();
        });
    }
}
