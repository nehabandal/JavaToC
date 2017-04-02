package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 3/31/17.
 */
public class Block extends OutputModelObject {
    public
    @ModelElement
    List<OutputModelObject> locals = new ArrayList<>();

    public
    @ModelElement
    List<OutputModelObject> instrs = new ArrayList<>();

}
