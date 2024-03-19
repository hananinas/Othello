public class TestAI implements IOthelloAI {

    // Set the maximum depth of the search tree
    private int maxDepth = 6;
    private int turn;
    private int size;

    // Set the number of positions evaluated to zero
    private int positionsEvaluated = 0;

    // Default constructor
    public TestAI() {
    }

    // Constructor with a specified maximum depth
    public TestAI(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    // Override the decideMove method of the IOthelloAI interface
    @Override
    public Position decideMove(GameState s) {

        // Start the timer
        var start = System.nanoTime();

        // Perform the search and get the valued move
        var vMove = search(s);

        // Stop the timer and calculate the elapsed time
        var stop = System.nanoTime();
        float time = (stop - start) / 1000000000f;

        // Determine the side and print the search results
        String side = turn == 1 ? "Black" : "White";
        System.out.println(
                side + ": " + vMove.value + " | Positions evaluated: " + positionsEvaluated + " | Time: " + time + "s");

        // Return the best move
        return vMove.move;
    }

    // Helper method equivalent to max(), but allows for returning the best move
    // found
    private ValuedMove search(GameState s) {

        // Reset the number of positions evaluated, set the board size and player turn
        positionsEvaluated = 0;
        size = s.getBoard().length;
        turn = s.getPlayerInTurn();

        // Get the legal moves
        var moves = s.legalMoves();

        // Initialize the best value and move to null
        int bestValue = Integer.MIN_VALUE;
        Position bestMove = null;

        // Iterate over the legal moves
        for (Position move : moves) {

            // Increment the number of positions evaluated
            positionsEvaluated++;

            // Create a copy of the current game state and make a move in the copy.
            var copy = copyAndMove(s, move);

            // Calculate the value of the move using the min function
            var value = min(copy, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // Update the best value and move if this move has a higher value
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        // Return the best move and its value
        return new ValuedMove(bestMove, bestValue);
    }

    // This method represents the max player and returns the evaluation of the best
    // move found
    private int max(GameState s, int depth, int a, int b) {

        // Increase the depth counter to keep track of the recursion depth.
        depth++;

        // Get all legal moves in the current game state.
        var moves = s.legalMoves();

        // If the game is over, return the value of the game state.
        if (s.isFinished()) {
            return getValue(s);
        }
        // If there are no legal moves, pass the turn to the other player and continue
        // searching.
        if (moves.size() == 0) {
            s.changePlayer();
            return min(s, depth, a, b);
        }

        int bestValue = Integer.MIN_VALUE;

        // Loop through all legal moves and evaluate their values.
        for (Position move : moves) {

            // Increment the number of positions evaluated
            positionsEvaluated++;

            // Create a copy of the current game state and make a move in the copy.
            var copy = copyAndMove(s, move);

            // If the current depth is less than the maximum depth, continue the search with
            // min player.
            // Otherwise, evaluate the current game state using the evaluation function.
            var value = depth < maxDepth ? min(copy, depth, Math.max(a, bestValue), b) : eval(s);

            // Update the best value if the new value is greater than the current best
            // value.
            if (value > bestValue) {
                bestValue = value;
            }
            // Perform beta.
            if (value >= b) {
                return bestValue;
            }
        }
        // Return the best value found in this level of the game tree.
        return bestValue;
    }

    // This method represents the min player and returns the evaluation of the best
    // move found
    private int min(GameState s, int depth, int a, int b) {

        // Increase the depth counter to keep track of the recursion depth.
        depth++;

        // Get all legal moves in the current game state.
        var moves = s.legalMoves();

        // If the game is over, return the value of the game state.
        if (s.isFinished()) {
            return getValue(s);
        }
        // If there are no legal moves, pass the turn to the other player and continue
        // searching.
        if (moves.size() == 0) {
            s.changePlayer();
            return max(s, depth, a, b);
        }

        int bestValue = Integer.MAX_VALUE;

        // Loop through all legal moves and evaluate their values.
        for (Position move : moves) {

            // Increment the number of positions evaluated
            positionsEvaluated++;

            // Create a copy of the current game state and make a move in the copy.
            var copy = copyAndMove(s, move);

            // If the current depth is less than the maximum depth, continue the search with
            // min player.
            // Otherwise, evaluate the current game state using the evaluation function.
            int value = depth < maxDepth ? max(copy, depth, a, Math.min(b, bestValue)) : eval(s);

            // Update the best value if the new value is smaller than the current best
            // value.
            if (value < bestValue) {
                bestValue = value;
            }

            // Perform alpha pruning.
            if (value <= a) {
                return bestValue;
            }
        }
        return bestValue;
    }

    // A private helper method that calculates the value of a given game state
    private int getValue(GameState s) {
        int[] tokens = s.countTokens();
        return 1000 * (tokens[turn - 1] - tokens[turn % 2]);
    }

    // A private helper method that evaluates a given game state and returns a score
    private int eval(GameState s) {
        int[][] board = s.getBoard();
        int[] tokens = s.countTokens();
        int c = size - 1; // Index of corner
        int cVal = 50; // Points for corner

        // calculate placementValue based on tokens' positions on the board
        int placementValue = v(board[0][0], cVal) +
                v(board[0][c], cVal) +
                v(board[c][0], cVal) +
                v(board[c][c], cVal) +
                v(board[1][1], -cVal) +
                v(board[1][c - 1], -cVal) +
                v(board[c - 1][1], -cVal) +
                v(board[c - 1][c - 1], -cVal);

        // calculate token evaluation score based on difference between number of tokens
        // of each color
        float tpr = (tokens[0] + tokens[1]) / 64f;
        int tokenDiff = tokens[turn - 1] - tokens[turn % 2];
        float tokenEval = 10 * tokenDiff * (tpr - 0.4f);

        // combine placementValue and tokenEval to return final score
        return (int) (tokenEval + placementValue * (1 - tpr));
    }

    // A private helper method that creates a copy of a given game state and makes a
    // move on it
    private GameState copyAndMove(GameState s, Position p) {
        var copy = new GameState(s.getBoard(), s.getPlayerInTurn());
        copy.insertToken(p);
        return copy;
    }

    // A private helper method that returns a value based on whether a token is of
    // the current player's color, the opponent's color, or neither
    private int v(int tokenColor, int value) {
        return tokenColor == turn ? value : tokenColor == ((turn % 2) + 1) ? -value : 0;
    }

    // A private nested class that represents a move along with its value
    class ValuedMove {
        public int value;
        public Position move;

        public ValuedMove(Position move, int value) {
            this.value = value;
            this.move = move;
        }
    }
}