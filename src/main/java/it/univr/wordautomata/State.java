package it.univr.wordautomata;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 */
public class State implements Comparable<State> {

    private SimpleStringProperty label;
    private SimpleBooleanProperty isFinal;
    private SimpleBooleanProperty isInitial;

    public State(String label) {
        this(label, false);
    }

    public State(String label, boolean isFinal) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = new SimpleStringProperty(label);
        this.isFinal = new SimpleBooleanProperty(isFinal);
        this.isInitial = new SimpleBooleanProperty(false);
    }

    public SimpleStringProperty getLabel() {
        return label;
    }

    public SimpleBooleanProperty isFinal() {
        return isFinal;
    }

    public void setFinal(boolean value) {
        this.isFinal.set(value);
    }

    public void setInitial(boolean value) {
        this.isInitial.set(value);
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
