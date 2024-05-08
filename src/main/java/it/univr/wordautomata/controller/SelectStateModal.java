package it.univr.wordautomata.controller;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

/**
 *
 */
public class SelectStateModal extends ChoiceDialog<State> {

    public SelectStateModal(Scene scene) {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());

        setTitle("Select");
        setHeaderText("Select a state to view its details");

        getItems().addAll(Model.getInstance().getGraph().objectsInVertices());
        setSelectedItem(Model.getInstance().getInitialState());

        initOwner(scene.getWindow());
        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(selectedItemProperty().isNull());
    }
}
