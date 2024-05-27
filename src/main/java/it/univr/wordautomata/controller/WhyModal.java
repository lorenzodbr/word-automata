package it.univr.wordautomata.controller;

import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WhyModal extends Dialog<Void> {

    public WhyModal() {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());
        setTitle("Why?");
        getDialogPane().setContent(new WhyModalBody());
        initOwner(Components.getInstance().getStage());
        getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);
        initModality(Modality.NONE);
    }
}