package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author David Lin
 */
class MovingRotor extends Rotor {

    /** Notches of the Rotor. */
    private String _notches;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    /** Return a string, representing notches of the Rotor. */
    String getNotches() {
        return _notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        if (_notches.contains(Character.toString(setting()))) {
            return true;
        }
        return false;
    }


    @Override
    void advance() {
        set((alphabet().toInt(setting()) + 1) % size());
    }


}
