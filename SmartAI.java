import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    Pair<Eval, Position> pair;

    @Override
    public Position decideMove(GameState s) {

        int alphaMax = Integer.MAX_VALUE;
        int betaMin = Integer.MIN_VALUE;

        int v = Integer.MIN_VALUE;
        Position bestMove = null;

        var moves = s.legalMoves();

        for (Position move : moves) {
            var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());
            duplicate.insertToken(move);

            int value = MinValue(duplicate, alphaMax, betaMin, 10);

            if (value > alphaMax) {

            }

        }

        Pair<Eval, Position> nextMove = MaxValue(s, alphaMax, betaMin, 10);

        return nextMove.getPosition();
    }

    private Pair<Eval, Position> MaxValue(GameState s, int alpha, int beta, int depth) {

        pair = new Pair<Eval, Position>(new Eval(s, this), null);

        var moves = s.legalMoves();

        if (isCutOff(s, depth)) {
            pair.getEval().evaluate();
            return pair;
        }

        // If there are no legal moves, change player and return the value of the next
        // player
        if (moves.size() == 0) {
            s.changePlayer();
            return MinValue(s, depth, alpha, beta);
        }

        pair.getEval().setHeuristic(Integer.MIN_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair2 = MinValue(Result(s, action), alpha, beta, depth - 1);

            if (pair2.getEval().getHeuristic() > pair.getEval().getHeuristic()) {
                pair.getEval().setHeuristic(pair2.getEval().getHeuristic());
                pair.setPosition(action);
                alpha = Integer.max(alpha, pair.getEval().getHeuristic());
            }

            if (pair.getEval().getHeuristic() >= beta) {
                return pair;
            }
        }

        return pair;
    }

    private GameState Result(GameState s, Position action) {

        var duplicate = new GameState(s.getBoard(), s.getPlayerInTurn());

        duplicate.insertToken(action);

        return duplicate;
    }

    private Pair<Eval, Position> MinValue(GameState s, int alpha, int beta, int depth) {

        pair = new Pair<Eval, Position>(new Eval(s, this), null);

        if (isCutOff(s, depth)) {
            pair.getEval().evaluate();
            return pair;
        }

        pair.getEval().setHeuristic(Integer.MIN_VALUE); // Changed from Integer.MAX_VALUE to Integer.MIN_VALUE

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair2 = MaxValue(Result(s, action), alpha, beta, depth - 1);

            if (pair2.getEval().getHeuristic() < pair.getEval().getHeuristic()) {
                pair.getEval().setHeuristic(pair2.getEval().getHeuristic());
                pair.setPosition(action);
                beta = Integer.min(beta, pair.getEval().getHeuristic()); // This should be beta, not alpha
            }
            if (pair.getEval().getHeuristic() <= alpha) {
                return pair;
            }
        }

        return pair;

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

}
