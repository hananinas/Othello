
public class Eval {

    GameState state;
    IOthelloAI ai;
    private int value = Integer.MIN_VALUE;

    public Eval(GameState s, IOthelloAI ai) {
        this.state = s;
        this.ai = ai;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void evaluate() {
        int[] tokens = this.state.countTokens();
        setValue(tokens[this.state.currentPlayer - 1] - tokens[(this.state.currentPlayer % 2)]);
    }

}
