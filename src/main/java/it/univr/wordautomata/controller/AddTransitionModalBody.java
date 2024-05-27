package it.univr.wordautomata.controller;

import it.univr.wordautomata.model.Model;
import it.univr.wordautomata.model.State;
import it.univr.wordautomata.model.Transition;
import it.univr.wordautomata.model.TransitionWrapper;
import it.univr.wordautomata.utils.Constants;
import it.univr.wordautomata.utils.Methods;

import java.util.Collection;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * The body of the dialog that allows the user to add a new transition to the
 * automaton.
 */
public class AddTransitionModalBody extends Pane {

    @FXML
    private TextField transitionLabelTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<State> startingStateChoiceBox;

    @FXML
    private ChoiceBox<State> endingStateChoiceBox;

    private SimpleBooleanProperty emptyTextfieldProperty;

    /**
     * Displays the body of the dialog that allows the user to add a new transition
     *
     * @param initialState the initial state of the transition
     * @param endingState  the ending state of the transition
     */
    public AddTransitionModalBody(State initialState, State endingState) {
        Methods.loadAndSetController(Constants.ADD_TRANSITION_MODAL_BODY_FXML_FILENAME, this);
        loadChoiceBoxes(initialState, endingState);

        emptyTextfieldProperty = new SimpleBooleanProperty(true);

        transitionLabelTextField.textProperty()
                .addListener((observable, oldValue, newValue) -> validateAndUpdateTextField(newValue));
    }

    /**
     * Validates the text field and updates the error label accordingly.
     *
     * @param newValue the new value of the text field
     */
    public void validateAndUpdateTextField(String newValue) {
        String from = startingStateChoiceBox.getSelectionModel().getSelectedItem().getLabel().get();
        String to = endingStateChoiceBox.getSelectionModel().getSelectedItem().getLabel().get();

        boolean invalid = newValue.isBlank()
                || Methods.existsTransitionFromVertex(from, newValue)
                || Methods.existsTransitionToVertex(to, newValue);

        if (!errorLabel.visibleProperty().isBound()) {
            errorLabel.visibleProperty().bind(emptyTextfieldProperty);
        }

        emptyTextfieldProperty.set(invalid);
        transitionLabelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, invalid);
    }

    /**
     * Builds a new transition from the data entered by the user.
     *
     * @return the new transition
     */
    public TransitionWrapper buildTransitionWrapper() {
        String label = transitionLabelTextField.getText();

        if (label != null && !label.isBlank()) {
            return new TransitionWrapper(
                    startingStateChoiceBox.getSelectionModel().getSelectedItem(),
                    endingStateChoiceBox.getSelectionModel().getSelectedItem(),
                    new Transition(label));
        }

        return null;
    }

    /**
     * Loads the choice boxes with the states of the automaton.
     *
     * @param initialState the initial state of the transition
     * @param endingState  the ending state of the transition
     */
    private void loadChoiceBoxes(State initialState, State endingState) {
        Collection<State> vertices = Model.getInstance().getGraph().objectsInVertices();

        if (initialState == null) {
            startingStateChoiceBox.setItems(FXCollections.observableArrayList(vertices));
        } else {
            startingStateChoiceBox.setItems(FXCollections.observableArrayList(initialState));
            startingStateChoiceBox.setDisable(true);
        }

        if (endingState == null) {
            endingStateChoiceBox.setItems(FXCollections.observableArrayList(vertices));
        } else {
            endingStateChoiceBox.setItems(FXCollections.observableArrayList(endingState));
            endingStateChoiceBox.setDisable(true);
        }

        startingStateChoiceBox.getSelectionModel().selectFirst();
        if (endingState == null && vertices.size() > 1) {
            endingStateChoiceBox.getSelectionModel().select(1);
        } else {
            endingStateChoiceBox.getSelectionModel().selectFirst();
        }

        startingStateChoiceBox.setOnAction(e -> validateAndUpdateTextField(transitionLabelTextField.getText()));
        endingStateChoiceBox.setOnAction(e -> validateAndUpdateTextField(transitionLabelTextField.getText()));
    }

    /**
     * Requests the focus on the text field.
     */
    public void requestTextFieldFocus() {
        Platform.runLater(() -> {
            transitionLabelTextField.requestFocus();
        });
    }

    /**
     * Returns the property that indicates whether the text field is empty.
     *
     * @return the property that indicates whether the text field is empty
     */
    public ObservableValue<Boolean> emptyTextfieldProperty() {
        return emptyTextfieldProperty;
    }
}
