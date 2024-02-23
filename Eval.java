
public class Eval {

    GameState state;
    IOthelloAI ai;
    private int heuristic;
    int alpha = Integer.MAX_VALUE;
    int beta = Integer.MIN_VALUE;

    public Eval(GameState s, IOthelloAI ai) {
        this.state = s;
        this.ai = ai;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int value) {
        this.heuristic = value;
    }

    public void evaluate() {

        int score = 0;

        for (int i = 0; i < state.getBoard().length; i++) {
            for (int j = 0; j < state.getBoard()[i].length; j++) {
                if (state.getBoard()[i][j] == state.currentPlayer) {
                    score++; // Increase score if the cell belongs to the current player
                } else if (state.getBoard()[i][j] != 0) {
                    score--; // Decrease score if the cell belongs to the opponent
                }
            }
        }

        this.heuristic = score;
    }

}
