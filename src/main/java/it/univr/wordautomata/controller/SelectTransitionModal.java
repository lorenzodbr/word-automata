package it.univr.wordautomata.controller;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.State;
import it.univr.wordautomata.model.Model;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import it.univr.wordautomata.Transition;

/**
 *
 */
public class SelectTransitionModal extends ChoiceDialog<Transition> {

    public SelectTransitionModal(Scene scene) {
        super((Transition) Model.getInstance().getGraph().objectsInEdges().iterator().next(), Model.getInstance().getGraph().objectsInEdges());
        
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        setTitle("Select");
        setHeaderText(null);
        setContentText("Select a transition to view details");
        
        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}