package it.univr.wordautomata.model;

/**
 * Represents a wrapper class for a transition in a word automaton, in order to store the starting and ending states of the transition.
 */
public class TransitionWrapper {

    private State startingState;
    private State endingState;
    private Transition transition;

    /**
     * Constructs a new TransitionWrapper object.
     *
     * @param startingState the starting state of the transition
     * @param endingState the ending state of the transition
     * @param transition the transition itself
     */
    public TransitionWrapper(State startingState, State endingState, Transition transition) {
        this.startingState = startingState;
        this.endingState = endingState;
        this.transition = transition;
    }

    /**
     * Returns the starting state of the transition.
     *
     * @return the starting state
     */
    public State getStartingState() {
        return startingState;
    }

    /**
     * Returns the ending state of the transition.
     *
     * @return the ending state
     */
    public State getEndingState() {
        return endingState;
    }

    /**
     * Returns the transition itself.
     *
     * @return the transition
     */
    public Transition getTransition() {
        return transition;
    }
}
