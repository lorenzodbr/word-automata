package it.univr.wordautomata.controller;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.model.Model;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

/**
 *
 */
public class SetInitialStateModal extends ChoiceDialog<State> {

    public SetInitialStateModal(Scene scene) {
        super(Model.getInstance().getInitialState(), Model.getInstance().getGraph().objectsInVertices());
        
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        setTitle("Initial state");
        setHeaderText(null);
        setContentText("Select an initial state for the automata");
        
        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}
