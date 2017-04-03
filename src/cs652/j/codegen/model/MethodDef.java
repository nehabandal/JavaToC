package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/2/17.
 */
public class MethodDef extends OutputModelObject {

    public String className;

    public
    @ModelElement
    Block body;

    public
    @ModelElement
    List<VarDef> args = new ArrayList<>();

    public
    @ModelElement
    String returnType;

    public
    @ModelElement
    String funcName;
}
