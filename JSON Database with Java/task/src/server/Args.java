package server;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-t"}, description = "Type of request")
    private String type;

    @Parameter(names = {"-k"}, description = "Index of the cell")
    private String key;

    @Parameter(names = {"-v"}, description = "Value to set")
    private String value;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
