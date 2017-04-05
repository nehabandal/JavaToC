package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class VarDef extends Stat {
    public boolean isParameter;

    public
    @ModelElement
    DataType type;

    public
    @ModelElement
    String name;

    public VarDef(DataType type, String name) {
        this.type = type;
        this.name = name;
    }

    public static VarDef createParameter(DataType dataType, String name) {
        VarDef varDef = new VarDef(dataType, name);
        varDef.isParameter = true;
        return varDef;
    }

}
