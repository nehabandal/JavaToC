package cs652.j.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npbandal on 4/1/17.
 */
public class PrintStat extends Stat {
    public String formattedString;

    public
    List<String> args = new ArrayList<>();

    public PrintStat(String formattedString) {
        this.formattedString = formattedString;
    }
}
