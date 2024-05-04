package it.univr.wordautomata.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a state in a word automaton.
 */
public class State implements Comparable<State> {

    private SimpleStringProperty label;
    private SimpleBooleanProperty isFinal;

    /**
     * Constructs a state with the given label.
     *
     * @param label the label of the state
     * @throws IllegalArgumentException if the label is null or blank
     */
    public State(String label) {
        this(label, false);
    }

    /**
     * Constructs a state with the given label and final status.
     *
     * @param label the label of the state
     * @param isFinal true if the state is final, false otherwise
     * @throws IllegalArgumentException if the label is null or blank
     */
    public State(String label, boolean isFinal) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = new SimpleStringProperty(label);
        this.isFinal = new SimpleBooleanProperty(isFinal);
    }

    /**
     * Returns the label of the state.
     *
     * @return the label of the state
     */
    public SimpleStringProperty getLabel() {
        return label;
    }

    /**
     * Returns true if the state is final, false otherwise.
     *
     * @return true if the state is final, false otherwise
     */
    public boolean isFinal() {
        return isFinal.get();
    }

    /**
     * Sets the final status of the state.
     *
     * @param value true if the state is final, false otherwise
     */
    public void setFinal(boolean value) {
        this.isFinal.set(value);
    }

    /**
     * Returns the final property of the state.
     *
     * @return the final property of the state
     */
    public SimpleBooleanProperty finalProperty() {
        return isFinal;
    }

    /**
     * Sets the label of the state.
     *
     * @param label the label of the state
     */
    public void setLabel(String label) {
        this.label.set(label);
    }

    /**
     * Returns the string representation of the state (its label).
     *
     * @return the string representation of the state
     */
    @Override
    public String toString() {
        return label.get();
    }

    /**
     * Compares this state to another state based on their labels.
     *
     * @param other the other state to compare to
     * @return a negative integer, zero, or a positive integer as this state is less than, equal to, or greater than the other state
     */
    @Override
    public int compareTo(State other) {
        return label.get().compareTo(other.label.get());
    }
}
