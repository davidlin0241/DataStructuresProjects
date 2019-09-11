package enigma;

import java.util.ArrayList;
import java.util.Arrays;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author David Lin
 */
class Permutation {

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** List of cycles. */
    private ArrayList<String> cyclesList;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        String[] cyclesArray = cycles.split("[()]");
        for (int i = 0; i < cyclesArray.length; i++) {
            cyclesArray[i] = cyclesArray[i].trim();
        }
        cyclesList = new ArrayList<>(Arrays.asList(cyclesArray));
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        return _alphabet.toInt(permute(_alphabet.toChar(p)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c)    {
        c = wrap(c);
        return _alphabet.toInt(invert(_alphabet.toChar(c)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (String s: cyclesList) {
            int index = s.indexOf(p);
            if (index != -1) {
                if (index + 1 == s.length()) {
                    return s.charAt(0);
                }
                return s.charAt(index + 1);
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (String s: cyclesList) {
            int index = s.indexOf(c);
            if (index != -1) {
                if (index - 1 == -1) {
                    return s.charAt(s.length() - 1);
                }
                return s.charAt(index - 1);
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int totalLen = 0;
        for (String s: cyclesList) {
            totalLen += s.length();
        }
        return totalLen == size();
    }

}
