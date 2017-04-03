package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 3/29/17.
 */
public class ClassDef extends OutputModelObject {

    public
    String name;

    public
    @ModelElement
    List<MethodDef> methods = new ArrayList<>();

    public
    @ModelElement
    List<VarDef> fields = new ArrayList<>();

    public
    List<String> vtable = new ArrayList<>();

    private int counter = 0;

    public void addVTable(MethodDef m) {
        String qualifiedName = qualifiedName(m.funcName);
        vtable.add(String.format("(%s *)", qualifiedName));
    }

    private String qualifiedName(String funcName) {
        return name + "_" + funcName;
    }
}
