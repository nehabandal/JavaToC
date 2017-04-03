package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class CallStat extends Stat {
    public
    @ModelElement
    Expr call;

    public CallStat(Expr call) {
        this.call = call;
    }
}
