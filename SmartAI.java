import java.util.ArrayList;
import java.util.List;

public class SmartAI implements IOthelloAI {

    @Override
    public Position decideMove(GameState s) {
        ArrayList<Position> moves = s.legalMoves();

        if (s.getPlayerInTurn() == 1) {
            return MiniMax(moves, s.legalMoves().getLast(), 3, true, s.isFinished());
        } else {
            return MiniMax(moves, s.legalMoves().getLast(), 3, false, s.isFinished());
        }
    }

    public Position MiniMax(ArrayList<Position> moves, Position position, int depth, boolean isMaximizingPlayer,
            boolean state) {
        if (depth == 0 || state) {
            return moves.get(0);
        } else if (isMaximizingPlayer) {
            Position max = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
            for (Position position1 : moves) {
                Position eval = MiniMax(moves, position1, depth - 1, false, state);
                max = findMax(max, eval);
            }
            return max;
        } else {
            Position min = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
            for (Position position2 : moves) {
                Position eval = MiniMax(moves, position2, depth - 1, true, state);
                min = findMin(min, eval);
            }
            return min;
        }

    }

    public Position findMax(Position max, Position child) {
        Position newMax = max;
        if (child.row > max.row) {
            newMax.row = child.row;
        }
        if (child.col > max.col) {
            newMax.col = child.col;
        }

        return newMax;
    }

    public Position findMin(Position min, Position child) {
        Position newMin = min;
        if (child.row < min.row) {
            newMin.row = child.row;
        }
        if (child.col < min.col) {
            newMin.col = child.col;
        }

        return newMin;
    }

}
