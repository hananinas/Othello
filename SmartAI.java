import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    Pair<Eval, Position> pair;

    @Override
    public Position decideMove(GameState s) {

        int alphaMax = Integer.MAX_VALUE;
        int betaMin = Integer.MIN_VALUE;

        Position nextMove = MaxValue(s, alphaMax, betaMin, 10).getPosition();

        if (nextMove == null) {
            ArrayList<Position> moves = s.legalMoves();
            if (!moves.isEmpty())
                return moves.get(0);
            else
                return new Position(-1, -1);
        } else {
            return nextMove;
        }

    }

    private Pair<Eval, Position> MaxValue(GameState s, int alpha, int beta, int depth) {

        pair = new Pair<Eval, Position>(new Eval(s, this), null);

        if (isCutOff(s, depth)) {
            pair.getEval().evaluate();
            return pair;
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

        GameState newState = new GameState(s.getBoard(), s.getPlayerInTurn());

        newState.insertToken(action);

        return newState;
    }

    private Pair<Eval, Position> MinValue(GameState s, int alpha, int beta, int depth) {

        pair = new Pair<Eval, Position>(new Eval(s, this), null);

        if (isCutOff(s, depth)) {
            pair.getEval().evaluate();
            return pair;
        }

        pair.getEval().setHeuristic(Integer.MIN_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Eval, Position> pair2 = MaxValue(Result(s, action), alpha, beta, depth - 1);

            if (pair2.getEval().getHeuristic() < pair.getEval().getHeuristic()) {
                pair.getEval().setHeuristic(pair2.getEval().getHeuristic());
                pair.setPosition(action);
                alpha = Integer.max(alpha, pair.getEval().getHeuristic());
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
