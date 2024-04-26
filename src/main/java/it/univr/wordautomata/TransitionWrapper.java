package it.univr.wordautomata;

/**
 *
 * @author Lorenzo
 */
public class TransitionWrapper {

    private State startingState;
    private State endingState;
    private Transition transition;

    public TransitionWrapper(State startingState, State endingState, Transition transition) {
        this.startingState = startingState;
        this.endingState = endingState;
        this.transition = transition;
    }

    public State getStartingState() {
        return startingState;
    }

    public State getEndingState() {
        return endingState;
    }

    public Transition getTransition() {
        return transition;
    }
}
