package amazons;

import org.junit.Test;

import static amazons.IteratorTests.*;
import static amazons.Move.mv;
import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;
import static amazons.Square.sq;
/** The suite of all JUnit tests for the amazons package.
 *  @author David Lin
 */
public class UnitTest {

    private Board b;

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    @Test
    public void testDirection() {
        Square s1 = sq(5, 4);
        Square s2 = sq(4, 5);
        Square s3 = sq(4, 3);
        Square s4 = sq(6, 5);
        Square s5 = sq(7, 2);
        assertEquals(7, s1.direction(s2));
        assertEquals(5, s1.direction(s3));
        assertEquals(1, s1.direction(s4));
        assertEquals(3, s1.direction(s5));
    }

    @Test
    public void testUndo() {
        b = new Board();
        b.makeMove(sq(9, 3), sq(9, 0), sq(9, 3));
        b.undo();
        b.makeMove(sq(9, 3), sq(9, 0), sq(9, 3));
        b.makeMove(sq(0, 6), sq(0, 4), sq(0, 5));
        b.undoTwice();
        assertEquals(b.toString(), INIT_BOARD_STATE);
        assertEquals(0, b.numMoves());
        assertEquals(b.turn(), WHITE);
    }

    @Test
    public void testMakeMove() {
        b = new Board();
        b.makeMove(sq(9, 3), sq(9, 0), sq(9, 3));
        assertTrue(b.turn() == BLACK);
        b.makeMove(sq(0, 6), sq(0, 4), sq(0, 5));
        assertTrue(b.turn() == WHITE);
        b.makeMove(sq(0, 3), sq(1, 3), sq(2, 4));
        b.makeMove(sq(0, 4), sq(0, 3), sq(0, 0));
        b.makeMove(sq(9, 0), sq(9, 1), sq(9, 2));
        assertTrue(b.turn() == BLACK);
        assertEquals(5, b.numMoves());
        assertEquals(TESTMAKEMOVE_BOARD_STATE, b.toString());
    }

    @Test
    public void testCopy() {
        b = new Board();
        buildBoard(b, TESTCOPYBOARD);
        Board temp = new Board(b);
        assertEquals(b.toString(), temp.toString());

        b = new Board();
        b.makeMove(sq(9, 3), sq(9, 0), sq(9, 3));
        b.makeMove(sq(0, 6), sq(0, 4), sq(0, 5));
        b.makeMove(sq(0, 3), sq(1, 3), sq(2, 4));
        b.makeMove(sq(0, 4), sq(0, 3), sq(0, 0));
        b.makeMove(sq(9, 0), sq(9, 1), sq(9, 2));
        temp = new Board(b);

        assertEquals(temp.turn(), b.turn());
        assertEquals(temp.winner(), b.winner());
        assertEquals(temp.numMoves(), b.numMoves());
        for (int i = 0; i < temp.stack().size(); i++) {
            assertTrue(temp.stack().get(i).equals(b.stack().get(i)));
        }
    }

    @Test
    public  void testisLegal() {
        b = new Board();
        assertTrue(b.isLegal(sq(3, 0)));
        assertTrue(b.isLegal(sq(9, 3)));
        assertFalse(b.isLegal(sq(4, 0), sq(6, 2)));
        assertFalse(b.isLegal(sq(3, 0), sq(4, 1), sq(4, 1)));
        assertTrue(b.isLegal(mv(sq(3, 0), sq(4, 1), sq(3, 0))));
        buildBoard(b, TESTCOPYBOARD);
        assertTrue(b.isLegal(mv(sq(1, 1), sq(0, 2), sq(2, 0))));
        buildBoard(b, ALMOSTFILLED2);
        b.put(WHITE, 2, 0);
        assertFalse(b.isLegal(mv(sq(2, 0), sq(1, 0), sq(1, 1))));
        buildBoard(b, SENDHELP);
        assertTrue(b.isLegal(mv(sq(0, 4), sq(1, 5), sq(1, 6))));
    }

