package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class ReturnStat extends Stat {
    public
    @ModelElement
    Expr expr;

    public ReturnStat(Expr expr) {
        this.expr = expr;
    }
}
