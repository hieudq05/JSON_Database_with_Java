package server;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Args {
    @Parameter(names = {"-t"}, description = "Type of request")
    private String type;

    @Parameter(names = {"-k"}, description = "Key")
    private String key;

    @Parameter(names = {"-v"}, description = "Value to set")
    private String value;

    @Parameter(names = {"-in"}, description = "Input file")
    private String inputFile;

    private static final Gson gson = new Gson();

    public String getType() {
        return type;
    }

    public String getKeyString() {
        return key;
    }

    public String[] getKey() {
        if (key == null) return null;

        // Try to parse as JSON array
        try {
            JsonElement element = JsonParser.parseString(key);
            if (element.isJsonArray()) {
                return gson.fromJson(element, String[].class);
            }
        } catch (Exception ignored) {
        }

        // If not JSON array, treat as single string
        return new String[]{key};
    }

    public String getValueString() {
        return value;
    }

    public JsonElement getValue() {
        if (value == null) return null;

        try {
            return JsonParser.parseString(value);
        } catch (Exception e) {
            // If not valid JSON, treat as string
            return gson.toJsonTree(value);
        }
    }

    public String getInputFile() {
        return inputFile;
    }
}
