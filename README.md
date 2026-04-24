------------------------------------------------------------------------
Project 3: Turing Machine Simulator
CS 361
------------------------------------------------------------------------

Authors: Diego Dominguez, Hunter McCallister

------------------------------------------------------------------------
Overview
------------------------------------------------------------------------

This project implements a deterministic Turing Machine simulator with a
bi-infinite tape. The simulator reads a TM encoding and input string from
a file, executes the machine step by step, and prints the contents of all
visited tape cells upon halting.

------------------------------------------------------------------------
Files
------------------------------------------------------------------------

tm/TMSimulator.java  - Main class. Parses the input file and runs the
                       simulation.
tm/TM.java           - Turing Machine class. Stores the transition table
                       and tape, and executes the simulation loop.
tm/TMState.java      - Represents a single state with its transitions
                       for each tape symbol.
tm/Transition.java   - Represents a single transition (next state,
                       write symbol, move direction).
README               - This file.

------------------------------------------------------------------------
Compiling and Running
------------------------------------------------------------------------

From the project root directory (the directory containing the tm/ folder):

  To compile:
    javac tm/*.java

  To run:
    java tm.TMSimulator <inputFile>

  Example:
    java tm.TMSimulator file0.txt

------------------------------------------------------------------------
Implementation Approach
------------------------------------------------------------------------

The simulator is designed with both correctness and performance in mind.

Tape Representation:
  The bi-infinite tape is represented as a single contiguous int array
  with an offset so that tape position 0 maps to tape[offset]. The
  head operates as a direct array index, eliminating branching on every
  read and write. The array is pre-allocated to 64K cells and doubles
  in size with re-centering if the head exceeds the bounds. Unvisited
  cells default to 0 (blank).

Transition Table:
  Transitions are stored in TMState objects for OO design, but are also
  flattened into three parallel int arrays (nextState, writeSymbol,
  moveDirection) indexed by (state * numTapeSymbols + symbol). This
  eliminates object access overhead during the simulation loop, which
  is the performance-critical section.

Simulation Loop:
  All frequently accessed fields (tape array, transition arrays, head
  position, visited bounds) are pulled into local variables before
  entering the loop. This avoids repeated field access overhead. Each
  iteration performs a single array read for the tape symbol, one
  indexed lookup into the transition arrays, a direct array write, and
  an integer add for the head move. The loop terminates when the machine
  enters the halting state (state n-1).

Output:
  After halting, the simulator prints the content of all tape cells from
  the leftmost visited position to the rightmost visited position,
  followed by a newline.

------------------------------------------------------------------------
