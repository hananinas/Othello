// Class representing an AI player
public class RyuAI implements IOthelloAI {

    private int turn;
    private final int maxDepth = 6; // Set the maximum depth of the search tree
    private int size;

    // Method to decide the next move
    public Position decideMove(GameState s) {
        // var start = System.nanoTime();

        Pair<Integer, Position> move = searchBestMove(s);

        return move.T2;
    }

    // Method to search for the best move using minimax algorithm
    private Pair<Integer, Position> searchBestMove(GameState s) {
        turn = s.getPlayerInTurn();
        int bestV = Integer.MIN_VALUE;
        size = s.getBoard().length;
        Position bestAc = null;

        // Loop through all legal moves
        for (Position action : s.legalMoves()) {

            // Make a duplicate of the current state and apply the move
            GameState duplicate = new GameState(s.getBoard(), turn);
            duplicate.insertToken(action);

            // Calculate the value of the move using minimax
            int v = minValue(duplicate, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // Update the best value and best action if necessary
            if (v > bestV) {
                bestV = v;
                bestAc = action;
            }
        }
        return new Pair<>(bestV, bestAc);
    }

    // Min-value function for minimax algorithm
    private int minValue(GameState s, int depth, int alpha, int beta) {

        // Check if the game is finished
        if (s.isFinished())
            return eval(s); // Return the evaluation of the terminal game state

        // Check if no legal moves are available for the current player
        if (s.legalMoves().isEmpty()) {
            // Switch to the next player's turn
            s.changePlayer();
            return maxValue(s, depth, alpha, beta);
        }

        int v = Integer.MAX_VALUE;
        for (Position action : s.legalMoves()) {
            // Create a duplicate game state
            GameState duplicate = new GameState(s.getBoard(), turn);

            // Apply the current action to the duplicate state
            duplicate.insertToken(action);

            // Recursively call maxValue with increased depth, unless the maximum depth is
            // reached (cut-off), in which case, evaluate the game state directly
            int value = (depth + 1) < maxDepth ? maxValue(duplicate, depth + 1, alpha, v) : eval(s);

            // Update the minimum value
            v = Math.min(v, value);
            if (v <= alpha)
                return v; // Cut-off (alpha-beta pruning) the search if the value is less than or equal to
                          // alpha
        }

        // Return the minimum value
        return v;
    }

    // Max-value function for minimax algorithm
    private int maxValue(GameState s, int depth, int alpha, int beta) {
        if (s.isFinished())
            return eval(s);

        if (s.legalMoves().isEmpty()) {
            s.changePlayer();
            return minValue(s, depth, alpha, beta);
        }

        int v = Integer.MIN_VALUE;
        for (Position action : s.legalMoves()) {
            GameState duplicate = new GameState(s.getBoard(), turn);
            duplicate.insertToken(action);
            int value = (depth + 1) < maxDepth ? minValue(duplicate, depth + 1, v, beta) : eval(s);
            v = Math.max(v, value);
            if (v >= beta)
                return v;
        }

        // Return the maximum value
        return v;
    }

    // Evaluation function for the current state
    private int eval(GameState s) {
        int[][] board = s.getBoard();
        int[] tokens = s.countTokens();

        // Difference in token count between players
        int tokenValue = tokens[turn - 1] - tokens[turn % 2];

        // Ratio of total tokens played to total board size
        float tpr = (tokens[0] + tokens[1]) / 64f;

        // Evaluate the placement of tokens on the board
        int placementValue = calculatePlacementValue(board);

        // Combined evaluation score
        return (int) (10 * tokenValue * (tpr - 0.5f) + placementValue * (1 - tpr));
    }

    // Helper method to calculate placement value based on board position
    private int calculatePlacementValue(int[][] board) {
        // Define values for different board positions
        final int cornerValue = 100;
        final int adjacentCornerValue = -25;
        final int edgeValue = 10;
        final int centerValue = 5;

        int score = 0;

        // Evaluate corners
        score += v(board[0][0], cornerValue);
        score += v(board[0][size - 1], cornerValue);
        score += v(board[size - 1][0], cornerValue);
        score += v(board[size - 1][size - 1], cornerValue);

        // Evaluate squares adjacent to corners
        if (board[0][0] == 0) { // Top-left corner is empty
            score += v(board[0][1], adjacentCornerValue);
            score += v(board[1][0], adjacentCornerValue);
            score += v(board[1][1], adjacentCornerValue);
        }
        if (board[0][size - 1] == 0) { // Top-right corner is empty
            score += v(board[0][size - 2], adjacentCornerValue);
            score += v(board[1][size - 1], adjacentCornerValue);
            score += v(board[1][size - 2], adjacentCornerValue);
        }
        if (board[size - 1][0] == 0) { // Bottom-left corner is empty
            score += v(board[size - 2][0], adjacentCornerValue);
            score += v(board[size - 2][1], adjacentCornerValue);
            score += v(board[size - 1][1], adjacentCornerValue);
        }
        if (board[size - 1][size - 1] == 0) { // Bottom-right corner is empty
            score += v(board[size - 2][size - 1], adjacentCornerValue);
            score += v(board[size - 2][size - 2], adjacentCornerValue);
            score += v(board[size - 1][size - 2], adjacentCornerValue);
        }

        // Evaluate edges
        for (int i = 1; i < size - 1; i++) {
            score += v(board[0][i], edgeValue); // Top edge
            score += v(board[size - 1][i], edgeValue); // Bottom edge
            score += v(board[i][0], edgeValue); // Left edge
            score += v(board[i][size - 1], edgeValue); // Right edge
        }

        // Evaluate center squares
        for (int i = 2; i < size - 2; i++) {
            for (int j = 2; j < size - 2; j++) {
                score += v(board[i][j], centerValue);
            }
        }

        return score;
    }

    // Helper method to calculate value based on the current player and the value of
    // a square
    private int v(int currentPlayer, int value) {
        if (currentPlayer == turn) {
            return value;
        } else if (currentPlayer != 0 && currentPlayer != turn) {
            return -value;
        } else {
            return 0;
        }
    }

    // Inner class representing a pair of objects
    public class Pair<T1, T2> {
        int T1;
        Position T2;

        // Constructor to initialize the pair with a value and a position
        public Pair(int value, Position position) {
            this.T1 = value;
            this.T2 = position;
        }
    }
}