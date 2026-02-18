package server;

public class Main {
    public static void main(String[] args) {
        Service service = Service.getInstance();
        service.startServer("127.0.0.1", 23456, 50);
    }
}
