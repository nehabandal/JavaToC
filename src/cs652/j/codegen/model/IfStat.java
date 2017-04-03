package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class IfStat extends Stat {
    public
    @ModelElement
    Expr condition;

    public
    @ModelElement
    Stat stat;
}
