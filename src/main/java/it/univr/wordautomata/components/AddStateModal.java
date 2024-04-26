package it.univr.wordautomata.components;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 *
 */
public class AddStateModal extends Dialog<State> {

    public AddStateModal(Scene scene) {
        WindowStyler.setTheme(((MainPanel) scene.getRoot()).getTheme(), (Stage) getDialogPane().getScene().getWindow());
        setTitle("Add state");
        AddStateModalBody body = new AddStateModalBody();

        getDialogPane().setContent(body);
        initOwner(scene.getWindow());
        setResultConverter(c -> {
            if (c == ButtonType.OK) {
                return body.buildState();
            }

            return null;
        });
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());

        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(body.emptyTextfieldProperty());

        body.requestTextFieldFocus();
    }
}
