package server;

import com.beust.jcommander.JCommander;

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

    public Response processCommand(Object argsParams) {
        Args args = new Args();

        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse((String[]) argsParams);

        return switch (args.getType()) {
            case "get" -> {
                String result = database.get(args.getKey());
                yield result.equals("ERROR") ?
                        Response.builder().response("ERROR").reason("No such key").build() :
                        Response.builder().response("OK").value(result).build();
            }
            case "delete" ->
                    database.delete(args.getKey()) ?
                            Response.builder().response("OK").build() :
                            Response.builder().response("ERROR").reason("No such key").build();
            case "set" -> database.set(args.getKey(), args.getValue()) ?
                    Response.builder().response("OK").build() :
                    Response.builder().response("ERROR").build();
            case "exit" ->
                    Response.builder().response("OK").build();
            default ->
                    Response.builder().response("ERROR").build();
        };
    }
}
