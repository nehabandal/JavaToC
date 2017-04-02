package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class PrintStat extends OutputModelObject {
    public
    @ModelElement
    String args;

    public PrintStat(String args) {
        this.args = args;
    }
}
