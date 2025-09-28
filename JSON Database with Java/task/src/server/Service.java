package server;

public class Service {
    private static Service instance;

    private Service() {
    }

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

    public Response processCommand(Args argsParams, Database database) {
        return database.processCommand(argsParams);
    }
}
