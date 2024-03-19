import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    Pair<Eval, Position> pair;

    private int depth = 10;

    private int turn;

    private int size;

    @Override
    public Position decideMove(GameState s) {

        var pair = search(s);

        return pair.getPosition();

    }

    private Pair<Eval, Position> search(GameState s) {
        size = s.getBoard().length;

        var pair = new Pair<Eval, Position>(new Eval(s, this), null);
        turn = s.getPlayerInTurn();

        var moves = s.legalMoves();

        for (Position move : moves) {

            var value = depth == 0 ? minValue(Result(s, move), Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1)
                    : eval(s);

            if (value > pair.getEval().getValue()) {
                pair.getEval().setValue(value);
                pair.setPosition(move);
            }
        }

        return pair;
    }

    private int maxValue(GameState s, int alpha, int beta, int depth) {

        var moves = s.legalMoves();

        if (isCutOff(s, depth)) {

            return getValue(s);
        }

        // If there are no legal moves, change player and return the value of the next
        // player
        if (moves.size() == 0) {
            s.changePlayer();
            return minValue(s, depth, alpha, beta);
        }

        int v = Integer.MIN_VALUE;

        for (Position action : s.legalMoves()) {

            var newValue = depth == 0 ? minValue(Result(s, action), Math.max(alpha, v), beta, depth - 1) : eval(s);

            if (newValue > v) {
                v = newValue;
            }

            if (newValue >= beta) {
                return v;
            }
        }

        return v;
    }

    private GameState Result(GameState s, Position action) {

        var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());

        duplicate.insertToken(action);

        return duplicate;
    }

    private int minValue(GameState s, int alpha, int beta, int depth) {
        var moves = s.legalMoves();

        if (isCutOff(s, depth)) {
            return getValue(s);

        }

        // If there are no legal moves, change player and return the value of the next
        // player
        if (moves.size() == 0) {
            s.changePlayer();
            return maxValue(s, depth, alpha, beta);
        }

        int v = Integer.MIN_VALUE;

        for (Position action : s.legalMoves()) {

            var newValue = maxValue(Result(s, action), alpha, Math.min(alpha, v), depth - 1);

            if (newValue < v) {
                v = newValue;
            }

            if (newValue <= alpha) {
                return v;
            }
        }

        return v;

    }

    /**
     * Our CUT-OFF function for pruning the returning tree.
     * 
     * @param s     Current game state
     * 
     * @param depth Depth of the tree
     */
    public boolean isCutOff(GameState s, int depth) {
        if (s.isFinished()) {
            return true;
        }
        if (depth == 0) {
            return true;
        } else
            return false;

    }

    private int getValue(GameState s) {
        int[] tokens = s.countTokens();
        return 1000 * (tokens[turn - 1] - tokens[turn % 2]);
    }

    private int eval(GameState s) {
        int[][] board = s.getBoard();
        int[] tokens = s.countTokens();
        int c = size - 1;
        int cVal = 50;
        int tokenValue = tokens[turn - 1] - tokens[turn % 2];
        int placementValue = v(board[0][0], cVal) +
                v(board[0][c], cVal) +
                v(board[c][0], cVal) +
                v(board[c][c], cVal) +
                v(board[1][1], -cVal) +
                v(board[1][c - 1], -cVal) +
                v(board[c - 1][1], -cVal) +
                v(board[c - 1][c - 1], -cVal);
        float tpr = (tokens[0] + tokens[1]) / 64f; // Tokens placed ratio
        return (int) (10 * tokenValue * (tpr - 0.4f) + placementValue * (1 - tpr));
    }

    private int v(int tokenColor, int value) {
        return tokenColor == turn ? value : tokenColor == ((turn % 2) + 1) ? -value : 0;
    }

}
