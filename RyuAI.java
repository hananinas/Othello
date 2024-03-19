
/**
 * 
 * Minimax implementation of OthelloAI
 * 
 * @version 1.0
 */
public class RyuAI implements IOthelloAI {

    private int size;
    private int turn;
    private int maxDepth = 2;

    /**
     * Returns first legal move
     */
    public Position decideMove(GameState s) {

        var move = searchPair(s);

        return move.move;

    }

    private ValuedMove searchPair(GameState s) {

        var moves = s.legalMoves();
        turn = s.getPlayerInTurn();
        size = s.getBoard().length;

        int v = Integer.MIN_VALUE;
        Position bestMove = null;

        for (Position move : moves) {
            var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());
            duplicate.insertToken(move);

            int value = minValue(duplicate, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (value > v) {
                v = value;
                bestMove = move;
            }
        }

        return new ValuedMove(bestMove, v);
    }

    private int minValue(GameState s, int depth, int alpha, int beta) {

        depth++;

        var moves = s.legalMoves();

        if (s.isFinished()) {
            return getValue(s);
        }

        if (moves.size() == 0) {
            s.changePlayer();
            return maxValue(s, depth, alpha, beta);
        }

        int v = Integer.MAX_VALUE;

        for (Position move : moves) {
            var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());
            duplicate.insertToken(move);

            int value = depth < maxDepth ? maxValue(duplicate, depth, Math.min(alpha, v), beta) : eval(s);

            if (value < v) {
                v = value;
            }
            if (v <= alpha) {
                return v;
            }
        }

        return v;
    }

    private int maxValue(GameState s, int depth, int alpha, int beta) {

        depth++;

        var moves = s.legalMoves();

        if (s.isFinished()) {
            return getValue(s);
        }

        if (moves.size() == 0) {
            s.changePlayer();
            return minValue(s, depth, alpha, beta);
        }

        int v = Integer.MIN_VALUE;

        for (Position move : moves) {
            var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());
            duplicate.insertToken(move);

            int value = depth < maxDepth ? minValue(duplicate, depth, Math.max(alpha, v), beta) : eval(s);

            if (value > v) {
                v = value;
            }
            if (v >= beta) {
                return v;
            }
        }

        return v;
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

    class ValuedMove {
        public int value;
        public Position move;

        public ValuedMove(Position move, int value) {
            this.value = value;
            this.move = move;
        }
    }

}
