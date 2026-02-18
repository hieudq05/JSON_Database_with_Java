package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Service {
    private static Service instance;
    private final Database database = new Database();
    private static final Gson gson = new GsonBuilder().create();

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

    public String processCommand(String requestJson) {
        try {
            client.Request request = gson.fromJson(requestJson, client.Request.class);
            Args args = new Args();

            Response response = database.processCommand(request);

            return gson.toJson(response);
        } catch (Exception e) {
            Response errorResponse = Response.builder()
                    .response("ERROR")
                    .reason(e.getMessage())
                    .build();
            return gson.toJson(errorResponse);
        }
    }
}
