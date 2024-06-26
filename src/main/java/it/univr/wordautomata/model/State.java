package it.univr.wordautomata.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import it.univr.wordautomata.WordAutomata;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a state in a {@link WordAutomata}.
 * It contains a label that identifies the state and a boolean property that
 * indicates if the state is final (accepting).
 */
public class State implements Comparable<State>, Serializable {

    private static final long serialVersionUID = 6863920576414706018L;

    private transient SimpleStringProperty label;
    private transient SimpleBooleanProperty isFinal;

    /**
     * Constructs a state with the given label and default final status (false).
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
     * @param label   the label of the state
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
        SimpleIntegerProperty p = Model.getInstance().finalStatesCountProperty();

        int inc = value ? 1 : -1;
        p.set(p.get() + inc);

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
     * @return a negative integer, zero, or a positive integer as this state is less
     *         than, equal to, or greater than the other state
     */
    @Override
    public int compareTo(State other) {
        return label.get().compareTo(other.label.get());
    }

    /**
     * Check if this state is equal to another object.
     *
     * @return true if the object is a state and has the same label, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof State) && compareTo((State) o) == 0;
    }
    
    /**
     * Writes the state to an object output stream
     * 
     * @param out the object output stream to write to
     */
    private void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(label.get());
            out.writeObject(isFinal.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the state from an object input stream
     * 
     * @param in the object input stream to read from
     */
    private void readObject(ObjectInputStream in) {
        try {
            label = new SimpleStringProperty(new String((String) in.readObject()));
            isFinal = new SimpleBooleanProperty((boolean) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
