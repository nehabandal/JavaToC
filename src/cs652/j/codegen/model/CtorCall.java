package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class CtorCall extends Expr {
    public
    String className;

    public CtorCall(String className, DataType type) {
        super(type);
        this.className = className;
    }
}
