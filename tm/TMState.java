package tm;

/**
 * Represents a state in a Turing Machine.
 * Each state holds an array of transitions, one for each tape symbol.
 *
 * @author Diego Dominguez
 * @author Hunter McCallister
 */
public class TMState {

    /** The label/id of this state */
    private final int stateId;

    /** Transitions indexed by tape symbol */
    private final Transition[] transitions;

    /**
     * Constructs a new TMState with the given id and number of tape symbols.
     *
     * @param stateId the integer label of this state
     * @param numTapeSymbols the size of the tape alphabet (|Gamma|)
     */
    public TMState(int stateId, int numTapeSymbols) {
        this.stateId = stateId;
        this.transitions = new Transition[numTapeSymbols];
    }

    /**
     * Sets the transition for a given tape symbol.
     *
     * @param symbol the tape symbol that triggers this transition
     * @param transition the transition to take
     */
    public void setTransition(int symbol, Transition transition) {
        transitions[symbol] = transition;
    }

    /**
     * Returns the transition for a given tape symbol.
     *
     * @param symbol the tape symbol under the head
     * @return the transition for that symbol
     */
    public Transition getTransition(int symbol) {
        return transitions[symbol];
    }

    /**
     * Returns the state id.
     *
     * @return the state id
     */
    public int getStateId() {
        return stateId;
    }
}
