package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

public class Database {
    private Map<String, String> database;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Path pathDb = Paths.get(System.getProperty("user.dir") + "/src/server/data/db.json");
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    public static final String GET_REQUEST = "get";
    public static final String SET_REQUEST = "set";
    public static final String DELETE_REQUEST = "delete";
    public static final String EXIT_REQUEST = "exit";

    public Database() {
       database = readDb();
    }

    private Map<String, String> readDb() {
        try {
            String data = new String(Files.readAllBytes(pathDb));
            Map<String, String> database;
            Type mapType = new TypeToken<Map<String, String>>(){}.getType();
            database = data.isEmpty() ? new HashMap<>() : new Gson().fromJson(data, mapType);
            return database;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response processCommand(Args args) {
        switch (args.getType()) {
            case GET_REQUEST -> {
                return get(args.getKey());
            }
            case SET_REQUEST -> {
                return set(args.getKey(), args.getValue());
            }
            case DELETE_REQUEST -> {
                return delete(args.getKey());
            }
            case EXIT_REQUEST -> {
                return Response.builder().response(OK).build();
            }
            default -> {
                return Response.builder().response(ERROR).reason("Unknown command").build();
            }
        }

    }

    public synchronized Response set(String key, String value) {
        WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        if (isKeyAvailable(key)) {
            database.put(key, value);
            writeToDb();
            writeLock.unlock();
            return Response.builder().response(OK).build();
        }
        return Response.builder().response(ERROR).reason("No such key").build();
    }

    public synchronized Response get(String key) {
        ReadLock readLock = lock.readLock();
        readLock.lock();
        if (!isKeyAvailable(key) || !database.containsKey(key)) {
            readLock.unlock();
            return Response.builder().response(ERROR).reason("No such key").build();
        }
        Response response = Response.builder().response(OK).value(database.get(key)).build();
        readLock.unlock();
        return response;
    }

    public synchronized Response delete(String key) {
        WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        if (isKeyAvailable(key) && database.containsKey(key)) {
            database.remove(key);
            writeToDb();
            writeLock.unlock();
            return Response.builder().response(OK).build();
        }
        writeLock.unlock();
        return Response.builder().response(ERROR).reason("No such key").build();
    }

    public void writeToDb() {
        try {
            Files.write(pathDb, new Gson().toJson(database).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isKeyAvailable(String key) {
        return (!key.isEmpty() && key.length() <= 1000);
    }
}
