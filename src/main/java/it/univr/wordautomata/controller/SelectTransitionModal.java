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
 *
 */
public class SelectTransitionModal extends ChoiceDialog<Transition> {

    public SelectTransitionModal(Scene scene) {        
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        List<Transition> transitions = Model.getInstance().getGraph().objectsInEdges().stream().sorted().toList();
        getItems().addAll(transitions);
        setSelectedItem(transitions.get(0));

        setTitle("Select");
        setHeaderText("Select a transition to view its details");
        
        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}
