package cs652.j.codegen.model;

import cs652.j.semantics.JClass;
import cs652.j.semantics.JField;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by npbandal on 4/2/17.
 */
public class DataType extends OutputModelObject {
    public String name;
    public boolean isPrimitive;

    public DataType(String name) {
        this.name = name;
        isPrimitive = isPrimitive(name);
    }

    public static boolean isPrimitive(String type) {
        return new HashSet<>(Arrays.asList("int", "float", "void")).contains(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataType dataType = (DataType) o;

        return name != null ? name.equals(dataType.name) : dataType.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
