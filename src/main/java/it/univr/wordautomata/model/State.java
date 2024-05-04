package it.univr.wordautomata.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Lorenzo
 */
public class State implements Comparable<State> {

    private SimpleStringProperty label;
    private SimpleBooleanProperty isFinal;

    public State(String label) {
        this(label, false);
    }

    public State(String label, boolean isFinal) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = new SimpleStringProperty(label);
        this.isFinal = new SimpleBooleanProperty(isFinal);
    }

    public SimpleStringProperty getLabel() {
        return label;
    }

    public boolean isFinal() {
        return isFinal.get();
    }

    public void setFinal(boolean value) {
        this.isFinal.set(value);
    }

    public SimpleBooleanProperty finalProperty() {
        return isFinal;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    @Override
    public String toString() {
        return label.get();
    }

    @Override
    public int compareTo(State o) {
        return label.get().compareTo(o.label.get());
    }
}
