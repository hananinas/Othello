
public class Pair<T1, T2> {

    Utility T1;
    Position T2;

    public Pair(Utility utility, Position position) {
        this.T1 = utility;
        this.T2 = position;
    }

    public Utility getUtility() {
        return this.T1;
    }

    public Position getPosition() {
        return this.T2;
    }

}
