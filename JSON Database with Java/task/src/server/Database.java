package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Database {
    private JsonObject database;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Path pathDb = Paths.get(System.getProperty("user.dir") + "/src/server/data/db.json");
//    private final Path pathDb = Paths.get(System.getProperty("user.dir") + "/JSON Database with Java/task/src/server/data/db.json");
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    public static final String GET_REQUEST = "get";
    public static final String SET_REQUEST = "set";
    public static final String DELETE_REQUEST = "delete";
    public static final String EXIT_REQUEST = "exit";

    public Database() {
        database = readDb();
    }

    private JsonObject readDb() {
        try {
            String data = new String(Files.readAllBytes(pathDb));
            JsonObject database;
            database = data.trim().isEmpty() ? new JsonObject() : new Gson().fromJson(data, JsonObject.class);
            return database;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response processCommand(client.Request request) {
        String type = request.getType();
        Object keyObj = request.getKey();

        // Convert key to String array
        String[] keys;
        if (keyObj instanceof String) {
            keys = new String[]{(String) keyObj};
        } else {
            // Handle JsonArray or other types
            Gson gson = new Gson();
            String keyJson = gson.toJson(keyObj);
            keys = gson.fromJson(keyJson, String[].class);
        }

        switch (type) {
            case GET_REQUEST -> {
                return get(keys);
            }
            case SET_REQUEST -> {
                return set(keys, request.getValue());
            }
            case DELETE_REQUEST -> {
                return delete(keys);
            }
            case EXIT_REQUEST -> {
                return Response.builder().response(OK).build();
            }
            default -> {
                return Response.builder().response(ERROR).reason("Unknown command").build();
            }
        }

    }

    public synchronized Response set(String[] keys, JsonElement value) {
        WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        JsonObject current = database;
        for (int i = 0; i < keys.length - 1; i++) {
            JsonElement child = current.get(keys[i]);
            if (isUnavailable(child)) {
                JsonObject newChild = new JsonObject();
                current.add(keys[i], newChild);
                current = newChild;
            } else {
                current = current.getAsJsonObject(keys[i]);
            }
        }
        current.add(keys[keys.length - 1], value);
        writeToDb();
        writeLock.unlock();
        return Response.builder().response(OK).build();

    }

    public synchronized Response get(String[] keys) {
        ReadLock readLock = lock.readLock();
        readLock.lock();
        JsonObject current = database;
        for (int i = 0; i < keys.length - 1; i++) {
            JsonElement child = current.get(keys[i]);
            if (isUnavailable(child)) {
                readLock.unlock();
                return Response.builder().response(ERROR).reason("No such key").build();
            }
            current = current.getAsJsonObject(keys[i]);
        }
        JsonElement result = current.get(keys[keys.length - 1]);
        if (result == null) {
            readLock.unlock();
            return Response.builder().response(ERROR).reason("No such key").build();
        }
        Response response = Response.builder().response(OK).value(result).build();
        readLock.unlock();
        return response;
    }

    public synchronized Response delete(String[] keys) {
        WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        JsonObject current = database;
        for (int i = 0; i < keys.length - 1; i++) {
            JsonElement child = current.get(keys[i]);
            if (isUnavailable(child)) {
                writeLock.unlock();
                return Response.builder().response(ERROR).reason("No such key").build();
            }
            current = current.getAsJsonObject(keys[i]);
        }
        if (current.get(keys[keys.length - 1]) == null) {
            writeLock.unlock();
            return Response.builder().response(ERROR).reason("No such key").build();
        }
        current.remove(keys[keys.length - 1]);
        writeToDb();
        writeLock.unlock();
        return Response.builder().response(OK).build();
    }

    public void writeToDb() {
        try {
            Files.write(pathDb, new Gson().toJson(database).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUnavailable(JsonElement value) {
        return value == null || !value.isJsonObject();
    }
}
