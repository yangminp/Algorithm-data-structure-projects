package core;

public class SubNode implements OPNode {

    private EXPNode exp1;
    private EXPNode exp2;

    public SubNode(EXPNode exp1, EXPNode exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public int evaluate(Robot robot) {
        return exp1.evaluate(robot) - exp2.evaluate(robot);
    }

    @Override
    public String toString() {
        return "sub(" + exp1.toString() + ", " + exp2.toString() + ")";
    }

}
