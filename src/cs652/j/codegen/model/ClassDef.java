package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 3/29/17.
 */
public class ClassDef extends OutputModelObject {
    public String name;

    public
    @ModelElement
    List<MethodDef> methods = new ArrayList<>();

    public
    @ModelElement
    List<VarDef> fields = new ArrayList<>();

    public
    @ModelElement
    List<MethodDefVTableInfo> vtable = new ArrayList<>();
}
