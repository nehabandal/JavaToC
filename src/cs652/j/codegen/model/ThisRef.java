package cs652.j.codegen.model;

import cs652.j.semantics.JClass;
import cs652.j.semantics.JField;

/**
 * Created by npbandal on 4/2/17.
 */
public class ThisRef extends Expr {
    public ThisRef(JClass jClass) {
        super(new DataType(jClass.getName()));
    }
}
