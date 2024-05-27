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
        setCircleClass(consumedEntireWordIndicator, PathFinder.consumedAllWordProperty().get());
        setCircleClass(endedOnFinalStateIndicator, PathFinder.endedOnFinalStateProperty().get());

        PathFinder.consumedAllWordProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(consumedEntireWordIndicator, newValue));
        PathFinder.endedOnFinalStateProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(endedOnFinalStateIndicator, newValue));
    }

    private void setCircleClass(Circle circle, boolean success) {
        String cssClass = Constants.CIRCLE_FAILURE_CLASS;
        if (success)
            cssClass = Constants.CIRCLE_SUCCESS_CLASS;

        circle.getStyleClass().clear();
        circle.getStyleClass().add(cssClass);
    }
}