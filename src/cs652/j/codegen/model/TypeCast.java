package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class TypeCast extends Expr {
    public
    @ModelElement
    Expr expr;

    public TypeCast(Expr expr) {
        this.type = expr.type;
        this.expr = expr;
    }
}
