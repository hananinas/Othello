public class RyuAI implements IOthelloAI {

    private int turn;
    private final int maxDepth = 6;
    private int size;

    public Position decideMove(GameState s) {
        var start = System.nanoTime();

        Pair<Integer, Position> move = searchBestMove(s);
        var stop = System.nanoTime();

        float time = (stop - start) / 1000000000f;

        String side = turn == 1 ? "Black" : "White";

        System.out.println(
                side + ": " + "Position evaluated: " + move.T2 + " Value: " + move.T1 +
                        " | Time: " + time + "s");

        return move.T2;
    }

    /**
     * This function will search for the next best move to do for the (AI) player.
     * 
     * @param s the class to represent the state of a game of Othello.
     * @return a Pair with the best calculated position for the next move, and its
     *         associated value.
     */
    private Pair<Integer, Position> searchBestMove(GameState s) {
        turn = s.getPlayerInTurn();

        // equivalent to initializing alpha to NEGATIVE INFINITY
        int bestV = Integer.MIN_VALUE;

        size = s.getBoard().length;

        // the best found action (next position to move) so far, initialized to none
        Position bestAc = null;

        /*
         * for every legal action (move) available in the gamestate s, a value v is
         * given to minValue,
         * in order to prune and avoid searching for nodes the AI knows will be useless
         */
        for (Position action : s.legalMoves()) {
            GameState duplicate = new GameState(s.getBoard(), turn);
            duplicate.insertToken(action);
            int v = minValue(duplicate, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // is the currently checked action (move) better than the previously saved?
            if (v > bestV) {
                bestV = v;
                bestAc = action;
            }
        }

        return new Pair<>(bestV, bestAc);
    }

    /**
     * Evaluates the game state for the minimizing player.
     * 
     * @param s     the class to represent the state of a game of Othello.
     * @param depth the maximum depth the algorithm will look for in the tree.
     * @param alpha best value for the maximizing player (starts as
     *              Integer.MIN_VALUE).
     * @param beta  best value for the minimizing player (starts as
     *              Integer.MAX_VALUE).
     * @return the value of the current node being checked.
     */
    private int minValue(GameState s, int depth, int alpha, int beta) {

        // if no other available moves exists for both players, then goto eval(s) (where
        // 's' is the gamestate)
        if (s.isFinished())
            return eval(s);

        // if no other available moves exists for the current player, then switch player
        if (s.legalMoves().isEmpty()) {
            s.changePlayer();
            return maxValue(s, depth, alpha, beta);
        }

        int v = Integer.MAX_VALUE;

        /**
         * While maximum depth hasn't been reached, look for the next minimum value.
         * If the next calculated value (here 'v') isn't less than/equal to alpha,
         * then continue to look at other legal actions.
         */
        for (Position action : s.legalMoves()) {
            GameState duplicate = new GameState(s.getBoard(), turn);
            duplicate.insertToken(action);
            int value = (depth + 1) < maxDepth ? maxValue(duplicate, depth + 1, alpha, v) : eval(s);
            v = Math.min(v, value);
            if (v <= alpha)
                return v;
        }
        return v;
    }

    /**
     * Evaluates the game state for the maximizing player.
     * 
     * @param s     the class to represent the state of a game of Othello.
     * @param depth the maximum depth the algorithm will look for in the tree.
     * @param alpha best value for the maximizing player (starts as
     *              Integer.MIN_VALUE).
     * @param beta  best value for the minimizing player (starts as
     *              Integer.MAX_VALUE).
     * @return the value of the current node being checked.
     */
    private int maxValue(GameState s, int depth, int alpha, int beta) {

        // if no other available moves exists for both players, then goto eval(s) (where
        // 's' is the gamestate)
        if (s.isFinished())
            return eval(s);

        // if no other available moves exists for the current player, then switch player
        if (s.legalMoves().isEmpty()) {
            s.changePlayer();
            return minValue(s, depth, alpha, beta);
        }

        int v = Integer.MIN_VALUE;

        /**
         * While maximum depth hasn't been reached, look for the next maximum value.
         * If the next calculated value (here 'v') isn't more than/equal to beta,
         * then continue to look at other legal actions.
         */
        for (Position action : s.legalMoves()) {
            GameState duplicate = new GameState(s.getBoard(), turn);
            duplicate.insertToken(action);
            int value = (depth + 1) < maxDepth ? minValue(duplicate, depth + 1, v, beta) : eval(s);
            v = Math.max(v, value);
            if (v >= beta)
                return v;
        }
        return v;
    }

    /**
     * Evaluate the desirability of the given gamestate.
     * 
     * @param s the class to represent the state of a game of Othello.
     * @return an integer value, which can be interpreted as the desirable value of
     *         the current state,
     *         the player is in.
     */
    private int eval(GameState s) {
        int[][] board = s.getBoard();
        int[] tokens = s.countTokens();

        int tokenValue = tokens[turn - 1] - tokens[turn % 2];

        int placementValue = calculatePlacementValue(board);

        return (tokenValue + placementValue);
    }

    private int calculatePlacementValue(int[][] board) {
        final int cornerValue = 100;
        final int adjacentCornerValue = -25;
        final int edgeValue = 10;
        final int centerValue = 5;

        int score = 0;

        // Evaluate corners
        score += checkPlayer(board[0][0], cornerValue);
        score += checkPlayer(board[0][size - 1], cornerValue);
        score += checkPlayer(board[size - 1][0], cornerValue);
        score += checkPlayer(board[size - 1][size - 1], cornerValue);

        // Evaluate squares adjacent to corners
        if (board[0][0] == 0) { // Top-left corner is empty
            score += checkPlayer(board[0][1], adjacentCornerValue);
            score += checkPlayer(board[1][0], adjacentCornerValue);
            score += checkPlayer(board[1][1], adjacentCornerValue);
        }
        if (board[0][size - 1] == 0) { // Top-right corner is empty
            score += checkPlayer(board[0][size - 2], adjacentCornerValue);
            score += checkPlayer(board[1][size - 1], adjacentCornerValue);
            score += checkPlayer(board[1][size - 2], adjacentCornerValue);
        }
        if (board[size - 1][0] == 0) { // Bottom-left corner is empty
            score += checkPlayer(board[size - 2][0], adjacentCornerValue);
            score += checkPlayer(board[size - 2][1], adjacentCornerValue);
            score += checkPlayer(board[size - 1][1], adjacentCornerValue);
        }
        if (board[size - 1][size - 1] == 0) { // Bottom-right corner is empty
            score += checkPlayer(board[size - 2][size - 1], adjacentCornerValue);
            score += checkPlayer(board[size - 2][size - 2], adjacentCornerValue);
            score += checkPlayer(board[size - 1][size - 2], adjacentCornerValue);
        }

        // Evaluate edges
        for (int i = 1; i < size - 1; i++) {
            score += checkPlayer(board[0][i], edgeValue); // Top edge
            score += checkPlayer(board[size - 1][i], edgeValue); // Bottom edge
            score += checkPlayer(board[i][0], edgeValue); // Left edge
            score += checkPlayer(board[i][size - 1], edgeValue); // Right edge
        }

        // Evaluate center squares
        for (int i = 2; i < size - 2; i++) {
            for (int j = 2; j < size - 2; j++) {
                score += checkPlayer(board[i][j], centerValue);
            }
        }

        return score;
    }

    /**
     * Returns a value based on whether a token is of the current player's color,
     * the
     * opponent's color, or neither.
     * 
     * @param currentPlayer the current player.
     * @param value         the value to return if the token is of the current
     *                      player's color.
     * @return the value of the token.
     */
    private int checkPlayer(int currentPlayer, int value) {

        if (currentPlayer == turn) {
            return value;
        } else if (currentPlayer != 0 && currentPlayer != turn) {
            return -value;
        } else {

            return 0;
        }
    }

    /**
     * A type for reading/writing a position and its associated value
     */
    public class Pair<T1, T2> {

        int T1;
        Position T2;

        public Pair(int value, Position position) {
            this.T1 = value;
            this.T2 = position;
        }

    }
}