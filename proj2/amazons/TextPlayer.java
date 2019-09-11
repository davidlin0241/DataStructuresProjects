package amazons;

import static amazons.Move.isGrammaticalMove;
import static amazons.Square.sq;

/** A Player that takes input as text commands from the standard input.
 *  @author David Lin
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else if (isGrammaticalMove(line)) {

                String[] s = line.split("-|\\(|\\s+");
                Square s1;
                s1 = sq(s[0]);
                Piece p = board().get(s1);

                if (!board().isTurn(p)
                        || Move.mv(line) == null
                        || !board().isLegal(Move.mv(line))) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                } else {
                    return line;
                }
            } else {
                return line;
            }
        }
    }
}
