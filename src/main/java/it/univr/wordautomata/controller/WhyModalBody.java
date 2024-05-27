package it.univr.wordautomata.controller;

import it.univr.wordautomata.backend.PathFinder;
import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class WhyModalBody extends VBox {

    @FXML
    private Circle consumedEntireWordIndicator;

    @FXML
    private Circle endedOnFinalStateIndicator;

    @FXML
    private Circle atLeastATransitionIndicator;

    @FXML
    private Circle singleInitialStateIndicator;

    @FXML
    private Circle atLeastAFinalStateIndicator;

    /*
     * The body of the dialog that shows the reasons why the automaton is rejecting
     * the word.
     */
    public WhyModalBody() {
        Methods.loadAndSetController(Constants.WHY_MODAL_BODY_FXML_FILENAME, this);
        initCircles();
    }

    /*
     * Initializes the circles that represent the reasons why the automaton is
     * rejecting the word.
     */
    private void initCircles() {
        setCircleClass(consumedEntireWordIndicator, PathFinder.consumedAllWordProperty().get());
        setCircleClass(endedOnFinalStateIndicator, PathFinder.endedOnFinalStateProperty().get());
        setCircleClass(atLeastATransitionIndicator, Model.getInstance().atLeastOneEdgeProperty().get());
        setCircleClass(atLeastAFinalStateIndicator, Model.getInstance().finalStatesCountProperty().get() > 0);

        PathFinder.consumedAllWordProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(consumedEntireWordIndicator, newValue));
        PathFinder.endedOnFinalStateProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(endedOnFinalStateIndicator, newValue));
        Model.getInstance().atLeastOneEdgeProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(atLeastATransitionIndicator, newValue));
        Model.getInstance().initialStateProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(singleInitialStateIndicator, newValue != null));
        Model.getInstance().finalStatesCountProperty().addListener(
                (observable, oldValue, newValue) -> setCircleClass(atLeastAFinalStateIndicator,
                        newValue.intValue() > 0));
    }

    /*
     * Sets the class of the given circle based on the success of the condition it
     * represents.
     */
    private void setCircleClass(Circle circle, boolean success) {
        String cssClass = Constants.CIRCLE_FAILURE_CLASS;
        if (success)
            cssClass = Constants.CIRCLE_SUCCESS_CLASS;

        circle.getStyleClass().clear();
        circle.getStyleClass().add(cssClass);
    }
}