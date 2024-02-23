import java.util.ArrayList;
import java.util.List;

public class SmartAI implements IOthelloAI {

    @Override
    public Position decideMove(GameState s) {

        if (s.getPlayerInTurn() == 1) {
            return MiniMax(s, s.legalMoves().getLast(), 4, true);
        } else {
            return MiniMax(s, s.legalMoves().getLast(), 4, false);
        }
    }

    public Position MiniMax(GameState s, Position position, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || s.isFinished()) {
            return s.legalMoves().get(0);
        } else if (isMaximizingPlayer) {
            Position max = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
            for (Position position1 : s.legalMoves()) {
                Position eval = MiniMax(s, position1, depth - 1, false);
                max = findMax(max, eval);

            }
            return max;
        } else {
            Position min = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
            for (Position position2 : s.legalMoves()) {
                Position eval = MiniMax(s, position2, depth - 1, true);
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
