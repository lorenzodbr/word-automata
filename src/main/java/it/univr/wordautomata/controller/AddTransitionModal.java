package it.univr.wordautomata.controller;

import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.stylings.WindowStyler;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 *
 */
public class AddTransitionModal extends Dialog<TransitionWrapper> {

    public AddTransitionModal(Scene scene) {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        setTitle("Add transition");
        AddTransitionModalBody body = new AddTransitionModalBody();

        getDialogPane().setContent(body);
        initOwner(scene.getWindow());
        setResultConverter(c -> {
            if (c == ButtonType.OK) {
                return body.buildTransitionWrapper();
            }

            return null;
        });
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(body.emptyTextfieldProperty());

        body.requestTextFieldFocus();
    }
}
