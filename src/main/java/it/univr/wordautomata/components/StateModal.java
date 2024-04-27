package it.univr.wordautomata.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.ModalBox;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import it.univr.wordautomata.State;
import java.util.function.Consumer;

/**
 *
 */
public class StateModal extends ModalBox {
    private StateModalBody body;
    
    public StateModal(ModalPane modalPane, SmartGraphVertex<State> vertex) {
        super(modalPane);
        addContent(body = new StateModalBody(vertex));
        setMaxWidth(250);
    }
    
    public void onTextChange(Consumer event){
        body.onTextChange(event);
    }
}
