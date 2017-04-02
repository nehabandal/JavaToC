package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/1/17.
 */
public class VarDef extends OutputModelObject {
    public
    @ModelElement
    String type;

    public
    @ModelElement
    String id;

    public VarDef(String t, String i) {
        type = t;
        id = i;
    }
}
