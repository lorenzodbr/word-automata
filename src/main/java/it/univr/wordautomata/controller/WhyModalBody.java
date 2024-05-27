package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.PathFinder;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class WhyModalBody extends VBox {

    @FXML
    private Circle consumedEntireWordIndicator;

    @FXML
    private Circle endedOnFinalStateIndicator;

    public WhyModalBody() {
        Methods.loadAndSetController(Constants.WHY_MODAL_BODY_FXML_FILENAME, this);
        initCircles();
    }

    private void initCircles() {
        consumedEntireWordIndicator.fillProperty().bind(Bindings.createObjectBinding(() -> {
            if (PathFinder.consumedAllWordProperty().get())
                return Constants.CIRCLE_SUCCESS_COLOR;
            return Constants.CIRCLE_FAILURE_COLOR;
        }, PathFinder.consumedAllWordProperty()));
        endedOnFinalStateIndicator.fillProperty().bind(Bindings.createObjectBinding(() -> {
            if (PathFinder.endedOnFinalStateProperty().get())
                return Constants.CIRCLE_SUCCESS_COLOR;
            return Constants.CIRCLE_FAILURE_COLOR;
        }, PathFinder.endedOnFinalStateProperty()));
    }
}