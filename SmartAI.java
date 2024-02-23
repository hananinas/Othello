import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    @Override
    public Position decideMove(GameState s) {

        ArrayList<Position> moves = s.legalMoves();

        if (!moves.isEmpty())
            return moves.get(0);
        else
            return MaxValue(s).getPosition();
    }

    private Pair<Utility, Position> MaxValue(GameState s) {

        Position move = null;
        Utility v = new Utility(s, this);

        if (s.isFinished()) {
            return new Pair<Utility, Position>(v, new Position(0, 0));
        }

        v.setHeuristic(Integer.MIN_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Utility, Position> pair = MinValue(Result(s, action));

            if (pair.getUtility().getHeuristic() > v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                move = pair.getPosition();
            }
        }

        return new Pair<Utility, Position>(v, move);
    }

    private Pair<Utility, Position> MinValue(GameState s) {

        Position move = null;
        Utility v = new Utility(s, this);

        if (s.isFinished()) {
            return new Pair<Utility, Position>(v, new Position(0, 0));
        }

        v.setHeuristic(Integer.MAX_VALUE);

        for (Position action : s.legalMoves()) {
            Pair<Utility, Position> pair = MaxValue(Result(s, action));

            if (pair.getUtility().getHeuristic() < v.getHeuristic()) {
                v.setHeuristic(pair.getUtility().getHeuristic());
                move = pair.getPosition();
            }
        }

        return new Pair<Utility, Position>(v, move);

    }

    public GameState Result(GameState s, Position position) {

        if (position == null) {
            return s;
        }

        GameState newState = new GameState(s.getBoard(), s.currentPlayer);

        newState.insertToken(position);

        return newState;
    }

}
