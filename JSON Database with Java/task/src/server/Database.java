package server;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String, String> database = new HashMap<>(1000);

    public boolean set(String key, String value) {
        if (isKeyAvailable(key)) {
            database.put(key, value);
            return true;
        }
        return false;
    }

    public String get(String key) {
        if (!isKeyAvailable(key)) {
            return "ERROR";
        }
        String value = database.get(key);
        return value == null ? "ERROR" : value;
    }

    public boolean delete(String key) {
        if (isKeyAvailable(key) && database.containsKey(key)) {
            database.remove(key);
            return true;
        }
        return false;
    }

    public boolean isKeyAvailable(String key) {
        return (key.length() >= 1 && key.length() <= 1000);
    }
}
