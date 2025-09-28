package client;

import com.beust.jcommander.JCommander;
import server.Args;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private final String host;
    private final int port;
    private final String[] args;

    public Client(String host, int port, String[] args) {
        this.host = host;
        this.port = port;
        this.args = args;
    }

    @Override
    public void run() {
        Args argsParams = new Args();
        JCommander.newBuilder()
                .addObject(argsParams)
                .build()
                .parse(args);

        try (
                Socket socket = new Socket(InetAddress.getByName(host), port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Client started!");

            outputStream.writeObject(args);
            System.out.println("Sent: " + argsParams.getType() + " " + argsParams.getKey() + (argsParams.getValue() == null ? "" : (" " + argsParams.getValue())));

            String input = inputStream.readUTF();
            System.out.println("Received: " + input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
