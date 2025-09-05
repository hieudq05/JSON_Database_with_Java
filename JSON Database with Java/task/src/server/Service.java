package server;

import com.beust.jcommander.JCommander;

import java.util.Map;

public class Service {
    private static Service instance;
    private final Database database = new Database();

    private Service() {}

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void startServer(String host, int port, int backlog) {
        Server server = new Server(host, port, backlog);
        server.start();
    }

    public String processCommand(Object argsParams) {
        Args args = new Args();

        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse((String[]) argsParams);

        return switch (args.getType()) {
            case "get" -> database.get(args.getIdx());
            case "delete" -> database.delete(args.getIdx()) ? "OK" : "ERROR";
            case "set" -> database.set(args.getIdx(), args.getMsg()) ? "OK" : "ERROR";
            case "exit" -> "OK";
            default -> "ERROR";
        };
    }

    public Map<Integer, String> getDatabase() {
        return database.getDatabase();
    }
}
