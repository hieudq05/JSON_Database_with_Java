package server;

public class Main {

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 2610;

    public static void main(String[] args) {
        Service service = Service.getInstance();
        service.startServer(SERVER_HOST, SERVER_PORT, 50);
    }
}
