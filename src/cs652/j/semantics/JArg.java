package cs652.j.semantics;

import org.antlr.symtab.VariableSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by npbandal on 3/9/17.
 */
public class JArg extends VariableSymbol {
    public JArg(String name, ParserRuleContext tree) {
        super(name);
        setDefNode(tree);
    }
}
