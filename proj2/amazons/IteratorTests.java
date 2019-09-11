package amazons;
import org.junit.Test;

import static amazons.Piece.BLACK;
import static amazons.Piece.EMPTY;
import static amazons.Piece.WHITE;
import static amazons.Square.sq;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Junit tests for our Board iterators.
 *  @author David Lin
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());

        Square reachable;
        Board v = new Board();
        int numMoves = 0;
        buildBoard(v, ALMOSTFILLED2);
        Square start = sq(1, 1);
        Iterator<Square> reachablePos = v.reachableFrom(start, null);
        Iterator<Square> spearPos;
        while (reachablePos.hasNext()) {
            reachable = reachablePos.next();
            spearPos = v.reachableFrom(reachable, start);
            System.out.println(reachable + "*");
            while (spearPos.hasNext()) {
                System.out.println(spearPos.next());
                numMoves += 1;
            }
        }
        assertEquals(14, numMoves);
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        int numMoves = 0;

        Iterator<Move> legalMoves = b.legalMoves(WHITE);
        while (legalMoves.hasNext()) {
            legalMoves.next();
            numMoves += 1;
        }
        assertEquals(2176, numMoves);

        numMoves = 0;
        buildBoard(b, SENDHELP2);
        System.out.println(b.toString());
        Iterator<Move> legalMoves2 = b.legalMoves(BLACK);
        b.changeTurn();
        while (legalMoves2.hasNext()) {
            legalMoves2.next();
            numMoves += 1;
        }
        assertEquals(99, numMoves);
    }

    static void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, sq(col, row));
            }
        }
    }

    static final Piece E = EMPTY;

    static final Piece W = WHITE;

    static final Piece B = BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] SENDHELP2 =
        {
            { S, S, S, E, S, S, B, E, S, E },
            { S, S, E, E, S, S, B, E, E, E },
            { S, S, S, S, S, S, S, E, E, E },
            { B, S, S, E, S, S, S, E, E, E },
            { S, S, S, S, S, S, E, E, E, S },
            { S, S, S, S, S, S, S, E, E, E },
            { W, S, S, S, S, B, S, S, S, S },
            { S, S, S, S, S, S, S, S, E, E },
            { S, E, S, S, S, W, S, E, S, E },
            { S, S, S, W, E, S, W, S, S, S },
        };

    static final Piece[][] SENDHELP =
        {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { S, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E },
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
            { S, W, E, S, B, S, B, S, B, E },
            { E, E, E, B, S, B, S, B, S, B },
        };

    static final Piece[][] REACHABLEFROMTESTBOARD =
        {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
        };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    sq(5, 5),
                    sq(4, 5),
                    sq(4, 4),
                    sq(6, 4),
                    sq(7, 4),
                    sq(6, 5),
                    sq(7, 6),
                    sq(8, 7)));
}
