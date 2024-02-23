
public class Pair<T1, T2> {

    Eval T1;
    Position T2;

    public Pair(Eval utility, Position position) {
        this.T1 = utility;
        this.T2 = position;
    }

    public Eval getEval() {
        return this.T1;
    }

    public Position getPosition() {
        return this.T2;
    }

    public void setEval(Eval utility) {
        this.T1 = utility;
    }

    public void setPosition(Position position) {
        this.T2 = position;
    }

}
