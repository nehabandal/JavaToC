package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class VarDef extends OutputModelObject {
    public boolean isParameter;

    public
    @ModelElement
    Type type;

    public
    @ModelElement
    String name;

    protected VarDef(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public static VarDef createParameter(Type type, String name) {
        VarDef varDef = create(type, name);
        varDef.isParameter = true;
        return varDef;
    }

    public static VarDef create(Type type, String name) {
        return new VarDef(type, name);
    }

}
