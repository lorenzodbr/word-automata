package it.univr.wordautomata.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 */
public class Transition implements Comparable<Transition> {
    private SimpleStringProperty label;

    public Transition(String label) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = new SimpleStringProperty(label);
    }

    public SimpleStringProperty labelProperty() {
        return label;
    }

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    @Override
    public String toString() {
        return label.get();
    }

    @Override
    public int compareTo(Transition o) {
        return label.get().compareTo(o.label.get());
    }
}
