package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

class Server extends Thread {

    private String host;
    private int port;
    private int backlog;

    public Server(String host, int port, int backlog) {
        this.host = host;
        this.port = port;
        this.backlog = backlog;
    }

    @Override
    public void run() {
        Database database = new Database();
        AtomicBoolean running = new AtomicBoolean(true);
        try (
                ServerSocket serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host));
        ) {
            System.out.println("Server started!");
            while (running.get()) {
                Session session = new Session(serverSocket.accept(), database, running);
                session.start();
                session.join();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


