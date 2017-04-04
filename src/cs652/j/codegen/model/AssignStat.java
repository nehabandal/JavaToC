package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class AssignStat extends Stat {

    public
    @ModelElement
    Expr left, right;
}
