package amazons;


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;

import static amazons.Piece.*;
import static amazons.Move.mv;
import static amazons.Square.DIR;
import static amazons.Square.exists;
import static amazons.Square.sq;
import static java.lang.Math.abs;


/** The state of an Amazons Game.
 *  @author David Lin
 */
class Board {

    /**
     * An empty iterator for initialization.
     */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();
    /**
     * Piece whose turn it is (BLACK or WHITE).
     */
    private Piece _turn;
    /**
     * Cached value of winner on this board, or EMPTY if it has not been
     * computed.
     */
    private Piece _winner;
    /**
     * Board representation.
     */
    private Piece[][] _board;
    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 10;
    /**
     * Keeps track of all moves made.
     */
    private Stack<Move> _moves = new Stack<>();

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        init();
        _turn = model.turn();
        _winner = model.winner();
        _board = new Piece[SIZE][SIZE];

        for (int i = 0; i < model._board.length; i++) {
            _board[i] = model._board[i].clone();
        }

        _moves.addAll(model._moves);

    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _board = new Piece[SIZE][SIZE];

        for (Piece[] row : _board) {
            Arrays.fill(row, EMPTY);
        }

        _turn = WHITE;

        put(WHITE, sq(0, 3));
        put(WHITE, sq(6, 0));
        put(WHITE, sq(3, 0));
        put(WHITE, sq(9, 3));

