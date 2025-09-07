package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import server.Args;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private final String host;
    private final int port;
    private final String[] args;
    private final Gson gson = new Gson();

    public Client(String host, int port, String[] args) {
        this.host = host;
        this.port = port;
        this.args = args;
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket(InetAddress.getByName(host), port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Client started!");

            Args argsParams = new Args();
            JCommander.newBuilder()
                    .addObject(argsParams)
                    .build()
                    .parse(args);

            String request = gson.toJson(argsParams);
            outputStream.writeUTF(request);
            System.out.println("Sent: " + request);

            System.out.println("Received: " + inputStream.readUTF());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
