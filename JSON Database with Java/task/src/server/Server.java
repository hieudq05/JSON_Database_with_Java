package server;

import java.net.InetAddress;
import java.net.ServerSocket;

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
        try (
                ServerSocket serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host));
        ) {
            System.out.println("Server started!");
            while (true) {
                Session session = new Session(serverSocket.accept());
                session.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


