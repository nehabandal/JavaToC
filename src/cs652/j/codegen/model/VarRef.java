package cs652.j.codegen.model;

import cs652.j.semantics.JField;

/**
 * Created by npbandal on 4/2/17.
 */
public class VarRef extends Expr {
    public JField jField;
    public String variable;

    public VarRef(JField jField, String variable) {
        super(new DataType(jField.getType().getName()));
        this.jField = jField;
        this.variable = variable;
    }
}
