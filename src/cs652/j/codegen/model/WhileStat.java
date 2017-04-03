package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class WhileStat extends Stat {
    public
    @ModelElement
    Expr condition;

    public
    @ModelElement
    Stat stat;

    public WhileStat(Expr condition, Stat stat) {
        this.condition = condition;
        this.stat = stat;
    }
}
