package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 3/31/17.
 */
public class Block extends Stat {
    public
    @ModelElement
    List<Stat> locals = new ArrayList<>();

    public
    @ModelElement
    List<Stat> instrs = new ArrayList<>();

}
