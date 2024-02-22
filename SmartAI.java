import java.util.ArrayList;

public class SmartAI implements IOthelloAI {

    @Override
    public Position decideMove(GameState s) {

        ArrayList<Position> moves = s.legalMoves(); // this stores the moves made by the ai and what postion we have
                                                    // used and then we
        if (s.getPlayerInTurn() == 1) {
            MiniMax(s, 3, true);
        } else {
            MiniMax(s, 3, false);
        }

        if (!moves.isEmpty())
            return moves.get(0);
        else
            return new Position(4, 1); // this function is just a simple ai that goes one postion down and up

    }

    public Position MiniMax(GameState s, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || s.isFinished()) {
            return new Position(-1, -1);
        }

        if (isMaximizingPlayer) {
            Position max = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
            for (Position position : s.legalMoves()) {
                Position eval = MiniMax(s, depth - 1, false);

            }
        }

        return null;
        // TODO Auto-generated method stub

    }

}
