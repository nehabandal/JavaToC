package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/3/17.
 */
public class FieldRef extends Expr {
    public
    @ModelElement
    Expr object;

    public String field;

    public FieldRef(Expr object, String field) {
        this.object = object;
        this.field = field;
    }
}
