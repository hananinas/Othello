import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    private boolean firstMove = true;

    @Override
    public Position decideMove(GameState s) {

        ArrayList<Position> moves = s.legalMoves();

        int alphaMax = Integer.MAX_VALUE;
        int betaMin = Integer.MIN_VALUE;

        if (firstMove && !moves.isEmpty()) {
            this.firstMove = false;
            return moves.get(0);
        } else {
            return MaxValue(s, alphaMax, betaMin, 3).getPosition();
        }
    }

    private Pair<Eval, Position> MaxValue(GameState s, int alpha, int beta, int depth) {

        Position move = null;
        Eval v = new Eval(s, this);

        if (isCutOff(s, depth)) {
            return new Pair<Eval, Position>(v, null);
        }

        v.setHeuristic(Integer.MIN_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair = MinValue(Result(s, action), alpha, beta, depth - 1);
            int a = pair.getUtility().getHeuristic();

            if (a >= v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                alpha = Integer.max(alpha, v.getHeuristic());
                move = pair.getPosition();
            }

            if (v.getHeuristic() >= beta) {
                return new Pair<Eval, Position>(v, move);
            }
        }

        return new Pair<Eval, Position>(v, move);
    }

    private Pair<Eval, Position> MinValue(GameState s, int alpha, int beta, int depth) {

        Position move = null;
        Eval v = new Eval(s, this);

        if (isCutOff(s, depth)) {
            return new Pair<Eval, Position>(v, new Position(0, 0));
        }

        v.setHeuristic(Integer.MAX_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair = MaxValue(Result(s, action), alpha, beta, depth - 1);
            int a = pair.getUtility().getHeuristic();

            if (a <= v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                beta = Integer.min(alpha, v.getHeuristic());
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

        GameState newState = new GameState(s.getBoard(), s.currentPlayer);

        newState.insertToken(position);

        return newState;
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
