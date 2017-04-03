package cs652.j.codegen.model;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by npbandal on 4/2/17.
 */
public class Type extends OutputModelObject {
    public String name;
    public boolean isPrimitive;

    public Type(String name) {
        this.name = name;
        isPrimitive = isPrimitive(name);
    }

    private static boolean isPrimitive(String type) {
        return new HashSet<>(Arrays.asList("int", "float")).contains(type);
    }
}
