package it.univr.wordautomata.controller;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.TransitionWrapper;
import it.univr.wordautomata.model.Model;
import java.util.Collection;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 *
 */
public class AddTransitionModal extends Dialog<TransitionWrapper> {

    public AddTransitionModal(Scene scene, Collection<State> vertices) {
        WindowStyler.setTheme(Model.getInstance().getTheme(), (Stage) getDialogPane().getScene().getWindow());

        setTitle("Add transition");
        AddTransitionModalBody body = new AddTransitionModalBody(vertices);

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
