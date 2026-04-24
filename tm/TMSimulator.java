package tm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main class for the Turing Machine simulator.
 * Parses a TM encoding file and simulates the machine on the given input.
 * Outputs the content of visited tape cells upon halting.
 *
 * <p>Usage: java tm.TMSimulator &lt;inputFile&gt;</p>
 *
 * @author Diego Dominguez
 * @author Hunter McCallister
 */
public class TMSimulator {

    /**
     * Parses the input file and constructs a TM, then runs the simulation.
     *
     * @param args command line arguments; args[0] is the input file path
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java tm.TMSimulator <inputFile>");
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            // Parse number of states
            int numStates = Integer.parseInt(br.readLine().trim());

            // Parse number of input symbols
            int numInputSymbols = Integer.parseInt(br.readLine().trim());

            // Total tape symbols: blank (0) + input symbols
            int numTapeSymbols = numInputSymbols + 1;

            // Create state objects
            TMState[] states = new TMState[numStates];
            for (int i = 0; i < numStates; i++) {
                states[i] = new TMState(i, numTapeSymbols);
            }

            // Parse transitions for non-halting states
            // Transitions are ordered: for each state 0..(numStates-2),
            // for each symbol 0..(numTapeSymbols-1)
            for (int s = 0; s < numStates - 1; s++) {
                for (int a = 0; a < numTapeSymbols; a++) {
                    String line = br.readLine().trim();
                    String[] parts = line.split(",");
                    int nextState = Integer.parseInt(parts[0]);
                    int writeSymbol = Integer.parseInt(parts[1]);
                    char moveDir = parts[2].charAt(0);
                    states[s].setTransition(a, new Transition(nextState, writeSymbol, moveDir));
                }
            }

            // Parse input string (last line, may be empty for epsilon)
            String inputLine = br.readLine();
            String input = (inputLine == null) ? "" : inputLine.trim();

            // Build and run the TM
            TM machine = new TM(numStates, numInputSymbols, states);
            machine.loadInput(input);
            String result = machine.run();

            System.out.println(result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        }
    }
}
