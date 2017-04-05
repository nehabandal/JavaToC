package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/2/17.
 */
public class MethodDef extends MethodDefVTableInfo {
    public
    @ModelElement
    Block body;

    public
    @ModelElement
    List<VarDef> args = new ArrayList<>();

    public
    @ModelElement
    DataType returnType;

    public MethodDef(String className, String funcName) {
        super(className, funcName);
    }
}
