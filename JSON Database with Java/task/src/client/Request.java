package client;

import com.google.gson.JsonElement;

public class Request {
    private String type;
    private Object key;  // Can be String or String[]
    private JsonElement value;

    public Request(String type, Object key, JsonElement value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }
}

