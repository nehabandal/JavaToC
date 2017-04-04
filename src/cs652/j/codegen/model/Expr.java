package cs652.j.codegen.model;

public abstract class Expr extends OutputModelObject {
    public
    @ModelElement
    DataType type;

    public Expr(DataType type) {
        this.type = type;
    }
}
