
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
        int[] tokens = this.state.countTokens();
        setHeuristic(tokens[this.state.currentPlayer - 1] - tokens[(this.state.currentPlayer % 2)]);
    }

}
