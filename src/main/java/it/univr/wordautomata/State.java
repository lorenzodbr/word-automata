package it.univr.wordautomata;

/**
 *
 * @author Lorenzo
 */
public class State {

    private String label;
    private boolean isFinal;

    public State(String label) {
        this(label, false);
    }

    public State(String label, boolean isFinal) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label must not be null or blank");
        }

        this.label = label;
        this.isFinal = isFinal;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean isFinal(){
        return isFinal;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
