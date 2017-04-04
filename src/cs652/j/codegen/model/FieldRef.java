package cs652.j.codegen.model;

import cs652.j.semantics.JField;

/**
 * Created by npbandal on 4/3/17.
 */
public class FieldRef extends Expr {
    public
    @ModelElement
    Expr object;

    public String field;

    public FieldRef(Expr object, JField jField) {
        super(new DataType(jField.getType().getName()));
        this.object = object;
        this.field = jField.getName();
    }
}
