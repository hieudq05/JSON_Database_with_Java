package server;

import com.beust.jcommander.JCommander;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String, String> database;
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    public static final String GET_REQUEST = "get";
    public static final String SET_REQUEST = "set";
    public static final String DELETE_REQUEST = "delete";
    public static final String EXIT_REQUEST = "exit";

    public Database() {
        database = new HashMap<>();
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

    public Response set(String key, String value) {
        if (isKeyAvailable(key)) {
            database.put(key, value);
            return Response.builder().response(OK).build();
        }
        return Response.builder().response(ERROR).reason("No such key").build();
    }

    public Response get(String key) {
        if (!isKeyAvailable(key) || !database.containsKey(key)) {
            return Response.builder().response(ERROR).reason("No such key").build();
        }
        return Response.builder().response(OK).value(database.get(key)).build();
    }

    public Response delete(String key) {
        if (isKeyAvailable(key) && database.containsKey(key)) {
            database.remove(key);
            return Response.builder().response(OK).build();
        }
        return Response.builder().response(ERROR).reason("No such key").build();
    }

    public boolean isKeyAvailable(String key) {
        return (!key.isEmpty() && key.length() <= 1000);
    }
}
