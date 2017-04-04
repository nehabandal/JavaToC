package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/2/17.
 */
public class LiteralRef extends Expr {
    public String literal;

    public LiteralRef(String literal, DataType type) {
        super(type);
        this.literal = literal;
    }
}
