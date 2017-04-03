package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/1/17.
 */
public class PrintStringStat extends Stat {
    public String formattedString;

    public PrintStringStat(String formattedString) {
        this.formattedString = formattedString;
    }

}
