package it.univr.wordautomata.controller;

import java.util.Optional;

import it.univr.wordautomata.State;
import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 */
public class AddStateModal extends Dialog<State> {

    public AddStateModal(Scene scene) {
        initTheme();
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

    private void initTheme() {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        WindowStyler.setTheme(stage);
    }
}
