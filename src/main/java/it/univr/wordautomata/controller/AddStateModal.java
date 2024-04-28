package it.univr.wordautomata.controller;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Utils;
import it.univr.wordautomata.utils.Utils.Theme;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

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
        Theme current = Model.getInstance().getTheme();
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        WindowStyler.setTheme(current, stage);

        if (current == Theme.DARK && Utils.SET_MICA) {
            WindowStyler.setMica(stage, getDialogPane().getScene(), getDialogPane());
        }

    }
}
