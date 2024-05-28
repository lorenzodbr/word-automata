package it.univr.wordautomata.controller;

import it.univr.wordautomata.stylings.WindowStyler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A modal dialog that displays the reasons why the automaton is rejecting the
 * word.
 */
public class WhyModal extends Dialog<Void> {

    /**
     * Creates a new instance of the {@code WhyModal} class.
     * The modal dialog is initialized with a title, content, and button types.
     */
    public WhyModal() {
        WindowStyler.setTheme((Stage) getDialogPane().getScene().getWindow());
        setTitle("Why?");
        getDialogPane().setContent(new WhyModalBody());
        initOwner(Components.getInstance().getStage());
        getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);
        initModality(Modality.NONE);
    }
}