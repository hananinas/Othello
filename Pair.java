
public class Pair<T1, T2> {

    Eval T1;
    Position T2;

    public Pair(Eval utility, Position position) {
        this.T1 = utility;
        this.T2 = position;
    }

    public Eval getUtility() {
        return this.T1;
    }

    public Position getPosition() {
        return this.T2;
    }

}