    @Test
    public void testIsUnblockedMove() {
        b = new Board();
        assertTrue(b.isUnblockedMove(sq(0, 3), sq(0, 4), null));
        assertTrue(b.isUnblockedMove(sq(0, 3), sq(0, 5), null));
        assertFalse(b.isUnblockedMove(sq(0, 3), sq(0, 6), null));
        assertFalse(b.isUnblockedMove(sq(0, 3), sq(1, 5), null));
        assertFalse(b.isUnblockedMove(sq(0, 3), sq(3, 0), null));
        assertFalse(b.isUnblockedMove(sq(0, 3), sq(4, 0), null));
        assertTrue(b.isUnblockedMove(sq(0, 3), sq(0, 0), null));
        b.put(SPEAR, 4, 0);
        assertFalse(b.isUnblockedMove(sq(3, 0), sq(4, 0), null));
        assertFalse(b.isUnblockedMove(sq(6, 0), sq(4, 0), null));
        b.put(SPEAR, 7, 1);
        assertFalse(b.isUnblockedMove(sq(6, 0), sq(9, 3), null));
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
        assertTrue(Square.sq(0, 2).isQueenMove(Square.sq(1, 1)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board v) {
        v.put(EMPTY, Square.sq(0, 3));
        v.put(EMPTY, Square.sq(0, 6));
        v.put(EMPTY, Square.sq(9, 3));
        v.put(EMPTY, Square.sq(9, 6));
        v.put(EMPTY, Square.sq(3, 0));
        v.put(EMPTY, Square.sq(3, 9));
        v.put(EMPTY, Square.sq(6, 0));
        v.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                v.put(SPEAR, Square.sq(col, row));
            }
        }
        v.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                v.put(SPEAR, Square.sq(col, row));
            }
        }
        v.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            v.put(WHITE, Square.sq(lip, 2));
        }
        v.put(WHITE, Square.sq(2, 3));
        v.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
                    "   - - - B - - B - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   B - - - - - - - - B\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   W - - - - - - - - W\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - W - - W - - -\n";

    static final String TESTMAKEMOVE_BOARD_STATE =
                    "   - - - B - - B - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - B\n"
                            +
                    "   S - - - - - - - - -\n"
                            +
                    "   - - S - - - - - - -\n"
                            +
                    "   B W - - - - - - - S\n"
                            +
                    "   - - - - - - - - - S\n"
                            +
                    "   - - - - - - - - - W\n"
                            +
                    "   S - - W - - W - - -\n";


    static final String SMILE =
                    "   - - - - - - - - - -\n"
                            +
                    "   - S S S - - S S S -\n"
                            +
                    "   - S - S - - S - S -\n"
                            +
                    "   - S S S - - S S S -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - W - - - - W - -\n"
                            +
                    "   - - - W W W W - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n";


    static final Piece[][] TESTCOPYBOARD =
        {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, B },
            { S, E, E, E, E, E, E, E, E, E },
            { E, E, S, E, E, E, E, E, E, E },
            { B, W, E, E, E, E, E, E, E, S },
            { E, E, E, E, E, E, E, E, E, S },
            { E, W, E, E, E, E, E, E, E, W },
            { S, E, E, W, E, E, S, E, E, E },
        };

    static final Piece[][] ALMOSTFILLED2 =
        {
            { B, S, B, S, B, S, B, S, B, S },
            { B, S, B, S, B, S, B, S, B, S },
            { B, S, B, S, B, S, B, S, B, S },
            { B, S, B, S, B, S, B, S, B, S },
            { B, S, B, S, B, S, B, S, E, S },
            { B, S, B, S, B, S, B, S, E, S },
            { B, S, B, S, B, S, B, S, B, S },
            { S, S, S, S, B, S, B, S, B, S },
            { S, W, S, S, B, S, B, S, B, E },
            { E, E, E, B, S, B, S, B, S, B },
        };
}
