import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    private boolean firstMove = true;
    Eval v;

    @Override
    public Position decideMove(GameState s) {
        ArrayList<Position> moves = s.legalMoves();

        int alphaMax = Integer.MAX_VALUE;
        int betaMin = Integer.MIN_VALUE;

        Position nextMove = MaxValue(s, alphaMax, betaMin, 3).getPosition();
        if (nextMove == null) {
            if (!moves.isEmpty()) {
                return moves.get(0);
            } else {
                return new Position(-1, -1); // return a default Position

            }
        } else {
            return nextMove;
        }

    }

    private Pair<Eval, Position> MaxValue(GameState s, int alpha, int beta, int depth) {

        Position move = null;
        v = new Eval(s, this);

        if (isCutOff(s, depth)) {
            v.evaluate();
            return new Pair<Eval, Position>(v, null);
        }

        v.setHeuristic(Integer.MIN_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair = MinValue(Result(s, action), alpha, beta, depth - 1);
            int a = pair.getUtility().getHeuristic();

            if (a > v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                move = pair.getPosition();
                alpha = Integer.max(alpha, v.getHeuristic());
            }

            if (v.getHeuristic() >= beta) {
                return new Pair<Eval, Position>(v, move);
            }
        }

        return new Pair<Eval, Position>(v, move);
    }

    private Pair<Eval, Position> MinValue(GameState s, int alpha, int beta, int depth) {

        Position move = null;
        v = new Eval(s, this);

        if (isCutOff(s, depth)) {
            v.evaluate();
            return new Pair<Eval, Position>(v, null);
        }

        v.setHeuristic(Integer.MAX_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair = MaxValue(Result(s, action), alpha, beta, depth - 1);
            int a = pair.getUtility().getHeuristic();

            if (a < v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                beta = Integer.min(beta, v.getHeuristic());
                move = pair.getPosition();
            }
        }

        if (v.getHeuristic() <= alpha) {
            return new Pair<Eval, Position>(v, move);
        }

        return new Pair<Eval, Position>(v, move);

    }

    public GameState Result(GameState s, Position position) {
        if (position == null) {
            return s;
        }

        s.insertToken(position);

        return s;
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
        }

        return false;

    }

}
