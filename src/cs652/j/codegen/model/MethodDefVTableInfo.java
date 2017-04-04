package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/2/17.
 */
public class MethodDefVTableInfo extends OutputModelObject {
    public String className;
    public String funcName;

    public MethodDefVTableInfo(String className, String funcName) {
        this.className = className;
        this.funcName = funcName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodDefVTableInfo that = (MethodDefVTableInfo) o;

        if (!className.equals(that.className)) return false;
        return funcName.equals(that.funcName);
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + funcName.hashCode();
        return result;
    }
}
