package it.univr.wordautomata.controller;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

/**
 * A dialog that allows the user to set the initial state of the automaton.
 */
public class SetInitialStateModal extends ChoiceDialog<State> {

    /**
     * Creates a new {@code SetInitialStateModal}.
     */
    public SetInitialStateModal() {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        getItems().addAll(Model.getInstance().getGraph().objectsInVertices());
        setSelectedItem(Model.getInstance().getInitialState());

        setTitle("Initial state");
        setHeaderText("Set an initial state for the automata");
        
        Scene scene = Components.getInstance().getScene();

        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}
