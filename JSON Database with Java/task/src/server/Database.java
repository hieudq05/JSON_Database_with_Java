package server;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Integer, String> database = new HashMap<>(1000);

    public boolean set(int key, String value) {
        if (isKeyAvailable(key)) {
            database.put(key, value);
            return true;
        }
        return false;
    }

    public String get(int key) {
        if (!isKeyAvailable(key)) {
            return "ERROR";
        }
        String value = database.get(key);
        return value == null ? "ERROR" : value;
    }

    public boolean delete(int key) {
        if (isKeyAvailable(key)) {
            database.remove(key);
            return true;
        }
        return false;
    }

    public boolean isKeyAvailable(int key) {
        return (key >= 1 && key <= 1000);
    }

    public Map<Integer, String> getDatabase() {
        return database;
    }
}
