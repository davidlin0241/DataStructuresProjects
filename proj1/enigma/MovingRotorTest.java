package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class MovingRotorTest {


    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    Alphabet ac = new CharacterRange('A', 'D');
    Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
    Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
    Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
    Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
    String setting = "AAA";
    Rotor[] machineRotors = {one, two, three, four};
    String[] rotors = {"R1", "R2", "R3", "R4"};
    Machine mach = new Machine(ac, 4, 3,
            new ArrayList<>(Arrays.asList(machineRotors)));

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotorz,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotorz.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkAtNotch() {
        int atNotches = 0;
        mach.insertRotors(rotors);
        mach.setRotors("CCC");
        for (Rotor r: mach.currRotors()) {
            if (r.atNotch()) {
                atNotches += 1;
            }
        }
        assertEquals(3, atNotches);

        atNotches = 0;
        mach.setRotors("CDC");
        for (Rotor r: mach.currRotors()) {
            if (r.atNotch()) {
                atNotches += 1;
            }
        }
        assertEquals(2, atNotches);

    }

}
