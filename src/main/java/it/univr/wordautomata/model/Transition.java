package it.univr.wordautomata.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a transition in a word automaton.
 */
public class Transition implements Comparable<Transition> {
    private SimpleStringProperty label;

    /**
     * Constructs a transition with the given label.
     *
     * @param label the label of the transition
     * @throws IllegalArgumentException if the label is null or blank
     */
    public Transition(String label) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = new SimpleStringProperty(label);
    }

    /**
     * Returns the label property of the transition.
     *
     * @return the label property
     */
    public SimpleStringProperty labelProperty() {
        return label;
    }

    /**
     * Returns the label of the transition.
     *
     * @return the label
     */
    public String getLabel() {
        return label.get();
    }

    /**
     * Sets the label of the transition.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label.set(label);
    }

    /**
     * Returns a string representation of the transition.
     *
     * @return a string representation of the transition
     */
    @Override
    public String toString() {
        return label.get();
    }

    /**
     * Compares this transition to another transition based on their labels.
     *
     * @param other the other transition to compare to
     * @return a negative integer, zero, or a positive integer as this transition is less than, equal to, or greater than the other transition
     */
    @Override
    public int compareTo(Transition other) {
        return label.get().compareTo(other.label.get());
    }
}
