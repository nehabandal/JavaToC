package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class TypeCast extends Expr {
    public
    @ModelElement
    Expr expr;

    public TypeCast(Expr expr) {
        this(expr.type, expr);
    }

    public TypeCast(DataType type, Expr expr) {
        super(type);
        this.expr = expr;
    }
}
