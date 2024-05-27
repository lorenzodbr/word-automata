package it.univr.wordautomata.controller;

import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.TransitionWrapper;
import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * A dialog that allows the user to add a new transition to the automaton.
 */
public class AddTransitionModal extends Dialog<TransitionWrapper> {

    /**
     * Creates a new transition.
     *
     * @param scene        the scene to which the modal will be attached
     * @param initialState the initial state of the transition
     * @param endingState  the ending state of the transition
     */

    public AddTransitionModal(Scene scene, State initialState, State endingState) {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        setTitle("Add transition");
        AddTransitionModalBody body = new AddTransitionModalBody(initialState, endingState);

        getDialogPane().setContent(body);
        initOwner(scene.getWindow());
        setResultConverter(c -> {
            if (c == ButtonType.OK) {
                return body.buildTransitionWrapper();
            }

            return null;
        });

        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(body.emptyTextfieldProperty());

        body.requestTextFieldFocus();
    }
}
