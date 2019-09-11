package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;


import static enigma.EnigmaException.*;
import static java.lang.System.*;

/** Enigma simulator.
 *  @author David Lin
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        String convertedMsg, nextLine;

        while (_input.hasNextLine()) {
            nextLine = _input.nextLine();
            if (nextLine.trim().equals("")) {
                _output.println();
            } else if (nextLine.substring(0, 1).equals("*")) {

                setUp(M, nextLine.substring(1).trim());
            } else {
                if (!M.setUp()) {
                    throw new EnigmaException("no rotors in machine");
                }
                convertedMsg = M.convert(nextLine);
                printMessageLine(convertedMsg);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String s = _config.nextLine().trim().toUpperCase(), next;
            char c;
            char smallest = s.charAt(0), largest = s.charAt(s.length() - 1);
            if (s.contains("(") || s.matches("([0-9]+ *)+") || s.equals("")) {
                throw error("Does not start with an alphabet "
                        + "range or initial line is blank");
            }

            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);

                if (c != '-') {
                    if (c < smallest) {
                        smallest = c;
                    } else if (c > largest) {
                        largest = c;
                    }
                }
            }

            _alphabet = new CharacterRange(smallest, largest);
            int numRotors = _config.nextInt(), pawls = _config.nextInt();
            if (numRotors <= pawls) {
                throw error("Number of rotors cannot be less "
                        + "than or equal to number of pawls");
            }
            List<Rotor> allRotors = new ArrayList<>();
            _config.nextLine();

            while (_config.hasNextLine()) {
                try {
                    next = _config.next();
                    allRotors.add(readRotor(next));
                } catch (NoSuchElementException e) {
                    continue;
                }
            }

            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Takes in NEXT string and returns a rotor,
     * reading its description
     * from _config. */
    private Rotor readRotor(String next) {
        String name, mutabilityAndNotches, cycles,
                cyclesTrimmed, temp, notches = "";
        int leftParenCount = 0, rightParenCount = 0;
        char firstChar;
        try {
            name = next.toUpperCase();
            mutabilityAndNotches = _config.next();
            cycles = _config.nextLine().trim();

            if (mutabilityAndNotches.contains("R")) {
                cyclesTrimmed = cycles.replaceAll("[()]", "");
                cyclesTrimmed = cyclesTrimmed.replaceAll(" ", "");

                while (cyclesTrimmed.length() != _alphabet.size()) {
                    temp = _config.nextLine().trim();
                    cycles += " " + temp;
                    cyclesTrimmed += temp.replaceAll(" ", "");
                    cyclesTrimmed = cyclesTrimmed.replaceAll("[()]", "");
                }
            }

            firstChar = mutabilityAndNotches.charAt(0);

            for (int i = 0; i < cycles.length(); i++) {
                if (cycles.charAt(i) == '(') {
                    leftParenCount += 1;
                } else if (cycles.charAt(i) == ')') {
                    rightParenCount += 1;
                }
            }

            if (leftParenCount != rightParenCount) {
                throw error("Not all cycles have an open parenthesis "
                        + "or a closing parenthesis");
            }

            Permutation perm = new Permutation(cycles, _alphabet);
            if (firstChar != 'R') {
                notches = mutabilityAndNotches.substring(1);
            }

            if (firstChar == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (firstChar == 'N') {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] settingsArray = settings.split(" ");
        String cycles = "";
        Permutation plugboard;
        int k = 0;

        M.insertRotors(Arrays.copyOfRange(settingsArray, 0, M.numRotors()));
        if (settingsArray[M.numRotors()].length() != M.numRotors() - 1) {
            throw error("settings line must have "
                    + "%d characters", M.numRotors() - 1);
        }

        for (int i = 1; i < M.numRotors(); i++) {
            if (M.currRotors().get(i).rotates()) {
                k++;
            }
        }
        if (k != M.numPawls()) {
            throw error("number of pawls does not "
                    + "match number of moving rotors");
        }

        M.setRotors(settingsArray[M.numRotors()]);

        for (int i = M.numRotors() + 1; i < settingsArray.length; i++) {
            cycles += settingsArray[i] + " ";
        }
        plugboard = new Permutation(cycles, _alphabet);
        M.setPlugboard(plugboard);
        M.setSetUp(true);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String msgTrimmed = msg.replaceAll(" ", ""), formattedMsg = "";
        while (msgTrimmed.length() > 5) {
            formattedMsg += msgTrimmed.substring(0, 5) + " ";
            msgTrimmed = msgTrimmed.substring(5);
        }
        formattedMsg += msgTrimmed;
        _output.println(formattedMsg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
