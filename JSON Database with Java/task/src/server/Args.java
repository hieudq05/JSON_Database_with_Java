package server;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-t"}, description = "Type of request")
    private String type;

    @Parameter(names = {"-i"}, description = "Index of the cell")
    private int idx;

    @Parameter(names = {"-m"}, description = "Value to set")
    private String msg;

    public String getType() {
        return type;
    }

    public int getIdx() {
        return idx;
    }

    public String getMsg() {
        return msg;
    }
}
