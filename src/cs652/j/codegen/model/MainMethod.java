package cs652.j.codegen.model;


/**
 * Created by npbandal on 3/29/17.
 */
public class MainMethod extends OutputModelObject {

    public
    @ModelElement
    Block body;

    public
    @ModelElement
    VarDef args;

    public
    @ModelElement
    VarDef returnType;

    public
    @ModelElement
    VarDef funcName;

}