        put(BLACK, sq(0, 6));
        put(BLACK, sq(3, 9));
        put(BLACK, sq(6, 9));
        put(BLACK, sq(9, 6));

    }

    /**
     * Return the Piece whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Change turns.
     */
    void changeTurn() {
        _turn = _turn.opponent();
    }

    /**
     * Return the moves stack.
     */
    Stack stack() {
        return _moves;
    }

    /**
     * Return the number of moves (that have not been undone) for this
     * board.
     */
    int numMoves() {
        return _moves.size();
    }

    /**
     * Return the winner in the current position, or null if the game is
     * not yet finished.
     */
    Piece winner() {
        return _winner;
    }

    /**
     * Checks for and returns the winner if applicable or null if the game is
     * not yet finished.
     */
    Piece determineWinner() {
        if (!legalMoves(_turn).hasNext()) {
            return _turn == BLACK ? WHITE : BLACK;
        }
        return null;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return _board[row][col];
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _board[s.row()][s.col()] = p;
    }


    /**
     * Set square (COL, ROW) to P.
     */
    final void put(Piece p, int col, int row) {
        put(p, sq(col, row));
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /**
     * Return boolean: checks whether P is a non-empty piece.
     */
    boolean isQueenOrSpear(Piece p) {
        return p == WHITE || p == BLACK || p == SPEAR;
    }

    /**
     * Return true iff FROM - TO is an unblocked queen move on the current
     * board, ignoring the contents of ASEMPTY, if it is encountered.
     * For this to be true, FROM-TO must be a queen move and the
     * squares along it, other than FROM and ASEMPTY, must be
     * empty. ASEMPTY may be null, in which case it has no effect.
     */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        Square s;
        Piece sP;
        int rowD, colD, steps;

        rowD = abs(to.row() - from.row());
        colD = abs(to.col() - from.col());

        if (!from.isQueenMove(to)) {
            return false;
        }

        if (rowD > colD) {
            steps = rowD;
        } else {
            steps = colD;
        }

        for (int i = 1; i <= steps; i++) {
            s = from.queenMove(from.direction(to), i);
            sP = _board[s.row()][s.col()];
            if (asEmpty != s && isQueenOrSpear(sP)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        Piece fromPiece = _board[from.row()][from.col()];

        if (fromPiece == BLACK || fromPiece == WHITE) {
            return true;
        }
        return false;

    }

    /**
     * Return true iff FROM-TO is a valid first part of move, ignoring
     * spear throwing.
     */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from)) {
            return isUnblockedMove(from, to, null);
        }
        return false;
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position. Second part of a move.
     */
    boolean isLegal(Square from, Square to, Square spear) {
        return isUnblockedMove(to, spear, from);
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position. Combining the two parts together.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to())
                && isLegal(move.from(), move.to(), move.spear());
    }

    /**
     * Returns true if it is the right turn for P.
     */
    boolean isTurn(Piece p) {
        return turn() == p;
    }

    /**
     * Move FROM-TO(SPEAR), assuming this is a legal move.
     */
    void makeMove(Square from, Square to, Square spear) {

        Move m = mv(from, to, spear);
        if (isTurn(get(from.col(), from.row())) && isLegal(m)) {
            if (_winner == null) {

                _moves.push(m);

                put(_turn, to.col(), to.row());
                put(EMPTY, from.col(), from.row());
                put(SPEAR, spear.col(), spear.row());
                changeTurn();
            }
        }
    }


    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /**
     * Undo one move. Has no effect on the initial board.
     */
    void undo() {
        if (!_moves.empty()) {
            Move m = _moves.pop();
            _board[m.spear().row()][m.spear().col()] = EMPTY;
            _board[m.from().row()][m.from().col()] =
                    _board[m.to().row()][m.to().col()];
            _board[m.to().row()][m.to().col()] = EMPTY;
            _turn = _turn.opponent();
        } else {
            throw new RuntimeException("No moves have been "
                    + "made or player has won");
        }
    }

    /**
     * Undo the previous two moves.
     */
    void undoTwice() {
        if (numMoves() >= 2) {
            undo();
            undo();
        } else {
            throw new RuntimeException("Less than 2 moves have been made");
        }
    }

    /**
     * Return an Iterator over the Squares that are reachable by an
     * unblocked queen move from FROM. Does not pay attention to what
     * piece (if any) is on FROM, nor to whether the game is finished.
     * Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     * feature is useful when looking for Moves, because after moving a
     * piece, one wants to treat the Square it came from as empty for
     * purposes of spear throwing.)
     */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /**
     * Return an Iterator over all legal moves on the current board.
     */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /**
     * Return an Iterator over all legal moves on the current board for
     * SIDE (regardless of whose turn it is).
     */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /**
     * An iterator used by reachableFrom.
     */
    private class ReachableFromIterator implements Iterator<Square> {

        /**
         * Starting square.
         */
        private Square _from;
        /**
         * Current direction.
         */
        private int _dir;
        /**
         * Current distance.
         */
        private int _steps;
        /**
         * Square treated as empty.
         */
        private Square _asEmpty;
        /**
         * Contains the next square to return.
         */
        private Square _nextSquare;

        /**
         * Iterator of all squares reachable by queen move from FROM,
         * treating ASEMPTY as empty.
         */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 1;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8 && _nextSquare != null;
        }

        @Override
        public Square next() {
            Square s = _nextSquare;
            _nextSquare = null;
            toNext();
            return s;
        }

        /**
         * Advance _dir and _steps, so that the next valid Square is
         * _steps steps in direction _dir from _from.
         */
        private void toNext() {
            Square to;
            int[] dirArray = DIR[_dir];
            int colScaled = dirArray[0] * _steps,
                    rowScaled = dirArray[1] * _steps,
                    colAdjusted = _from.col() + colScaled,
                    rowAdjusted = _from.row() + rowScaled;

            while (_dir < 8 && _nextSquare == null) {

                while (!exists(colAdjusted, rowAdjusted)) {
                    _dir += 1;
                    _steps = 1;

                    if (_dir >= 8) {
                        break;
                    }

                    dirArray = DIR[_dir];
                    colScaled = dirArray[0] * _steps;
                    rowScaled = dirArray[1] * _steps;
                    colAdjusted = _from.col() + colScaled;
                    rowAdjusted = _from.row() + rowScaled;
                }

                if (_dir < 8) {
                    to = _from.queenMove(_dir, _steps);
                    if (isUnblockedMove(_from, to, _asEmpty)) {
                        _nextSquare = to;
                        _steps += 1;
                    } else {
                        _dir += 1;
                        _steps = 1;
                    }

                    if (_dir >= 8) {
                        return;
                    }

                    dirArray = DIR[_dir];
                    colScaled = dirArray[0] * _steps;
                    rowScaled = dirArray[1] * _steps;
                    colAdjusted = _from.col() + colScaled;
                    rowAdjusted = _from.row() + rowScaled;
                }
            }
        }
    }

    /**
     * An iterator used by legalMoves.
     */
    private class LegalMoveIterator implements Iterator<Move> {

        /**
         * Color of side whose moves we are iterating.
         */
        private Piece _fromPiece;
        /**
         * Current starting square.
         */
        private Square _start;
        /**
         * Remaining starting squares to consider.
         */
        private Iterator<Square> _startingSquares;
        /**
         * Current piece's new position.
         */
        private Square _nextSquare;
        /**
         * Remaining moves from _start to consider.
         */
        private Iterator<Square> _pieceMoves;
        /**
         * Remaining spear throws from _piece to consider.
         */
        private Iterator<Square> _spearThrows;
        /**
         * Next move from iterator.
         */
        private Move _nextMove;
        /**
         * Current reachable square from start .
         */
        private Square _startMove;

        /**
         * All legal moves for SIDE (WHITE or BLACK).
         */
        LegalMoveIterator(Piece side) {
            _fromPiece = side;
            Square[] startings = new Square[4];
            int i = 0;

            for (int c = 0; c < _board.length; c++) {
                for (int r = 0; r < _board.length; r++) {
                    if (get(c, r) == side) {
                        startings[i] = sq(c, r);
                        i += 1;
                    }
                }
            }

            List<Square> startingList = Arrays.asList(startings);
            _startingSquares = startingList.iterator();
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _nextMove != null;
        }

        @Override
        public Move next() {
            Move m = _nextMove;
            toNext();
            return m;
        }

        /**
         * Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows.
         */
        private void toNext() {
            Square sT;
            Move m;

            if (_nextMove != null) {
                while (_spearThrows.hasNext()) {
                    sT = _spearThrows.next();
                    m = mv(_start, _startMove, sT);
                    if (isLegal(m)) {
                        _nextMove = m;

                        return;
                    }
                }


                while (_pieceMoves.hasNext()) {
                    _startMove = _pieceMoves.next();
                    _spearThrows = reachableFrom(_startMove, _start);
                    while (_spearThrows.hasNext()) {
                        sT = _spearThrows.next();
                        m = mv(_start, _startMove, sT);
                        if (isLegal(m)) {
                            _nextMove = m;
                            return;
                        }
                    }
                }
            }

            while (_startingSquares.hasNext()) {
                _start = _startingSquares.next();
                _pieceMoves = reachableFrom(_start, null);
                while (_pieceMoves.hasNext()) {
                    _startMove = _pieceMoves.next();
                    _spearThrows = reachableFrom(_startMove, _start);
                    while (_spearThrows.hasNext()) {
                        sT = _spearThrows.next();
                        m = mv(_start, _startMove, sT);
                        if (isLegal(m)) {
                            _nextMove = m;
                            return;
                        }
                    }
                }
            }

            _nextMove = null;
        }
    }


    @Override
    public String toString() {
        String lines = "  ";
        for (int r = SIZE - 1; r >= 0; r--) {
            for (int c = 0; c <= 9; c++) {
                lines += " " + _board[r][c].toString();
            }

            lines += "\n";

            if (r != 0) {
                lines += "  ";
            }

        }

        return lines;
    }
}

