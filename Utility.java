
public class Utility {

    GameState state;
    IOthelloAI ai;
    private int heuristic;

    public Utility(GameState s, IOthelloAI ai) {
        this.state = s;
        this.ai = ai;
    }

    public int getHeuristic() {
        return heuristic;

    }

    public void setHeuristic(int value) {
        this.heuristic = value;
    }

}
