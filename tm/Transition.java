package tm;

/**
 * Represents a single transition in a Turing Machine.
 * Each transition specifies the next state, symbol to write,
 * and direction to move the tape head.
 *
 * @author Diego Dominguez
 * @author Hunter McCallister
 */
public class Transition {

    /** The state to transition to */
    private final int nextState;

    /** The symbol to write on the tape */
    private final int writeSymbol;

    /** The direction to move: 'L' for left, 'R' for right */
    private final char moveDirection;

    /**
     * Constructs a new Transition.
     *
     * @param nextState the state to transition to
     * @param writeSymbol the symbol to write on the tape
     * @param moveDirection the direction to move the tape head ('L' or 'R')
     */
    public Transition(int nextState, int writeSymbol, char moveDirection) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.moveDirection = moveDirection;
    }

    /**
     * Returns the next state for this transition.
     *
     * @return the next state
     */
    public int getNextState() {
        return nextState;
    }

    /**
     * Returns the symbol to write for this transition.
     *
     * @return the write symbol
     */
    public int getWriteSymbol() {
        return writeSymbol;
    }

    /**
     * Returns the move direction for this transition.
     *
     * @return 'L' for left or 'R' for right
     */
    public char getMoveDirection() {
        return moveDirection;
    }
}
