package tm;


/**
 * Represents a Turing Machine with a bi-infinite tape.
 * Uses a single contiguous array with an offset to represent the bi-infinite
 * tape, eliminating branching in the hot simulation loop.
 * The transition table is flattened into parallel arrays for fast lookup.
 *
 * @author Diego Dominguez
 * @author Hunter McCallister
 */
public class TM {

    /** Number of tape symbols (|Gamma| = numInputSymbols + 1) */
    private final int numTapeSymbols;

    /** The halting state id (numStates - 1) */
    private final int haltState;

    // Flattened transition table arrays indexed by (state * numTapeSymbols + symbol)
    /** Next state for each transition */
    private final int[] transNextState;

    /** Symbol to write for each transition */
    private final int[] transWriteSymbol;

    /** Move direction for each transition: +1 for R, -1 for L */
    private final int[] transMoveDir;

    /** Single contiguous tape array; tape position p maps to tape[p + offset] */
    private int[] tape;

    /** Offset so that tape position 0 maps to tape[offset] */
    private int offset;

    /** Current allocated size of the tape array */
    private int tapeSize;

    /** Leftmost visited tape position */
    private int minPos;

    /** Rightmost visited tape position */
    private int maxPos;

    /** The states of this TM, used for OO representation */
    private final TMState[] states;

    /**
     * Constructs a Turing Machine.
     *
     * @param numStates total number of states
     * @param numInputSymbols number of input symbols in Sigma
     * @param states array of TMState objects with transitions set
     */
    public TM(int numStates, int numInputSymbols, TMState[] states) {
        this.numTapeSymbols = numInputSymbols + 1;
        this.haltState = numStates - 1;
        this.states = states;

        // Flatten transition table into parallel arrays for fast simulation
        int tableSize = (numStates - 1) * numTapeSymbols;
        transNextState = new int[tableSize];
        transWriteSymbol = new int[tableSize];
        transMoveDir = new int[tableSize];

        for (int s = 0; s < numStates - 1; s++) {
            for (int a = 0; a < numTapeSymbols; a++) {
                int idx = s * numTapeSymbols + a;
                Transition t = states[s].getTransition(a);
                transNextState[idx] = t.getNextState();
                transWriteSymbol[idx] = t.getWriteSymbol();
                transMoveDir[idx] = (t.getMoveDirection() == 'R') ? 1 : -1;
            }
        }

        // Initialize tape as a single array with offset at the center
        tapeSize = 65536;
        tape = new int[tapeSize];
        offset = tapeSize / 2;

        minPos = 0;
        maxPos = 0;
    }

    /**
     * Grows the tape array when the head moves out of bounds.
     * Doubles the size and re-centers the data.
     *
     * @param requiredPos the tape position that triggered the resize
     */
    private void growTape(int requiredPos) {
        int newSize = tapeSize * 2;
        // Keep doubling until the required position fits
        while (requiredPos + newSize / 2 < 0 || requiredPos + newSize / 2 >= newSize) {
            newSize *= 2;
        }
        int newOffset = newSize / 2;
        int[] newTape = new int[newSize];
        // Copy existing visited region
        int srcStart = minPos + offset;
        int dstStart = minPos + newOffset;
        int length = maxPos - minPos + 1;
        System.arraycopy(tape, srcStart, newTape, dstStart, length);
        tape = newTape;
        offset = newOffset;
        tapeSize = newSize;
    }

    /**
     * Loads the input string onto the tape starting at position 0.
     * Each character is converted to its integer symbol value.
     *
     * @param input the input string (may be empty for epsilon)
     */
    public void loadInput(String input) {
        if (input != null && !input.isEmpty()) {
            // Grow if input doesn't fit
            if (input.length() + offset >= tapeSize) {
                growTape(input.length());
            }
            for (int i = 0; i < input.length(); i++) {
                tape[i + offset] = input.charAt(i) - '0';
            }
            maxPos = input.length() - 1;
        }
    }

    /**
     * Runs the Turing Machine simulation until the halting state is reached.
     * Uses local variables and a single contiguous tape array to minimize
     * overhead per step.
     *
     * @return the content of visited tape cells as a String
     */
    public String run() {
        // Pull fields into locals for the hot loop
        int state = 0;
        int head = offset; // head index directly into tape array
        int min = minPos + offset;
        int max = maxPos + offset;
        int[] t = tape;
        int tLen = tapeSize;
        final int[] ns = transNextState;
        final int[] ws = transWriteSymbol;
        final int[] md = transMoveDir;
        final int nts = numTapeSymbols;
        final int halt = haltState;

        while (state != halt) {
            int idx = state * nts + t[head];

            // Apply transition
            state = ns[idx];
            t[head] = ws[idx];
            head += md[idx];

            // Track visited range
            if (head < min) {
                min = head;
            } else if (head > max) {
                max = head;
            }

            // Grow tape if head goes out of bounds (rare)
            if (head < 0 || head >= tLen) {
                // Write locals back before growing
                minPos = min - offset;
                maxPos = max - offset;
                growTape(head - offset);
                // Re-read after grow
                head = (head - offset) + this.offset;
                min = minPos + this.offset;
                max = maxPos + this.offset;
                t = tape;
                tLen = tapeSize;
            }
        }

        // Build output from visited tape cells
        StringBuilder sb = new StringBuilder(max - min + 1);
        for (int i = min; i <= max; i++) {
            sb.append(t[i]);
        }
        return sb.toString();
    }
}
