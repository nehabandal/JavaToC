package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/2/17.
 */
public class MethodCall extends Expr {
    public String name;

    public
    @ModelElement
    List<Expr> args = new ArrayList<>();

    public
    String receiverType;

}
