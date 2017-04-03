package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class TypeCast extends Expr {
    public
    @ModelElement
    Type type;

    public
    @ModelElement
    Expr expr;

    public TypeCast(Type type, Expr expr) {
        this.type = type;
        this.expr = expr;
    }
}
