package enigma;

import java.util.ArrayList;
import java.util.List;

import static enigma.EnigmaException.*;
import static java.lang.Character.toUpperCase;

/** Class that represents a complete enigma machine.
 *  @author David Lin
 */
class Machine {

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of Rotors and number of pawls. */
    private int _numRotors, _pawls;
    /** List of all rotors and list of currently used rotors. */
    private List<Rotor> _allRotors, _currRotors = new ArrayList<Rotor>();
    /** plugboard. */
    private Permutation _plugboard;
    /** whether maachine has been set up yet. */
    private Boolean _setUp = false;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            List<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = new Permutation("", _alphabet);
    }

    /** Returns whether the machine has been set up. */
    Boolean setUp() {
        return _setUp;
    }

    /** Changes setUp instance variable to B. */
    void setSetUp(Boolean b) {
        _setUp = b;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return the current rotor list. */
    List<Rotor> currRotors() {
        return _currRotors;
    }

    /** Uses the ROTORS' names to see if there duplicates. */
    void noDuplicates(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < rotors.length; j++) {
                if (j != i) {
                    if (rotors[j].equals(rotors[i])) {
                        throw error("unknown or "
                                + "duplicated rotor name: %s", rotors[j]);
                    }
                }
            }
        }
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        noDuplicates(rotors);
        _currRotors.clear();
        Boolean found = false;
        for (String s : rotors) {

            for (int i = 0; i < _allRotors.size(); i++) {
                if (s.equals(_allRotors.get(i).name()
                        .toUpperCase())) {

                    if (_currRotors.size() == 0
                            && !_allRotors.get(i).reflecting()) {
                        throw error("first rotor must be a reflector");
                    }

                    _currRotors.add(_allRotors.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw error("unknown or duplicated rotor name: %s", s);
            }
            found = false;
        }

        if (_currRotors.size() != _numRotors) {
            throw error("Must fill all rotor slots");
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _currRotors.get(i + 1).set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        try {
            Rotor r;
            boolean atNotch = false;
            c = _plugboard.permute(c);

            for (int i = 1; i < _currRotors.size(); i++) {
                r = _currRotors.get(i);

                if (i + 1 < _currRotors.size() && r.rotates()) {
                    atNotch = _currRotors.get(i + 1).atNotch();
                }

                if (atNotch || i == _currRotors.size() - 1) {
                    r.advance();
                    if (i + 1 < _currRotors.size() - 1) {
                        _currRotors.get(i + 1).advance();
                        i++;
                    }
                    atNotch = false;

                }
            }

            for (int i = _currRotors.size() - 1; i > -1; i--) {
                r = _currRotors.get(i);
                c = r.convertForward(c);
            }

            for (int i = 1; i < _currRotors.size(); i++) {
                r = _currRotors.get(i);
                c = r.convertBackward(c);
            }

            return _plugboard.permute(c);

        } catch (EnigmaException e) {
            throw error("Integer is not in alphabet's range");
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int c;
        char charC;
        char[] msgA = msg.toCharArray();
        for (int i = 0; i < msgA.length; i++) {
            charC = toUpperCase(msgA[i]);
            if (_alphabet.contains(charC)) {
                c = _alphabet.toInt(charC);
                msgA[i] = _alphabet.toChar(convert(c));
            }
        }
        return new String(msgA);
    }
}
