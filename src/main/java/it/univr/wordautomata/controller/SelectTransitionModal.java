package it.univr.wordautomata.controller;

import java.util.List;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

/**
 * A dialog that allows the user to select a transition to view its details.
 */
public class SelectTransitionModal extends ChoiceDialog<Transition> {

    /**
     * Creates a new {@code SelectTransitionModal}.
     */
    public SelectTransitionModal() {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        List<Transition> transitions = Model.getInstance().getGraph().objectsInEdges();
        getItems().addAll(transitions);
        setSelectedItem(transitions.get(0));

        setTitle("Select");
        setHeaderText("Select a transition to view its details");

        Scene scene = Components.getInstance().getScene();

        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}
