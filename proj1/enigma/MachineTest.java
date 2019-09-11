package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MachineTest {

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

    Alphabet _al = new CharacterRange('A', 'Z');
    Rotor r1 = new MovingRotor("I",
            new Permutation("(AELTPHQXRU) "
                    + "(BKNW) (CMOY) "
            + "(DFG) (IV) (JZ) (S)", _al), "Q");
    Rotor r2 = new MovingRotor("II",
            new Permutation("(FIXVYOMW) "
                    + "(CDKLHUP) (ESZ) "
            + "(BJ) (GR) (NT) (A) (Q)", _al), "E");
    Rotor r3 = new MovingRotor("III",
            new Permutation("(ABDHP"
                    + "EJT) "
            + "(CFLVMZOYQIRWUKXSG) (N)", _al), "V");
    Rotor r4 = new MovingRotor("IV",
            new Permutation("(AEPLIYW"
                    + "COXMRFZBSTGJQNH) "
                    + "(DV) (KU)", _al), "J");
    Rotor r6 = new MovingRotor("VI",
            new Permutation("(AJQ"
                    + "DVLEOZWIYTS) "
                    + "(CGMNHFUX) (BPRK)", _al), "ZM");
    Rotor rBeta = new FixedRotor("Beta",
            new Permutation(
                    "(ALBEVFCYO"
                    + "DJWUGNMQTZSKPR) (HIX)", _al));
    Rotor rG = new FixedRotor("GAMMA",
            new Permutation(
            "(AFNIRLBSQW"
                    + "VXGUZDKMTPCOYJHE)", _al));
    Rotor rC = new Reflector("C",
            new Permutation("(AR) (BD) (CO) (EJ) "
            + "(FN) (GT) (HK) (IV)"
                    + " (LM) (PW) (QZ) (SX) (UY)", _al));
    Rotor rB = new Reflector("B",
            new Permutation("(AE) (BN) (CK) (DQ) (FU) "
            + "(GY) (HW) (IJ) (LO)"
                    + " (MP) (RX) (SZ) (TV) (PW) (QZ) (SX) (UY)", _al));
    String setting2 = "BCDE";
    Rotor [] machineRotors2 = {rC, rB, rG, rBeta, r6, r4, r3, r2, r1};
    String[] insertedRotors = {"C", "GAMMA", "VI", "IV", "III", "II"};
    String[] insertedRotors2 = {"C", "GAMMA", "III", "II", "I", "VI"};
    Machine mach2 = new Machine(_al, 6, 4,
            new ArrayList<>(Arrays.asList(machineRotors2)));
    Machine mach3 = new Machine(_al, 5, 3,
            new ArrayList<>(Arrays.asList(machineRotors2)));
    @Test
    public void testInsertRotors() {
        mach2.insertRotors(insertedRotors);
        int i = 0;
        for (Rotor r: mach2.currRotors()) {
            assertEquals(r.name(), insertedRotors[i]);
            i++;
        }
    }

    @Test
    public void testSetRotors() {
        mach2.insertRotors(insertedRotors2);
        mach2.setRotors("ZZZZZ");
        for (int i = 1; i < mach2.currRotors().size(); i++) {
            assertEquals('Z', mach2.currRotors().get(i).setting());
        }

        assertEquals('A', mach2.currRotors().get(0).setting());

        mach2.setRotors("ABCDE");
        assertEquals('A', mach2.currRotors().get(1).setting());
        assertEquals('B', mach2.currRotors().get(2).setting());
        assertEquals('C', mach2.currRotors().get(3).setting());
        assertEquals('D', mach2.currRotors().get(4).setting());
        assertEquals('E', mach2.currRotors().get(5).setting());

    }
    @Test
    public void testConvert() {
        mach3.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        mach3.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", _al));
        mach3.setRotors("AXLE");
        String s = "FROM his shoulder Hiawatha",
                t = "Took the camera of rosewood",
                q = "QVPQS OKOIL PUBKJ ZPISF XDW",
                v = "BHCNS CXNUO AATZX SRCFY DGU";
        assertEquals("QVPQS OKOIL PUBKJ ZPISF XDW".replaceAll(" ", ""),
                mach3.convert(s).replaceAll(" ", ""));
        assertEquals(v.replaceAll(" ", ""),
                mach3.convert(t).replaceAll(" ", ""));
        mach3.setRotors("AXLE");
        assertEquals(s.replaceAll(" ", "").toUpperCase(),
                mach3.convert(q).replaceAll(" ", ""));
        assertEquals(t.replaceAll(" ", "").toUpperCase(),
                mach3.convert(v).replaceAll(" ", ""));
    }

    @Test
    public void testDoubleStep() {
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('c');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('d');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('c');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('d');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('d');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('d');
        assertEquals("ABDA", getSetting(ac, machineRotors));

        for (int i = 0; i < 6; i++) {
            mach.convert('d');
        }
        for (int i = 0; i < 7; i++) {
            mach.convert('a');
        }
        assertEquals("ACDB", getSetting(ac, machineRotors));

        setting = "CCC";
        mach.setRotors(setting);
        mach.convert('d');
        assertEquals("ADDD", getSetting(ac, machineRotors));

    }

    /** Helper method to get the String
     * representation of the current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machRotors) {
        String currSetting = "";
        for (Rotor r : machRotors) {
            currSetting += r.setting();
        }
        return currSetting;
    }
}
