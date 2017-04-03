package cs652.j.codegen.model;

/**
 * Created by npbandal on 4/1/17.
 */
public class PrintStringStat extends OutputModelObject {
    public
    @ModelElement
    String args;

    public PrintStringStat(String args) {
        this.args = args;
    }
}
