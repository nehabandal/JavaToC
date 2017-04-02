package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class AssignStat extends OutputModelObject {

    public String left, right;

    public AssignStat(String t, String i) {
        left = t;
        right = i;
    }
}
