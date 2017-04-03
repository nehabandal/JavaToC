package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class VarRef extends Expr {
    public String variable;

    public VarRef(String variable) {
        this.variable = variable;
    }
}
