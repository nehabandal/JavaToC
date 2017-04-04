package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/3/17.
 */
public class FuncPtrType extends OutputModelObject {
    public
    @ModelElement
    DataType returnType;

    public
    @ModelElement
    List<DataType> argTypes = new ArrayList<>();
}
